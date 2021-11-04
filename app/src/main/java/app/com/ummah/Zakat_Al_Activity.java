package app.com.ummah;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Dell on 15-04-2019.
 */

public class Zakat_Al_Activity  extends AppCompatActivity implements View.OnClickListener {

    ImageView Iv_Donation, Iv_Zakat,Iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zakat_al_activity);
        Find_Element();
        Click_Event();
       /* All_Depndency();
        ProgressBar_Function();

        Mosque_Activity_Education();
        Recycle_view();
       */
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Zakat_Al_Activity.this, Mosque_Donation_Type_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void Click_Event() {
        Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {
        Iv_back = (ImageView)findViewById(R.id.Iv_back);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Iv_back:

                onBackPressed();

                break;
        }

    }

}
