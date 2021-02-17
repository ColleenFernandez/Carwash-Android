package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wagen.cl.R;

public class MercagoActivity extends AppCompatActivity {
    Button mercagopay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercago);

        mercagopay=(Button)findViewById(R.id.mercagopay);
        mercagopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMercadoPagoCheckout("677318256-c86aa0cf-d86e-4e27-b9c3-7d47973938e8");
            }
        });
    }

    private static final int REQUEST_CODE = 1;

    private void startMercadoPagoCheckout( String checkoutPreferenceId) {
      /*  new MercadoPagoCheckout.Builder("TEST-9316000c-da0c-417d-93d6-c771452316d1", checkoutPreferenceId).build()
                .startPayment(MercagoActivity.this, REQUEST_CODE);*/
    }

    /*@Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                ((TextView) findViewById(R.id.mp_results)).setText("Resultado del pago: " + payment.getPaymentStatus());
                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getExtras() != null
                        && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                    ((TextView) findViewById(R.id.mp_results)).setText("Error: " + mercadoPagoError.getMessage());
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }*/

}