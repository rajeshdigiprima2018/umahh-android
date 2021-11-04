package Util;


public class Validation {
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }
    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 3) {
            return true;
        }

        return false;

    }
}
