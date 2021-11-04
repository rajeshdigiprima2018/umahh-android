package app.com.ummah;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Abhi on 3/11/2019.
 */

public class Create_new_password_activity extends AppCompatActivity implements View.OnClickListener {

    EditText Et_email,Et_Phone;
    Button bt_submit;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setContentView(R.layout.create_new_pass_activity);
        Find_element();
        Click_Event();
    }

    private void Click_Event() {

        bt_submit.setOnClickListener(this);
    }

    private void Find_element() {

        Et_email = (EditText)findViewById(R.id.Et_email);
        Et_Phone = (EditText)findViewById(R.id.Et_Phone);
        bt_submit = (Button)findViewById(R.id.bt_submit);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_submit:



                break;
        }
    }
}
