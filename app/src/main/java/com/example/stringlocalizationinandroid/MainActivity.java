package com.example.stringlocalizationinandroid;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvCustomerName, tvStock, tvPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvStock = (TextView) findViewById(R.id.tvStock);
        tvPrice = (TextView) findViewById(R.id.tvPrice);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("MainActivity");

        String name = String.format(getResources().getString(R.string.customer_name), "Jon Doe");
        tvCustomerName.setText(name);

        String stock = String.format(getResources().getString(R.string.stock), 405); //For longs, use 405L
        tvStock.setText(stock);

        String price = String.format(getResources().getString(R.string.price), 999.99f); //For doubles, use 999.99
        tvPrice.setText(price);
    }
}
