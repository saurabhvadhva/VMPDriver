package app.vmp.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import app.vmp.driver.R;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;

import static app.vmp.driver.utils.Constant.SEL_LANG;

public class LanguageActivity extends BaseActivity {

    @BindView(R.id.tvHindi)
    AppCompatTextView tvHindi;
    @BindView(R.id.tvGujarati)
    AppCompatTextView tvGujarati;
    @BindView(R.id.tvEnglish)
    AppCompatTextView tvEnglish;

    private String from = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra("from");
        if (Util.getPrefData(SEL_LANG,LanguageActivity.this).equals("hi")){
            changeLang(0);
        } else if (Util.getPrefData(SEL_LANG,LanguageActivity.this).equals("gu")){
            changeLang(1);
        } else{
            changeLang(2);
        }
    }

    private int posSel = 0;

    public void onLangClick(View view){
        switch (view.getId()){
            case R.id.tvHindi:
                changeLang(0);
                break;
            case R.id.tvGujarati:
                changeLang(1);
                break;
            case R.id.tvEnglish:
                changeLang(2);
                break;
        }
    }

    private void changeLang(final int pos){
        posSel = pos;
        if (pos==0){
            tvHindi.setBackgroundResource(R.drawable.bg_primary_round_small);
            tvEnglish.setBackgroundResource(R.drawable.bg_border_primary_small);
            tvGujarati.setBackgroundResource(R.drawable.bg_border_primary_small);
            tvHindi.setTextColor(getResources().getColor(R.color.white));
            tvEnglish.setTextColor(getResources().getColor(R.color.black));
            tvGujarati.setTextColor(getResources().getColor(R.color.black));
        } else if (pos==1){
            tvHindi.setBackgroundResource(R.drawable.bg_border_primary_small);
            tvEnglish.setBackgroundResource(R.drawable.bg_border_primary_small);
            tvGujarati.setBackgroundResource(R.drawable.bg_primary_round_small);
            tvHindi.setTextColor(getResources().getColor(R.color.black));
            tvEnglish.setTextColor(getResources().getColor(R.color.black));
            tvGujarati.setTextColor(getResources().getColor(R.color.white));
        } else if (pos==2){
            tvHindi.setBackgroundResource(R.drawable.bg_border_primary_small);
            tvEnglish.setBackgroundResource(R.drawable.bg_primary_round_small);
            tvGujarati.setBackgroundResource(R.drawable.bg_border_primary_small);
            tvHindi.setTextColor(getResources().getColor(R.color.black));
            tvEnglish.setTextColor(getResources().getColor(R.color.white));
            tvGujarati.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public void onNext(View view){
        if (posSel == 0){
            Util.setPrefData(Constant.SEL_LANG,"hi",LanguageActivity.this);
        } else if (posSel == 1){
            Util.setPrefData(Constant.SEL_LANG,"gu",LanguageActivity.this);
        } else if (posSel == 2){
            Util.setPrefData(Constant.SEL_LANG,"en",LanguageActivity.this);
        }
        Util.changeLang(LanguageActivity.this);
        if (from.equals("home")){
            startActivity(new Intent(mContext, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            Intent in = new Intent(LanguageActivity.this, LoginActivity.class);
            startActivity(in);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }

    }

}
