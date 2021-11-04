package jcplayersample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jean.jcplayer.JcAudio;
import com.example.jean.jcplayer.JcPlayerView;
import com.example.jean.jcplayer.JcStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Util.SessionManager;
import app.com.ummah.Quran_activity;
import app.com.ummah.R;

public class Play_MainActivity extends AppCompatActivity
        implements JcPlayerView.OnInvalidPathListener, JcPlayerView.JcPlayerViewStatusListener, View.OnClickListener {

    private static final String TAG = Play_MainActivity.class.getSimpleName();

    private JcPlayerView player;
    private RecyclerView recyclerView;
    private AudioAdapter audioAdapter;
    TextView Tv_title;
    ImageView IV_back;
    private ProgressDialog progress_dialog;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity_main);
        ProgressBar_Function();
        IV_back = findViewById(R.id.IV_back);
        Tv_title = findViewById(R.id.Tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        player = (JcPlayerView) findViewById(R.id.jcplayer);

        Bundle bundle = getIntent().getExtras();
        String EnglishNameTranslation = bundle.getString("EnglishNameTranslation");
        String NumberOfAyahs = bundle.getString("NumberOfAyahs");
        Tv_title.setText(EnglishNameTranslation);
        // API_Eng(NumberOfAyahs);
        API(NumberOfAyahs);

        IV_back.setOnClickListener(this);


/*

        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        jcAudios.add(JcAudio.createFromURL("url audio","http://cdn.alquran.cloud/media/audio/ayah/ar.abdulbasitmurattal/6231"));
        jcAudios.add(JcAudio.createFromAssets("Asset audio 1", "49.v4.mid"));
        jcAudios.add(JcAudio.createFromAssets("Asset audio 2", "56.mid"));
        jcAudios.add(JcAudio.createFromAssets("Asset audio 3", "a_34.mp3"));
        jcAudios.add(JcAudio.createFromRaw("Raw audio 1", R.raw.a_34));
        jcAudios.add(JcAudio.createFromRaw("Raw audio 2", R.raw.a_203));
        //jcAudios.add(JcAudio.createFromFilePath("File directory audio", this.getFilesDir() + "/" + "CANTO DA GRAÚNA.mp3"));
        //jcAudios.add(JcAudio.createFromAssets("I am invalid audio", "aaa.mid")); // invalid assets file
        player.initPlaylist(jcAudios);
        player.playAudio(player.getMyPlaylist().get(1));


//        jcAudios.add(JcAudio.createFromFilePath("test", this.getFilesDir() + "/" + "13.mid"));
//        jcAudios.add(JcAudio.createFromFilePath("test", this.getFilesDir() + "/" + "123123.mid")); // invalid file path
//        jcAudios.add(JcAudio.createFromAssets("49.v4.mid"));
//        jcAudios.add(JcAudio.createFromRaw(R.raw.a_203));
//        jcAudios.add(JcAudio.createFromRaw("a_34", R.raw.a_34));
//        player.initWithTitlePlaylist(jcAudios, "Awesome music");


//        jcAudios.add(JcAudio.createFromFilePath("test", this.getFilesDir() + "/" + "13.mid"));
//        jcAudios.add(JcAudio.createFromFilePath("test", this.getFilesDir() + "/" + "123123.mid")); // invalid file path
//        jcAudios.add(JcAudio.createFromAssets("49.v4.mid"));
//        jcAudios.add(JcAudio.createFromRaw(R.raw.a_203));
//        jcAudios.add(JcAudio.createFromRaw("a_34", R.raw.a_34));
//        player.initAnonPlaylist(jcAudios);

//        Adding new audios to playlist
//        player.addAudio(JcAudio.createFromURL("url audio","http://www.villopim.com.br/android/Music_01.mp3"));
//        player.addAudio(JcAudio.createFromAssets("49.v4.mid"));
//        player.addAudio(JcAudio.createFromRaw(R.raw.a_34));
//        player.addAudio(JcAudio.createFromFilePath(this.getFilesDir() + "/" + "121212.mmid"));

        player.registerInvalidPathListener(this);
        player.registerStatusListener(this);
        adapterSetup();*/
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Play_MainActivity.this, Quran_activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
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

    private void API(String numberOfAyahs) {
        SessionManager session = new SessionManager(this);
        String lng = session.getQuranLanguageIdentity();
        String lng_audio = session.getQuranLanguageAudio();
//"http://api.alquran.cloud/v1/surah/"
//                + numberOfAyahs + "/" + lng + ".abdulbasitmurattal",


        String url = "http://api.alquran.cloud/v1/surah/"
                + numberOfAyahs + "/editions/" + lng + ",ar.asad," + lng_audio;
     System.out.println(url);

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("audio list", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            //  Log.e("audio List", json.toString());
                            if (json != null) {

                                json = new JSONObject(response);
                                JSONArray data = json.optJSONArray("data");

                                JSONArray jsonMainNode = data.getJSONObject(0).optJSONArray("ayahs");
                                JSONArray jsonMainNode2 = data.getJSONObject(1).optJSONArray("ayahs");
                                JSONArray jsonMainNode3 = data.getJSONObject(2).optJSONArray("ayahs");
                                int lengthJsonArr = jsonMainNode.length();
                                for (int i = 1; i < lengthJsonArr; i++) {

                                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                    JSONObject jsonChildNode2 = jsonMainNode2.getJSONObject(i);
                                    JSONObject jsonChildNode3 = jsonMainNode3.getJSONObject(i);
                                    long id = jsonChildNode.getLong("numberInSurah");
                                    String title = jsonChildNode.getString("text");
                                    String title2 = jsonChildNode2.getString("text");
                                    String audio = "";
                                    if (jsonChildNode3.has("audio")) {
                                        audio = jsonChildNode3.getString("audio");
                                    }
                                    jcAudios.add(JcAudio.createFromURL(title + "\n\n" + title2, audio));
                                }
                                try {
                                    player.initPlaylist(jcAudios);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            player.registerInvalidPathListener(Play_MainActivity.this);
                                            player.registerStatusListener(Play_MainActivity.this);


                                            if (player.getMyPlaylist().get(0).getPath().length() > 0) {
                                                player.playAudio(player.getMyPlaylist().get(0));
                                            } else {
                                                player.setEnabled(false);
                                                Toast.makeText(Play_MainActivity.this, "Audio is not available", Toast.LENGTH_SHORT).show();
                                            }
                                            adapterSetup();
                                        }
                                    }, 1000);



                                   } catch (Exception ee) {
                                    ee.printStackTrace();
                                    player.setEnabled(false);
                                    Toast.makeText(Play_MainActivity.this, "Audio is not available", Toast.LENGTH_SHORT).show();
                                }
                                adapterSetup();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                return map;
            }
        };

        Log.e("url", stringRequest.getUrl().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(Play_MainActivity.this);
        requestQueue.add(stringRequest);
    }

//    private void API_Eng(String numberOfAyahs) {
//
//        showProgressDialog();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.alquran.cloud/v1/surah/" + numberOfAyahs + "/en.asad",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("text list", response);
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            //  Log.e("audio List", json.toString());
//                            if (json != null) {
//
//                                json = new JSONObject(response);
//                                JSONObject data = json.optJSONObject("data");
//                                JSONArray jsonMainNode = data.optJSONArray("ayahs");
//                                int lengthJsonArr = jsonMainNode.length();
//                                for (int i = 1; i < lengthJsonArr; i++) {
//                                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
//                                    long id = jsonChildNode.getLong("numberInSurah");
//                                    String title = jsonChildNode.getString("text");
//                                    // jcAudios.add(JcAudio.createFromURL(title,audio));
//                                    JcAudio jcAudio = new JcAudio(title);
//                                    jcAudio.setENG_Title(title);
//                                    jcAudio.setTitle(title);
//                                    jcAudios.add(jcAudio);
//                                }
////                                player.initPlaylist(jcAudios);
////                                player.playAudio(player.getMyPlaylist().get(0));
////                                player.registerInvalidPathListener(Play_MainActivity.this);
////                                player.registerStatusListener(Play_MainActivity.this);
////                                adapterSetup();
//                                hideProgressDialog();
//
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(Play_MainActivity.this);
//        requestQueue.add(stringRequest);
//    }


    protected void adapterSetup() {
        audioAdapter = new AudioAdapter(player.getMyPlaylist());
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (player.getMyPlaylist().get(position).getPath().length() > 0) {
                    player.playAudio(player.getMyPlaylist().get(position));
                } else {
                    Toast.makeText(Play_MainActivity.this, "Audio is not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSongItemDeleteClicked(int position) {
                Toast.makeText(Play_MainActivity.this, "Delete song at position " + position,
                        Toast.LENGTH_SHORT).show();
//                if(player.getCurrentPlayedAudio() != null) {
//                    Toast.makeText(Play_MainActivity.this, "Current audio = " + player.getCurrentPlayedAudio().getPath(),
//                            Toast.LENGTH_SHORT).show();
//                }
                removeItem(position);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(audioAdapter);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    @Override
    public void onPause() {
        super.onPause();
        player.createNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.kill();
    }

    @Override
    public void onPathError(JcAudio jcAudio) {
        Toast.makeText(this, jcAudio.getPath() + " with problems", Toast.LENGTH_LONG).show();
//        player.removeAudio(jcAudio);
//        player.next();
    }

    private void removeItem(int position) {
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);

        //        jcAudios.remove(position);
        player.removeAudio(player.getMyPlaylist().get(position));
        audioAdapter.notifyItemRemoved(position);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void onPausedStatus(JcStatus jcStatus) {

    }

    @Override
    public void onContinueAudioStatus(JcStatus jcStatus) {

    }

    @Override
    public void onPlayingStatus(JcStatus jcStatus) {

    }

    @Override
    public void onTimeChangedStatus(JcStatus jcStatus) {
        updateProgress(jcStatus);
    }

    @Override
    public void onCompletedAudioStatus(JcStatus jcStatus) {
        updateProgress(jcStatus);
    }

    @Override
    public void onPreparedAudioStatus(JcStatus jcStatus) {
    }

    private void updateProgress(final JcStatus jcStatus) {
        Log.d(TAG, "Song id = " + jcStatus.getJcAudio().getId() + ", song duration = " + jcStatus.getDuration()
                + "\n song position = " + jcStatus.getCurrentPosition());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // calculate progress
                float progress = (float) (jcStatus.getDuration() - jcStatus.getCurrentPosition())
                        / (float) jcStatus.getDuration();
                progress = 1.0f - progress;
                audioAdapter.updateProgress(jcStatus.getJcAudio(), progress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.IV_back:
                onBackPressed();
                break;
        }
    }
}