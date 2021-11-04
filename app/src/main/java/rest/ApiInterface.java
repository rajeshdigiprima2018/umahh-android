package rest;

import com.google.gson.JsonObject;

import Modal.Juz;
import Modal.Surah;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("surah")
    Call<Surah> getSura();

    @GET("juz/30/en.asad")
    Call<Juz> getJuz();

    @GET("edition/language")
    Call<JsonObject> getLanguage();

    @GET("edition/type/translation")
    Call<JsonObject> getLanguageTranslation();

    @GET("edition/format/audio")
    Call<JsonObject> getLanguageTranslationAudio();

   /* @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);*/
}
