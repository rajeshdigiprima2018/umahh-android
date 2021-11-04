package Util;

/**
 * Created by Abhi on 9/12/2018.
 */

public class Constant {

    public static String KEY_Business_category_id = "";
    public static String KEY_Business_name = "";




    public static String Map_type = "Normal";
    public static final String SHARED_PREF = "ah_firebase";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final int NETWORK_FAILED = 1;
    public static final int PLEASE_FILL_REQUIRED_DETAIL = 2;
    public static final int APP_EXIT = 3;
    public static final int SERVER_MESSAGE = 4;

    public static String imageurl = "Imageurl";

    public static String tv_Count = "tv_Count";
    public static final String USERID = "id";
    public static final String DO_IN_BACKGROUND_EXCEPTION = "exception";
    public static final int Login_api_id = 2;
    public static final int Registration_api_id = 12;
    public static final int Verification_api_id = 3;
    public static final int Resend_api_id = 4;
    public static final String MSG_INTERNET_CONNECTION_FAILURE = "Please check your internet connection.";

    public static final String JSON_APP_ID_TAG = "ApiId";
    public static final String JSON_ROLE_TAG = "Role";
    public static final String JSON_NAME_TAG = "Name";
    public static final String JSON_DATA_TAG = "Data";
    public static final String JSON_PASSWORD_TAG = "Password";
    public static final String JSON_TOKEN_TAG = "Token";
    public static final String JSON_MOBILESTATUS_TAG = "MobileStatus";
    public static final String JSON_MOBILE_NO_TAG = "MobileNumber";
    public static final String JSON_VERYFICATION_CODE_TAG = "VerificationCode";
    public static final String SHARED_PREFERENCE_RESTURENT_ID_KEY = "RestaurantId";


    ////////////////////////////////////////////////////////////


    public static final String SHARED_PREFERENCE_isLoggedIn_KEY = "isLoggedIn";
    public static final String SHARED_PREFERENCE_nameContactPerson_KEY = "nameContactPerson";
    public static final String SHARED_PREFERENCE_isFBUser_KEY = "isFBUser";
    public static final String SHARED_PREFERENCE__id_KEY = "_id";

    public static final String SHARED_PREFERENCE_USER_ID_KEY = "UserId";
    public static final String SHARED_PREFERENCE_ID_KEY = "Id";
    public static final String SHARED_PREFERENCE_NAME_KEY = "Name";
    public static final String SHARED_PREFERENCE_Role_KEY = "Role_id";
    public static final String SHARED_PREFERENCE_TOKEN_KEY = "Token";
    public static final String SHARED_PREFERENCE_EMAIL_KEY = "Email";
    public static final String SHARED_PREFERENCE_Phone_KEY= "Phone";
    public static final String SHARED_PREFERENCE_Driving_licence_no_KEY= "Driving_licence_no";
    public static final String SHARED_PREFERENCE_EventId_KEY= "EventId";
    public static final String SHARED_PREFERENCE_Cab_no_KEY= "cab_no";
    public static final String SHARED_PREFERENCE_deleted_at_KEY = "Delete_at";
    public static final String SHARED_PREFERENCE_created_at_KEY = "Created_at";
    public static final String SHARED_PREFERENCE_updated_at_KEY = "Updated_at";
    public static final String SHARED_PREFERENCE_street_address_KEY = "Event_title";
    public static final String SHARED_PREFERENCE_country_KEY = "Start_date";
    public static final String SHARED_PREFERENCE_state_KEY = "End_date";
    public static final String SHARED_PREFERENCE_city_KEY = "Address";
    public static final String SHARED_PREFERENCE_latitude_KEY ="latitude";
    public static final String SHARED_PREFERENCE_longitude_KEY = "longitude";
    public static final String SHARED_PREFERENCE_phoneVerification_KEY = "Status";
    public static final String SHARED_PREFERENCE_cab_title_KEY = "Cab_title";
    public static  double SHARED_PREFERENCE_latitude_KEY_ ;
    public static  double SHARED_PREFERENCE_longitude_KEY_ ;
    public static final  String SHARED_PREFERENCE_zipCode_KEY = "Cab_Id";
    public static final  String SHARED_PREFERENCE_vefication_KEY ="referral_by";
    public static final  String SHARED_PREFERENCE_randomToken_KEY = "referral_code";
    public static final  String SHARED_PREFERENCE_Profile_pic_KEY="";

    public static final  String SHARED_PREFERENCE_Email_otp_KEY = "Email_otp";
    public static final  String SHARED_PREFERENCE_Mobile_otp_KEY= "Referral_code";
    public static final  String SHARED_PREFERENCE_Email_status_KEY=  "Email_status";
    public static final  String SHARED_PREFERENCE_Mobile_status_KEY = "Mobile_status";
    public static final  String SHARED_PREFERENCE_mosque_id_KEY = "mosque_id";
    public static final  String SHARED_PREFERENCE_Follow_status_KEY = "Unfollow";

    public static final String SHARED_PREFERENCE_Mosque_id_KEY = "Mosque_id";
    public static final String SHARED_PREFERENCE_Mosque_name_KEY = "Mosque_name";
    public static final String SHARED_PREFERENCE_Mosque_state_KEY = "Mosque_state";
    public static final String SHARED_PREFERENCE_Mosque_city_KEY = "Mosque_city";
    public static final String SHARED_PREFERENCE_Mosque_country_KEY = "Mosque_country";
    public static final String SHARED_PREFERENCE_Mosque_email_KEY = "Mosque_email";
    public static final String SHARED_PREFERENCE_Mosque_nameContactPerson_KEY = "Mosque_nameContactPerson";
    public static final String SHARED_PREFERENCE_Mosque_lng_KEY = "Mosque_lng";
    public static final String SHARED_PREFERENCE_Mosque_lat_KEY = "Mosque_lat";
    public static final String SHARED_PREFERENCE_Mosque_mobile_KEY = "Mosque_mobile";
    public static final String SHARED_PREFERENCE_Mosque_avtar_KEY = "Mosque_avtar";
    public static final String SHARED_PREFERENCE_Mosque_role_KEY = "role";
    public static final String SHARED_PREFERENCE_Mosque_zipCode_KEY = "zipCode";
    public static final String SHARED_PREFERENCE_Mosque_street_address_KEY = "street_address";
    public static final String SHARED_PREFERENCE_description_service_KEY = "description_service";


    public static final  String SHARED_PREFERENCE_School_KEY= "school";
    public static final String id_ = "loction_id";
    public static String longitude = "0.0";
    public static String latitude = "0.0";

    public static final String Base_url = "http://167.172.131.53:4002/api/";
    public static final String Quran_Base_url = "http://api.alquran.cloud/v1/";
    public static final String Image_Base_url = "http://167.172.131.53:4002";
    public static final String Register_url = Base_url + "user/register";
    public static final String Login_url = Base_url + "user/login";
    public static final String cabList_url = Base_url + "cabList?";
    public static final String eventLocation_url = Base_url + "eventLocation?";
    public static final String eventUpdate= Base_url+ "eventUpdate?";
    public static final String ForgetPassword_url= Base_url+ "apiResetPassword";
    public static final String PasswordUpdate_url= Base_url+ "user/password/reset";
    public static final String Resend_OTP_url = Base_url + "resend_otp";
    public static final String Check_OtpUpdate_url= Base_url + "checkOpUpdate";
   // public static final String Refferal_url = Base_url + "index.php/refferal/";
    public static final String Refferal_url = "https://pathtrack.app/refferal/";

    public static final String MyQuran_List_Url= Quran_Base_url+"quran/en.asad";
    public static final String Mosque_nearby = Base_url+ "mosque/nearby/";
    public static final String Add_Profile_Pic = Base_url+ "user/add/photo";




}
