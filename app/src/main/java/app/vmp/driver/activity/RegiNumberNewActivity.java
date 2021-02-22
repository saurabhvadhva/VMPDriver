package app.vmp.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;

import app.vmp.driver.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegiNumberNewActivity extends BaseActivity {

    @BindView(R.id.spinNumbers)
    AppCompatSpinner spinNumbers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_regi_number_new);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        ArrayList<String> list = new ArrayList<>();
        list.add("GJ 11 1234");
        list.add("MH 13 5678");
        list.add("RJ 08 2388");
        list.add("DL 12 9383");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinNumbers.setAdapter(adapter);
    }

    public void onNext(View view){
        Intent in = new Intent(RegiNumberNewActivity.this, MainActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void onPrev(View view){
        finish();
    }

}
