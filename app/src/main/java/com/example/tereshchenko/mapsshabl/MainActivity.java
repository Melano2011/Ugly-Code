package com.example.tereshchenko.mapsshabl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button2;
    Button button4;
    Button button;
    Button button3;
    private void showInfo(){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Информация");
        helpBuilder.setMessage("This app is designed to help you find the best place for taking care of your pet :)");
        helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }
    private void showPet(){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Домашние Животные");
        helpBuilder.setMessage("Скоро!");
        helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }
    private void showBoard(){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Доска Объявлений");
        helpBuilder.setMessage("Скоро!");
        helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button2:
                Intent intent = new Intent(this,MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.button4:
                showInfo();
                break;
            case R.id.button:
                showPet();
                break;
            case R.id.button3:
                showBoard();
                break;
            default:
                    break;
        }
    }

}
