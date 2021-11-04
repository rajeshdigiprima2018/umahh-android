package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Modal.Question;
import Util.ConnectionDetector;
import Util.SessionManager;

/**
 * Created by Dell on 24-03-2019.
 */

public class Gk_result_Activity extends AppCompatActivity implements View.OnClickListener {

    ImageView IV_back;
    TextView Tv_wrong_Answer, Tv_Correct_Answer;
    Button Bt_Replay;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    RecyclerView recyclerView;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    public static List<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gk_result);
        Tv_Correct_Answer = (TextView) findViewById(R.id.Tv_Correct_Answer);
        Tv_wrong_Answer = (TextView) findViewById(R.id.Tv_wrong_Answer);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        SetData();
        Clicklistener();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int str_Wrong = extras.getInt("Wrong");
        int str_Correct = extras.getInt("Correct");
        try {
            System.out.println("str_Wrong-->" + str_Wrong);
            System.out.println("str_Correct-->" + str_Correct);
            String str_Wrong_ = String.valueOf(str_Wrong);
            String str_Correct_ = String.valueOf(str_Correct);

            Tv_Correct_Answer.setText(str_Correct_);
            Tv_wrong_Answer.setText(str_Wrong_);


        } catch (Exception e) {

        }


        recyclerView.setLayoutManager(new LinearLayoutManager(Gk_result_Activity.this));
        General_Knowledge_Activity.QuestionAdapter questionAdapter = new General_Knowledge_Activity.QuestionAdapter(Gk_result_Activity.this, questions, false);
        recyclerView.setAdapter(questionAdapter);


    }

    private void SetData() {


    }

    private void API() {

    }

    private void Find_element() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        IV_back = (ImageView) findViewById(R.id.IV_back);
        Bt_Replay = (Button) findViewById(R.id.Bt_Replay);


    }

    private void Clicklistener() {

        Bt_Replay.setOnClickListener(this);
        IV_back.setOnClickListener(this);
    }


    private void All_Depndency() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;

    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.Bt_Replay:


                Intent intent = new Intent(Gk_result_Activity.this, Quiz_Activity.class);

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.IV_back:

                Intent intent2 = new Intent(Gk_result_Activity.this, Quiz_Activity.class);

                startActivity(intent2);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Gk_result_Activity.this, GK_Question_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
