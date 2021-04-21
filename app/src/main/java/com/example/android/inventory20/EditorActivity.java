package com.example.android.inventory20;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditorActivity extends AppCompatActivity {

    private EditText mName;

    private EditText mQuantity;

    private EditText mPrice;

    private int quantity = 100;

    private int price = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mName = (EditText)findViewById(R.id.edit_item_name);
        //mQuantity = findViewById(R.id.quantity);

    }

    public void quantityPlus(View v){
        int increment;
        mQuantity = (EditText) findViewById(R.id.increase_by_quantity);
        String incrementString = "";
        incrementString  = mQuantity.getText().toString();

        if(!incrementString.equals("")) {
            increment = Integer.parseInt(incrementString);
        }
        else{
            increment = 10;
        }
        quantity += increment;
        displayQuantity(quantity);
    }

    public void quantityMinus(View v){
        int decrement;
        mQuantity = (EditText) findViewById(R.id.increase_by_quantity);
        String decrementString = "";
        decrementString  = mQuantity.getText().toString();

        if(!decrementString.equals("")) {
            decrement = Integer.parseInt(decrementString);
        }
        else{
            decrement = 10;
        }

        if(decrement<quantity){
            quantity -= decrement;
        }
        displayQuantity(quantity);
    }

    public void pricePlus(View v){
        int increment;
        mPrice = (EditText) findViewById(R.id.increase_by_price);
        String incrementString = "";
        incrementString  = mPrice.getText().toString();

        if(!incrementString.equals("")) {
            increment = Integer.parseInt(incrementString);
        }
        else{
            increment = 10;
        }
        price += increment;
        displayPrice(price);
    }

    public void priceMinus(View v){
        int decrement;
        mPrice = (EditText) findViewById(R.id.increase_by_price);
        String decrementString = "";
        decrementString  = mPrice.getText().toString();

        if(!decrementString.equals("")) {
            decrement = Integer.parseInt(decrementString);
        }
        else{
            decrement = 10;
        }
        if(decrement<price){
            price -= decrement;
        }
        displayPrice(price);
    }

    public void displayQuantity(int quantity){
        TextView quantityView = findViewById(R.id.number_display);
        quantityView.setText(String.valueOf(quantity));
    }

    public void displayPrice(int price){
        TextView quantityView = findViewById(R.id.price_display);
        quantityView.setText(String.valueOf(price));
    }
}
