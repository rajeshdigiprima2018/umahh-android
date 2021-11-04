//package paypalintegration;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
//
//import org.json.JSONException;
//
//import java.math.BigDecimal;
//
//import app.com.ummah.R;
//
////Implementing click listener to our class
//public class PayPal_MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    //The views
//    private Button buttonPay;
//    private TextView editTextAmount;
//
//    //Payment Amount
//    private String paymentAmount;
//
//
//
//    Intent intent2 ;
//    String str ;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.paypal_activity_main);
//
//        buttonPay = (Button) findViewById(R.id.buttonPay);
//        editTextAmount = (TextView) findViewById(R.id.editTextAmount);
//        intent2 = getIntent();
//        String str = intent2.getStringExtra("Key");
//        editTextAmount.setText(str);
//
//        buttonPay.setOnClickListener(this);
//
//
//
//    }
//
//
//
//   }
