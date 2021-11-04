package app.com.ummah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;

import java.util.Locale;

import Util.ConnectionDetector;
import Util.SessionManager;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Dell on 20-03-2019.
 */

public class Setting_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView IV_back;
    TextView quran_lang_name;
    Button Button_Log_out;
    private LinearLayout languageChangeBtn;
    private LinearLayout QuranlanguageChangeBtn;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        Clicklistener();

        String lng = session.getQuranLanguage();
        Locale loc = new Locale(lng);
        String name = loc.getDisplayLanguage(loc);
        quran_lang_name.setText(getString(R.string.Quran_Translation) + "(" + name + ")");
    }

    private void Clicklistener() {
        findViewById(R.id.tandcBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting_Activity.this, TandCActivity.class);
                startActivity(intent);
            }
        });
        languageChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeLanguage();
            }
        });
        QuranlanguageChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                API_Quran_Language();
            }
        });
        IV_back.setOnClickListener(this);
        Button_Log_out.setOnClickListener(this);
    }

    private void API_Quran_Language() {
        //Sura_Fragment.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progress_dialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getLanguage();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                int statusCode = response.code();
                String[] animals = new String[response.body().get("data").getAsJsonArray().size()];
                String[] lang = new String[response.body().get("data").getAsJsonArray().size()];

                for (int i = 0; i < response.body().get("data").getAsJsonArray().size(); i++) {
                    String lng = response.body().get("data").getAsJsonArray().get(i).getAsString();
                    Locale loc = new Locale(lng);
                    String name = loc.getDisplayLanguage(loc);
                    System.out.println(name);
                    lang[i] = lng;
                    animals[i] = name;
                }

                progress_dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting_Activity.this);
                builder.setTitle(getString(R.string.Quran_Translation));
                builder.setItems(animals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quran_lang_name.setText(getString(R.string.Quran_Translation) + "(" + animals[which] + ")");
                        session.setQuranLanguage(lang[which]);
                        API_Quran_Language(lang[which]);
                        API_Quran_Language_audio(lang[which]);
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                dialog.show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                // Log.e(TAG, t.toString());
            }
        });
    }

    private void API_Quran_Language_audio(String lang) {
        //Sura_Fragment.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progress_dialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getLanguageTranslationAudio();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                int statusCode = response.code();


                for (int i = 0; i < response.body().get("data").getAsJsonArray().size(); i++) {
                    JsonObject data = response.body().get("data").getAsJsonArray().get(i).getAsJsonObject();
                    String lng = data.get("language").getAsString();
                    if (lang.equals(lng)) {
                        Locale loc = new Locale(lng);
                        String name = loc.getDisplayLanguage(loc);
                        System.out.println(name);
                        data.get("identifier").getAsString();
                        quran_lang_name.setText(getString(R.string.Quran_Translation) + "(" + name + ")");
                        session.setQuranLanguageAudio(data.get("identifier").getAsString());
                        Log.e("audio", data.get("identifier").getAsString());
                        break;
                    }
                }

                progress_dialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                // Log.e(TAG, t.toString());
            }
        });
    }

    private void API_Quran_Language(String lang) {
        //Sura_Fragment.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progress_dialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getLanguageTranslation();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                int statusCode = response.code();


                for (int i = 0; i < response.body().get("data").getAsJsonArray().size(); i++) {
                    JsonObject data = response.body().get("data").getAsJsonArray().get(i).getAsJsonObject();
                    String lng = data.get("language").getAsString();
                    if (lang.equals(lng)) {
                        Locale loc = new Locale(lng);
                        String name = loc.getDisplayLanguage(loc);
                        System.out.println(name);
                        data.get("identifier").getAsString();
                        quran_lang_name.setText(getString(R.string.Quran_Translation) + "(" + name + ")");
                        session.setQuranLanguageIdentity(data.get("identifier").getAsString());
                        Log.e("audio", data.get("identifier").getAsString());
                        break;
                    }
                }

                progress_dialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                // Log.e(TAG, t.toString());
            }
        });
    }

    private void UpdateLang(String lng) {
        Locale locale = new Locale(lng);
        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        res.updateConfiguration(config,
                dm);

        startActivity(getIntent());
        finish();
    }

    private void ChangeLanguage() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.Language_of));
// add a list
        String[] animals = {"English", "Hindi", "Urdu", "Bangali", "Español", "Català",
                "Malay", "French", "Portuguese", "ߒߞߏ Nko", "أسماء الأمة سلاسل(Arabic)"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        UpdateLang("en");
                        break;
                    }
                    case 1: {
                        UpdateLang("hi");
                        break;
                    }
                    case 2: {
                        UpdateLang("ur");
                        break;
                    }
                    case 3: {
                        UpdateLang("bn");
                        break;
                    }

                    case 4: {
                        UpdateLang("es");
                        break;
                    }

                    case 5: {
                        UpdateLang("ca");
                        break;
                    }
                    case 6: {
                        UpdateLang("ms");
                        break;
                    }

                    case 7: {
                        UpdateLang("fr");
                        break;
                    }
                    case 8: {
                        UpdateLang("pt");
                        break;
                    }

                    case 9: {
                        UpdateLang("nqo");
                        break;
                    }
                    case 10: {
                        UpdateLang("ar");
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

    }

    private void API() {
    }

    private void Find_element() {
        quran_lang_name = findViewById(R.id.quran_lang_name);
        languageChangeBtn = findViewById(R.id.languageChangeBtn);
        QuranlanguageChangeBtn = findViewById(R.id.QuranlanguageChangeBtn);
        IV_back = (ImageView) findViewById(R.id.Iv_back);
        Button_Log_out = (Button) findViewById(R.id.Button_Log_out);
        if (!session.checkEntered()) {
            Button_Log_out.setText(R.string.login);
        }
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

            case R.id.Iv_back:
                onBackPressed();
                break;

            case R.id.Button_Log_out:
                if (session.checkEntered()) {
                    session.logoutUser();
                    Intent in = new Intent(Setting_Activity.this, Login_Activity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                } else {
                    Intent in = new Intent(Setting_Activity.this, Login_Activity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("fromProfile", false)) {

            Intent intent = new Intent(Setting_Activity.this, Profile_Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {

            Intent intent = new Intent(Setting_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
