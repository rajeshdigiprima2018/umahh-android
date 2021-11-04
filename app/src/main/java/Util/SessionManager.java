package Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by appson on 4/23/2016.
 */
public class SessionManager {


    static SharedPreferences pref;
    public static String id_;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys

    public static final String KEY_SUBSCRIBE = "subs";
    public static final String KEY_EXPIRY_DATE = "expiry date";
    public static final String KEY_INTENT_ONE = "in";
    public static final String SPPINER_ID = "S_id";
    public static final String SPPINER_TIME_ID = "t_id";
    public static final String SPINNER_UNITS_ID = "u_id";
    public static final String DAY_NAME_ID = "day_name_id";
    public static final String QURAN_LANG = "quran_lang";
    public static final String QURAN_LANG_IDENTIFY = "quran_lang_identify";
    public static final String QURAN_AUDIO_IDENTIFY = "quran_lang_audio";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USER_STATUS = "user_status";
    public static final int USERID = 0;
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String IMEI_NO = "imi_no";
    public static final String IMEI_ID = "imi_id";
    public static final String CITY_ID = "id";
    public static final String CITY_NAME = "city_name";
    private static final String CITY_ICON = "city_icon";
    public static final String DISTANCE = "distance";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }


    public void setIsEntered() {
        editor.putBoolean(KEY_LOGIN, true);
        editor.commit();
    }

    public void setSubscribe() {
        editor.putBoolean(KEY_SUBSCRIBE, true);
        editor.commit();
    }

    public void setSpinerid(String id) {
        editor.putString(SPPINER_ID, id);
        editor.commit();
    }

    public void setSppinerTimeId(String id) {

        editor.putString(SPPINER_TIME_ID, id);
        editor.commit();
    }

    public void setSpinnerUnitsId(String id) {

        editor.putString(SPINNER_UNITS_ID, id);
        editor.commit();

    }

    public void setDayNameId(String id) {

        editor.putString(DAY_NAME_ID, id);
        editor.commit();
    }

    public void setExpiryDate(String exp_date) {
        editor.putString(KEY_EXPIRY_DATE, exp_date);
        editor.commit();
    }

    public void setIntentValueOne(String in) {
        editor.putString(KEY_INTENT_ONE, in);
        editor.commit();
    }
    public void setQuranLanguage(String in) {
        editor.putString(QURAN_LANG, in);
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public String getQuranLanguage() {
        return pref.getString(QURAN_LANG, "en");

    }
    public void setQuranLanguageIdentity(String in) {
        editor.putString(QURAN_LANG_IDENTIFY, in);
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public String getQuranLanguageIdentity() {
        return pref.getString(QURAN_LANG_IDENTIFY, "ar.asad");

    }

    public void setQuranLanguageAudio(String in) {
        editor.putString(QURAN_AUDIO_IDENTIFY, in);
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public String getQuranLanguageAudio() {
        return pref.getString(QURAN_AUDIO_IDENTIFY, "ar.alafasy");

    }
    public HashMap<String, String> getSpinerid() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(SPPINER_ID, pref.getString(SPPINER_ID, null));
        return user;
    }

    public HashMap<String, String> getSppinerTimeId() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(SPPINER_TIME_ID, pref.getString(SPPINER_TIME_ID, null));
        return user;
    }

    public HashMap<String, String> getSpinnerUnitsId() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(SPINNER_UNITS_ID, pref.getString(SPINNER_UNITS_ID, null));
        return user;
    }

    public HashMap<String, String> getDayNameId() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(DAY_NAME_ID, pref.getString(DAY_NAME_ID, null));
        return user;
    }

    public HashMap<String, String> getExpiryDate() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EXPIRY_DATE, pref.getString(KEY_EXPIRY_DATE, null));
        return user;
    }


    public HashMap<String, String> getIntentValueOne() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_INTENT_ONE, pref.getString(KEY_INTENT_ONE, null));
        return user;
    }


    public boolean checkSubscribe() {
        return pref.getBoolean(KEY_SUBSCRIBE, false);
    }






  /*  public void checkLogin() {
        // Check login statususer
        if (!this.checkEntered()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, OptionMenu.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
    }*/


    public HashMap<String, String> getUserProfile() {
        HashMap<String, String> UserProfile = new HashMap<String, String>();
        // user name
        UserProfile.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        UserProfile.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        UserProfile.put(KEY_USER_STATUS, pref.getString(KEY_USER_STATUS, null));


        return UserProfile;
    }


    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity

    }

    public boolean checkEntered() {
        return pref.getBoolean(KEY_LOGIN, false);
    }


    public static HashMap<String, Integer> getuserId() {
        HashMap<String, Integer> user = new HashMap<String, Integer>();
        user.put(Constant.SHARED_PREFERENCE_ID_KEY, pref.getInt(Constant.SHARED_PREFERENCE_ID_KEY, 0));
        return user;
    }

    public HashMap<String, String> getMobile_no() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constant.SHARED_PREFERENCE_Phone_KEY, pref.getString(Constant.SHARED_PREFERENCE_Phone_KEY, null));
        return user;
    }

    public HashMap<String, String> getRole_id() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constant.SHARED_PREFERENCE_Role_KEY, pref.getString(Constant.SHARED_PREFERENCE_Role_KEY, null));
        return user;
    }


    public void setToken_id(String token_id) {
        editor.putString(Constant.SHARED_PREFERENCE_TOKEN_KEY, token_id);
        editor.commit();
    }

    public HashMap<String, String> getToken_id() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constant.SHARED_PREFERENCE_TOKEN_KEY, pref.getString(Constant.SHARED_PREFERENCE_TOKEN_KEY, null));
        return user;
    }


    public void SetProfile(String imageurl) {
        editor.putString(Constant.SHARED_PREFERENCE_Profile_pic_KEY, imageurl);
        editor.commit();
    }

    public HashMap<String, String> getProfile() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constant.SHARED_PREFERENCE_Profile_pic_KEY, pref.getString(Constant.SHARED_PREFERENCE_Profile_pic_KEY, null));
        return user;
    }

    public void putid(String id) {
        editor.putString(id_, id);
        editor.commit();
    }

    public static HashMap<String, String> getid() {
        HashMap<String, String> userinfo = new HashMap<String, String>();
        userinfo.put(id_, pref.getString(id_, null));
        return userinfo;
    }

    public void setMobile_status(String token_id) {
        editor.putString(Constant.SHARED_PREFERENCE_Mobile_status_KEY, token_id);
        editor.commit();
    }

    public HashMap<String, String> getMobile_status() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constant.SHARED_PREFERENCE_Mobile_status_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mobile_status_KEY, null));
        return user;
    }

    public void setMobile(String token_id) {
        editor.putString(Constant.SHARED_PREFERENCE_Phone_KEY, token_id);
        editor.commit();
    }

    public HashMap<String, String> getMobile() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constant.SHARED_PREFERENCE_Phone_KEY, pref.getString(Constant.SHARED_PREFERENCE_Phone_KEY, null));
        return user;
    }

    public void setUserDetail_2(String username, String mosque_id, String mobile, String isLoggedIn,
                                String nameContactPerson, String isFBUser, String email, String street_address, String country, String state, String city, String zipCode, String vefication, String phoneVerification, String deletedAt, String updatedAt, String randomToken, String role, String _id, String user_id, String id) {
        editor.putString(Constant.SHARED_PREFERENCE_NAME_KEY, username);
        editor.putString(Constant.SHARED_PREFERENCE_isLoggedIn_KEY, isLoggedIn);
        editor.putString(Constant.SHARED_PREFERENCE_nameContactPerson_KEY, nameContactPerson);
        editor.putString(Constant.SHARED_PREFERENCE_isFBUser_KEY, isFBUser);
        editor.putString(Constant.SHARED_PREFERENCE__id_KEY, _id);
        editor.putString(Constant.SHARED_PREFERENCE_USER_ID_KEY, user_id);
        editor.putString(Constant.SHARED_PREFERENCE_ID_KEY, id);
        editor.putString(Constant.SHARED_PREFERENCE_zipCode_KEY, zipCode);
        editor.putString(Constant.SHARED_PREFERENCE_vefication_KEY, vefication);
        editor.putString(Constant.SHARED_PREFERENCE_randomToken_KEY, randomToken);
        editor.putString(Constant.SHARED_PREFERENCE_state_KEY, state);
        editor.putString(Constant.SHARED_PREFERENCE_city_KEY, city);
        editor.putString(Constant.SHARED_PREFERENCE_phoneVerification_KEY, phoneVerification);
        editor.putString(Constant.SHARED_PREFERENCE_deleted_at_KEY, deletedAt);
        editor.putString(Constant.SHARED_PREFERENCE_updated_at_KEY, updatedAt);
        editor.putString(Constant.SHARED_PREFERENCE_street_address_KEY, street_address);
        editor.putString(Constant.SHARED_PREFERENCE_country_KEY, country);
        editor.putString(Constant.SHARED_PREFERENCE_Phone_KEY, mobile);
        editor.putString(Constant.SHARED_PREFERENCE_Role_KEY, role);
        editor.putString(Constant.SHARED_PREFERENCE_EMAIL_KEY, email);
        editor.putString(Constant.SHARED_PREFERENCE_mosque_id_KEY, mosque_id);

        editor.commit();

    }

    public HashMap<String, String> getuserinfo() {
        HashMap<String, String> userinfo = new HashMap<String, String>();
        // user name
      //  userinfo.put(Constant.SHARED_PREFERENCE_USER_ID_KEY, String.valueOf(pref.getInt(Constant.SHARED_PREFERENCE_USER_ID_KEY, 0)));

        userinfo.put(Constant.SHARED_PREFERENCE_NAME_KEY, pref.getString(Constant.SHARED_PREFERENCE_NAME_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_isLoggedIn_KEY, pref.getString(Constant.SHARED_PREFERENCE_isLoggedIn_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_nameContactPerson_KEY, pref.getString(Constant.SHARED_PREFERENCE_nameContactPerson_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_isFBUser_KEY, pref.getString(Constant.SHARED_PREFERENCE_isFBUser_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE__id_KEY, pref.getString(Constant.SHARED_PREFERENCE__id_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_USER_ID_KEY, pref.getString(Constant.SHARED_PREFERENCE_USER_ID_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_ID_KEY, pref.getString(Constant.SHARED_PREFERENCE_ID_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_zipCode_KEY, pref.getString(Constant.SHARED_PREFERENCE_zipCode_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_vefication_KEY, pref.getString(Constant.SHARED_PREFERENCE_vefication_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_randomToken_KEY, pref.getString(Constant.SHARED_PREFERENCE_randomToken_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_state_KEY, pref.getString(Constant.SHARED_PREFERENCE_state_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_city_KEY, pref.getString(Constant.SHARED_PREFERENCE_city_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_phoneVerification_KEY, pref.getString(Constant.SHARED_PREFERENCE_phoneVerification_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_deleted_at_KEY, pref.getString(Constant.SHARED_PREFERENCE_deleted_at_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_updated_at_KEY, pref.getString(Constant.SHARED_PREFERENCE_updated_at_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_street_address_KEY, pref.getString(Constant.SHARED_PREFERENCE_street_address_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_country_KEY, pref.getString(Constant.SHARED_PREFERENCE_country_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_Phone_KEY, pref.getString(Constant.SHARED_PREFERENCE_Phone_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_Role_KEY, pref.getString(Constant.SHARED_PREFERENCE_Role_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_EMAIL_KEY, pref.getString(Constant.SHARED_PREFERENCE_EMAIL_KEY, null));
        userinfo.put(Constant.SHARED_PREFERENCE_mosque_id_KEY, pref.getString(Constant.SHARED_PREFERENCE_mosque_id_KEY, null));
        return userinfo;

    }

   public void SetSelectMosque_Detail(String description_service, String _id, String username, String role, String email, String zipCode, String street_address, String lng, String lat, String avtar, String isSelect, String nameContactPerson, String mobile, String city_name, String state_name, String country_name){

       editor.putString(Constant.SHARED_PREFERENCE_Mosque_id_KEY, _id);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_name_KEY, username);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_city_KEY, city_name);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_state_KEY, state_name);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_country_KEY, country_name);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_email_KEY, email);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_nameContactPerson_KEY, nameContactPerson);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_lng_KEY, lng);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_lat_KEY, lat);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_mobile_KEY, mobile);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY, avtar);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_role_KEY, role);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_zipCode_KEY, zipCode);
       editor.putString(Constant.SHARED_PREFERENCE_Mosque_street_address_KEY, street_address);
       editor.putString(Constant.SHARED_PREFERENCE_description_service_KEY, description_service);
       editor.commit();



    }

    public HashMap<String, String> getMosqueinfo() {
        HashMap<String, String> mosqueinfo = new HashMap<String, String>();

        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_id_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_id_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_name_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_name_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_city_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_city_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_state_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_state_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_country_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_country_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_email_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_email_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_nameContactPerson_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_nameContactPerson_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_lng_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_lng_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_lat_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_lat_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_mobile_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_mobile_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_role_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_role_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_zipCode_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_zipCode_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_Mosque_street_address_KEY, pref.getString(Constant.SHARED_PREFERENCE_Mosque_street_address_KEY, null));
        mosqueinfo.put(Constant.SHARED_PREFERENCE_description_service_KEY, pref.getString(Constant.SHARED_PREFERENCE_description_service_KEY, null));

        return mosqueinfo;
    }

    public void setFollow_status(String following) {
        editor.putString(Constant.SHARED_PREFERENCE_Follow_status_KEY, following);
        editor.commit();
    }

    public HashMap<String, String> getFollow_status() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constant.SHARED_PREFERENCE_Follow_status_KEY, pref.getString(Constant.SHARED_PREFERENCE_Follow_status_KEY, null));
        return user;
    }


}