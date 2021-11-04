package app.com.ummah;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.Mosque_donation_Detail_Adapter;
import Adapter.Select_mousque_Adapter;
import Modal.Bookmark_Modal;
import Modal.Mosque_donation_Detail_Modal;
import Modal.Select_Home_Mosque_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.SessionManager;
import paypalintegration.ConfirmationActivity;
import paypalintegration.PayPalConfig;

/**
 * Created by Dell on 15-04-2019.
 */

public class Mosque_Donation_Detail_Activity extends AppCompatActivity implements View.OnClickListener {

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    ImageView Iv_Donation, Iv_Zakat, Iv_back;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    String total = "";

    RecyclerView recyclerView;
    Mosque_donation_Detail_Adapter mosque_donation_detail_adapter;
    public ArrayList<Mosque_donation_Detail_Modal> modalList;
    Bookmark_Modal List_modal = new Bookmark_Modal();

    Bundle bundle;
    String KEY_dona_category_id, KEY_mosque_id, KEY_title;
    Button Bt_Done;
    String va;
    public static double Sum = 0;
    TextView Tv_sum, TV_title;
    LinearLayout Lay_member, Lay_paypal, Lay_amount;
    EditText Et_Amount, Et_Amount1;
    EditText Et_family_member;

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danation_activity);
        All_Depndency();
        Find_Element();
        Click_Event();
        ProgressBar_Function();
        Recycle_view();
        Danation_detail();
        if (KEY_title.equals("Zakat Al Fitr")) {
            Bt_Done.setVisibility(View.GONE);
        } else {
            Bt_Done.setVisibility(View.VISIBLE);

        }
        Et_Amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    UpdateAmount();
                }
            }
        });
        Et_Amount1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    UpdateAmount();
                }
            }
        });
        Et_family_member.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    UpdateAmount();
                }
            }
        });
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
        Bt_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateAmount();

            }
        });

    }

    private void UpdateAmount() {
        for (int i = 0; i < Mosque_donation_Detail_Adapter.flatList.size(); i++) {

            va = Mosque_donation_Detail_Adapter.flatList.get(i).getEditTextValue();
            //  tv.setText(tv.getText() + " " + Mosque_donation_Detail_Adapter.editModelArrayList.get(i).getEditTextValue() +System.getProperty("line.separator"));
            System.out.println("values---->>>><<<>>>>>  " + va);
            try {
                Sum = Sum + Double.parseDouble(va);
                System.out.println("Sum---->>>><<<>>>>>  " + Sum);
            } catch (Exception e) {
            }
        }

        if (KEY_title.equals("Zakat Al Fitr")) {
            double s = Integer.parseInt((Et_Amount1.getText().toString().length() == 0 ? "0" : Et_Amount1.getText().toString()));

            double a = Integer.parseInt((Et_Amount.getText().toString().length() == 0 ? "0" : Et_Amount.getText().toString()));
            double su = s * a;
            String sss = String.valueOf(su);
            Tv_sum.setText(sss);
            total = sss;
        } else {

            String s = String.valueOf(Sum);
            Tv_sum.setText(s);
            total = s;
        }

        Sum = 0;
    }

    private ArrayList<Mosque_donation_Detail_Modal> populateList() {

        ArrayList<Mosque_donation_Detail_Modal> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Mosque_donation_Detail_Modal editModel = new Mosque_donation_Detail_Modal();
            editModel.setEditTextValue(String.valueOf(i));
            list.add(editModel);
            // Toast.makeText(this, editModel, Toast.LENGTH_SHORT).show();
        }

        return list;
    }


    private void Danation_detail() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url +
                "donation/setAmount/getAllByCategoryId" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Donation Detail list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Donation Detail List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Mosque_donation_Detail_Modal List_modal = new Mosque_donation_Detail_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String mosque_id = jsonChildNode.optString("mosque_id").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String amount = jsonChildNode.optString("amount").toString();
                                            String status = jsonChildNode.optString("status").toString();
                                            String dona_amount_id = jsonChildNode.optString("dona_amount_id").toString();
                                            String id = jsonChildNode.optString("id").toString();

                                            Et_Amount1.setText(amount);

                                            List_modal.setMosque_id(mosque_id);
                                            List_modal.setTitle(title);
                                            List_modal.setEditTextValue(amount);
                                            List_modal.setStatus(title);
                                            List_modal.setStatus(status);
                                            List_modal.setDona_amount_id(dona_amount_id);
                                            List_modal.setId(id);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            mosque_donation_detail_adapter = new Mosque_donation_Detail_Adapter(Mosque_Donation_Detail_Activity.this, modalList, new Mosque_donation_Detail_Adapter.ChangeEdittext() {
                                                @Override
                                                public void onChange() {
                                                    UpdateAmount();
                                                }
                                            });
                                            recyclerView.setAdapter(mosque_donation_detail_adapter);
                                            mosque_donation_detail_adapter.notifyDataSetChanged();

                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Mosque_Donation_Detail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                map.put("dona_category_id", KEY_dona_category_id);
                map.put("mosque_id", KEY_mosque_id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Mosque_Donation_Detail_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void Click_Event() {
        Iv_back.setOnClickListener(this);
        Lay_paypal.setOnClickListener(this);
    }

    private void Find_Element() {
        Lay_amount = findViewById(R.id.Lay_amount);
        Et_Amount1 = findViewById(R.id.Et_Amount1);
        TV_title = findViewById(R.id.TV_title);
        Lay_paypal = findViewById(R.id.Lay_paypal);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Bt_Done = findViewById(R.id.Bt_Done);
        Tv_sum = findViewById(R.id.Tv_sum);
        Et_Amount = findViewById(R.id.Et_Amount);
        Lay_member = findViewById(R.id.Lay_member);
        Et_family_member = findViewById(R.id.Et_family_member);
        if (KEY_title.equals("Donation")) {
            Lay_member.setVisibility(View.GONE);
            Lay_amount.setVisibility(View.GONE);
            Et_family_member.setVisibility(View.GONE);
        } else if (KEY_title.equals("Zakat Al Fitr")) {
            recyclerView.setVisibility(View.GONE);
            TV_title.setText("Zakat Al Fitr");
        }
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
        modalList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        bundle = getIntent().getExtras();
        KEY_dona_category_id = bundle.getString("KEY_dona_category_id");
        KEY_mosque_id = bundle.getString("KEY_mosque_id");
        KEY_title = bundle.getString("KEY_title");
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;

    }


    @Override
    public void onBackPressed() {

//        Intent intent = new Intent(Mosque_Donation_Detail_Activity.this, Mosque_Donation_Type_Activity.class);
//        intent.putExtra("id", getIntent().getStringExtra("KEY_mosque_id"));
//        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:

                onBackPressed();

                break;
            case R.id.Lay_paypal:

                if (total.equals("")) {


                    new AlertDialog.Builder(this)
                            .setMessage("Please enter the valid value and click Done button")
                            .setCancelable(false)
                            .setNegativeButton("Okay", null)
                            .show();
                } else {
                    if (Double.parseDouble(total) != 0) {
                        getPayment();
                    } else {
                        new AlertDialog.Builder(this)
                                .setMessage("Please enter the valid value and click Done button")
                                .setCancelable(false)
                                .setNegativeButton("Okay", null)
                                .show();
                    }
                }
                break;


        }

    }


    private void getPayment() {

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(total)), "USD", "Mosque Donation Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);


        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        JSONObject paymentDetails = confirm.toJSONObject();
                        //       Log.i("paymentExample", paymentDetails);
/*
{"client":{"environment":"sandbox","paypal_sdk_version":"2.14.2","platform":
"Android","product_name":"PayPal-Android-SDK"},"response":{"create_time":"2020-04-30T08:22:08Z",
"id":"PAY-52Y80248JX2351503L2VIVMA","intent":"sale","state":"approved"},"response_type":"payment"}
 */
                        SavePayment(paymentDetails.getJSONObject("response").getString("id"), total);
                        //Starting a new activity for the payment details and also putting the payment details with intent

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void SavePayment(String txn, String amount) {
        showProgressDialog();


        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("mosque_id", getIntent().getStringExtra("KEY_mosque_id"));
        map.put("txn_id", txn);
        map.put("amount", amount);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.Base_url + "payment/savePayment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("searchMosque list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("searchMosque List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        Toast.makeText(Mosque_Donation_Detail_Activity.this, "Payment success.", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Mosque_Donation_Detail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                    }
                                }
                                hideProgressDialog();


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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + session.getToken_id().get(Constant.SHARED_PREFERENCE_TOKEN_KEY));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mosque_id", getIntent().getStringExtra("KEY_mosque_id"));
                map.put("txn_id", txn);
                map.put("amount", amount);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Mosque_Donation_Detail_Activity.this);
        requestQueue.add(stringRequest);
    }


    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Mosque_Donation_Detail_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //  String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
                String title1 = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.title)).getText().toString();
                Toast.makeText(getApplicationContext(), title1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));*/

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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
}
