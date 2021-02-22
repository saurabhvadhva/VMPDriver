package app.vmp.driver.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;

import app.vmp.driver.R;

public class DownloadVideoDialog extends Dialog {

    ProgressBar progressBar;
    TextView tvProgress;
    String url;
    Activity mContext;
    private File mediaStorageDir;


    public DownloadVideoDialog(@NonNull Activity context
            , String url) {
        super(context);
        this.url = url;
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_download_video);
        setCancelable(false);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        tvProgress = findViewById(R.id.tvProgress);
        progressBar = findViewById(R.id.progress_bar);

        show();

        mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                mContext.getResources().getString(R.string.app_name));

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                download();
            }
        } else {
            download();
        }

    }

    private void download(){
        Log.e("ttt",url+"");
        PRDownloader.initialize(mContext);

        String path = System.currentTimeMillis()+".apk";

        PRDownloader.download(url,mediaStorageDir.getPath(),path).build().setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
                tvProgress.setText(prog+"%");
                progressBar.setProgress(prog);
            }
        }).start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                File apkFile = new File(mediaStorageDir.getPath(),path);
                Log.e("ttt",apkFile.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
//                Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", apkFile);
                Uri uri = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        ? FileProvider.getUriForFile(mContext.getApplicationContext(), mContext.getPackageName(), apkFile)
                        : Uri.fromFile(apkFile);

                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
                 cancel();
                 mContext.finish();
            }

            @Override
            public void onError(Error error) {

            }
        });

    }

}