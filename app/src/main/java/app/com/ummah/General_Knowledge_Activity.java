package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modal.Question;
import Modal.Question_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.SessionManager;

/**
 * Created by Dell on 22-03-2019.
 */

public class General_Knowledge_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    Button bt_play;
    RecyclerView recyclerView;
    public static int Ans_correct_Sum;
    public static int Ans_Wrong_Sum;
    Intent intent;
    TextView tv_tilte;
    public ArrayList<Question_Modal> modalList;
    private List<Question> questions = new ArrayList<>();
    String KEY_Personnage_id, Title;
    private QuestionAdapter questionAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_knowledge_activity);
        tv_tilte = findViewById(R.id.tv_tilte);
        modalList = new ArrayList<>();
        Ans_correct_Sum = 0;
        Ans_Wrong_Sum = 0;
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        Clicklistener();
        SetData();


    }

    private void SetData() {

    }


    private void Clicklistener() {
        Iv_back.setOnClickListener(this);
        bt_play.setOnClickListener(this);
    }


    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url + "quiz/getAll/" + KEY_Personnage_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("quiz list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("quiz List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Question_Modal List_modal = new Question_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String Question = jsonChildNode.optString("question").toString();

                                            String option1 = jsonChildNode.optString("option1").toString();
                                            String option2 = jsonChildNode.optString("option2").toString();
                                            String option3 = jsonChildNode.optString("option3").toString();
                                            String option4 = jsonChildNode.optString("option4").toString();
                                            String answer = jsonChildNode.optString("answer").toString();
                                            String quiz_id = jsonChildNode.optString("quiz_id").toString();


                                            Question question = new Question();
                                            question.id = i;
                                            question.question = (i + 1) + " " + Question;
                                            question.option1 = option1;
                                            question.option2 = option2;
                                            question.option3 = option3;
                                            question.option4 = option4;
                                            // question.correctOption = new Random().nextInt(3);
                                            // question.answer = "Answer is: " + question.correctOption;
                                            question.answer = answer;
                                            questions.add(question);

                                            recyclerView.setLayoutManager(new LinearLayoutManager(General_Knowledge_Activity.this));
                                            questionAdapter = new QuestionAdapter(General_Knowledge_Activity.this, questions, true);
                                            recyclerView.setAdapter(questionAdapter);
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(General_Knowledge_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void showProgressDialog() {

        if (!progress_dialog.isShowing()) {
            progress_dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }


    private void Find_element() {


        bt_play = (Button) findViewById(R.id.bt_play);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(General_Knowledge_Activity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(General_Knowledge_Activity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//                    }
//                })
//        );
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
        intent = getIntent();
        KEY_Personnage_id = intent.getStringExtra("KEY_id");
        Title = intent.getStringExtra("KEY_Title");
        tv_tilte.setText(Title);

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_play:
                List<Question> questions = questionAdapter.GetItem();
                Ans_correct_Sum = 0;
                Ans_Wrong_Sum = 0;
                for (int i = 0; i < questions.size(); i++) {
                    if (questions.get(i).correct) {
                        Ans_correct_Sum++;
                    } else {
                        Ans_Wrong_Sum++;
                    }
                }

                Gk_result_Activity.questions = questionAdapter.GetItem();
                Intent intent = new Intent(General_Knowledge_Activity.this, Gk_result_Activity.class);
                intent.putExtra("Correct", Ans_correct_Sum);
                intent.putExtra("Wrong", Ans_Wrong_Sum);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(General_Knowledge_Activity.this, Quiz_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public static class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

        private static final String TAG = "QuestionAdapter";
        private LayoutInflater inflater;
        private List<Question> questions;
        private boolean IsEditable = true;

        public List<Question> GetItem() {
            return questions;
        }

        public QuestionAdapter(Context context, List<Question> questions, boolean Editable) {
            this.inflater = LayoutInflater.from(context);
            this.questions = questions;
            this.IsEditable = Editable;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.layout_question, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Question current = questions.get(position);
            holder.setQuestion(current.question);
            holder.setAnswer(current.answer);
            holder.setOptions(current, position);
            if (!IsEditable) {
                holder.mark.setVisibility(View.VISIBLE);
                if (current.correct)
                    holder.mark.setImageResource(R.drawable.ic_baseline_check_24);
                else
                    holder.mark.setImageResource(R.drawable.wrong);
            }
            Log.e(TAG, position + " :onBindViewHolder: " + current.toString());
        }

        @Override
        public int getItemCount() {
            if (questions == null) {
                return 0;
            } else {
                return questions.size();
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private LinearLayout linearLayoutContainer;
            private TextView textViewQuestion, textViewAnswer;
            private RadioGroup radioGroupOptions;
            private RadioButton radioButtonOption1, radioButtonOption2;
            private RadioButton radioButtonOption3, radioButtonOption4;
            private ImageView mark;

            public ViewHolder(View itemView) {
                super(itemView);
                linearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.linear_layout_container);
                textViewQuestion = (TextView) itemView.findViewById(R.id.text_view_question);
                textViewAnswer = (TextView) itemView.findViewById(R.id.text_view_answer);
                radioGroupOptions = (RadioGroup) itemView.findViewById(R.id.radio_group_options);
                radioButtonOption1 = (RadioButton) itemView.findViewById(R.id.radio_button_option_1);
                radioButtonOption2 = (RadioButton) itemView.findViewById(R.id.radio_button_option_2);
                radioButtonOption3 = (RadioButton) itemView.findViewById(R.id.radio_button_option_3);
                radioButtonOption4 = (RadioButton) itemView.findViewById(R.id.radio_button_option_4);
                mark = itemView.findViewById(R.id.mark);
                mark.setVisibility(View.INVISIBLE);
            }

            public void setQuestion(String question) {
                textViewQuestion.setText(question);
            }

            public void setAnswer(String answer) {
                textViewAnswer.setText(answer);
            }

            public void setOptions(Question question, int position) {
                radioGroupOptions.setTag(position);
                radioButtonOption1.setText(question.option1);
                radioButtonOption2.setText(question.option2);
                radioButtonOption3.setText(question.option3);
                radioButtonOption4.setText(question.option4);
                Log.e(TAG, position + " :setOptions: " + question.toString());


                if (question.isAnswered) {
                    radioGroupOptions.check(question.checkedId);
                } else {
                    radioGroupOptions.check(-1);
                }

                if (IsEditable) {
                    radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            int pos = (int) group.getTag();
                            Question que = questions.get(pos);
                            que.isAnswered = true;
                            que.checkedId = checkedId;

                            String Ans_String = question.answer;
                            String[] separated_ans = Ans_String.split(":");
                            String ans_main = separated_ans.length > 1 ? separated_ans[1] : separated_ans[0];


                            if (radioButtonOption1.isChecked()) {


                                String OP1 = String.valueOf(radioButtonOption1.getText());
                                String[] Option_1 = OP1.split(":");
                                String ans = Option_1.length > 1 ? Option_1[1] : Option_1[0];
                                if (ans_main.equals(ans)) {
                                    questions.get(pos).correct = true;
                                    Ans_correct_Sum = Ans_correct_Sum + 1;
                                } else {
                                    questions.get(pos).correct = false;
                                    Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                                }
                            } else if ((radioButtonOption2.isChecked())) {
                                String OP2 = String.valueOf(radioButtonOption2.getText());
                                String[] Option_2 = OP2.split(":");
                                String ans = Option_2.length > 1 ? Option_2[1] : Option_2[0];
                                if (ans_main.equals(ans)) {
                                    questions.get(pos).correct = true;
                                    Ans_correct_Sum = Ans_correct_Sum + 1;
                                } else {
                                    questions.get(pos).correct = false;
                                    Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                                }
                            } else if ((radioButtonOption3.isChecked())) {

                                String OP3 = String.valueOf(radioButtonOption3.getText());
                                String[] Option_3 = OP3.split(":");

                                String ans = Option_3.length > 1 ? Option_3[1] : Option_3[0];
                                if (ans_main.equals(ans)) {
                                    questions.get(pos).correct = true;
                                    Ans_correct_Sum = Ans_correct_Sum + 1;
                                } else {
                                    questions.get(pos).correct = false;
                                    Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                                }
                            } else if ((radioButtonOption4.isChecked())) {

                                String OP4 = String.valueOf(radioButtonOption4.getText());
                                String[] Option_4 = OP4.split(":");
                                String ans = Option_4.length > 1 ? Option_4[1] : Option_4[0];
                                if (ans_main.equals(ans)) {
                                    questions.get(pos).correct = true;
                                    Ans_correct_Sum = Ans_correct_Sum + 1;
                                } else {
                                    questions.get(pos).correct = false;
                                    Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                                }
                            }


                        }
                    });
                }
            }
        }
    }
}
