package com.example.prototype_govet;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button payments;
    EditText amounts;

    public static final String clientKey = "AfxOCHDgJp3kx4RSeJ1Rcr_3Fn3qnhebda3ZXICjOMXgwg_J9W1QktmaqtI00aUYBgJJMiWqo_WI-6ay";
    public static final int PAYPAL_REQUEST_CODE = 123;

   // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
           .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
           .clientId(clientKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payments = findViewById(R.id.payments);
        amounts = findViewById(R.id.amounts);

        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPayment();

            }
        });
    }

    private void getPayment() {

        String amount = amounts.getText().toString();

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "PHP", "Learn", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);

        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PAYPAL_REQUEST_CODE){
            PaymentConfirmation configuration = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if (configuration!=null){
                try {
                    String paymentDetails = configuration.toJSONObject().toString(4);

                    JSONObject payObj = new JSONObject(paymentDetails);
                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("Error", "Something went wrong");
                }
            }
        }
        else if (requestCode==Activity.RESULT_CANCELED){
            Log.i("Error", "Something went wrong");
        }
        else if (requestCode==PaymentActivity.RESULT_EXTRAS_INVALID){
            Log.i("Payment", "Invalid Payment");
        }
    }




}