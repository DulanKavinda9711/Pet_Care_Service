package com.example.happytailhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    EditText enterHours, petType;
    Button calcButton, backButton;
    TextView paymentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        enterHours = findViewById(R.id.enterHours);
        petType = findViewById(R.id.petType);
        calcButton = findViewById(R.id.calcButton);
        paymentView = findViewById(R.id.feesView);
        backButton = findViewById(R.id.backButton);

        calcButton.setOnClickListener(v -> calculatePayment());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, CustomerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void calculatePayment() {
        String petTypeText = petType.getText().toString();
        int hours = Integer.parseInt(enterHours.getText().toString());

        int amountPerHour = 0;
        
        if ("dog".equalsIgnoreCase(petTypeText)) {
            amountPerHour = 5;
        } else if ("cat".equalsIgnoreCase(petTypeText)) {
            amountPerHour = 3;
        }

        // Calculate the total payment
        int totalPayment = hours * amountPerHour;

        // Display the result in paymentView
        paymentView.setText("Total Payment: $" + totalPayment);
    }
}