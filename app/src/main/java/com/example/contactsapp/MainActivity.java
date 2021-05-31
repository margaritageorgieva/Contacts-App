package com.example.contactsapp;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends DBActivity {

    protected EditText editName, editPhoneNumber, editEmail, editCategory, editDescription;
    protected Button btnInsert;
    protected ListView smpList;

    protected  void FillListView() throws  Exception{

        final ArrayList<String> listResults = new ArrayList<String>();
        SelectSQL("SELECT * FROM CONTACTS ORDER BY ID",
                null,
                new OnSelectSuccess() {
                    @Override
                    public void OnElementSelected(String ID, String Name, String Tel, String Email, String Category, String Description) {
                        listResults.add(ID+" "+Name+" "+ Tel+" "+ Email+" "+ Category+" "+Description+"\n");
                    }
                }
        );
        smpList.clearChoices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_listview,
                R.id.textView,
                listResults
        );
        smpList.setAdapter(arrayAdapter);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName=findViewById(R.id.editName);
        editPhoneNumber=findViewById(R.id.editPhoneNumber);
        editEmail=findViewById(R.id.editEmail);
        editCategory=findViewById(R.id.editCategory);
        editDescription=findViewById(R.id.editDescription);
        btnInsert=findViewById(R.id.btnInsert);
        smpList=findViewById(R.id.smpList);

        smpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView)(view.findViewById(R.id.textView)))
                        .getText().toString();
                String[] elements=selected.split(" ");

                Intent intent = new Intent(
                        MainActivity.this,
                        UpdateAndDelete.class
                );
                Bundle b = new Bundle();
                b.putString("ID",elements[0]);
                b.putString("Name",elements[1]);
                b.putString("Tel",elements[2]);
                b.putString("Email",elements[3]);
                b.putString("Category",elements[4]);
                b.putString("Description",elements[5].trim());

                intent.putExtras(b);
                startActivityForResult(intent,200,b);
            }
        });

        initDB();
        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnInsert.setOnClickListener((view)->{
            ExecSQL("INSERT INTO CONTACTS(Name, Tel, Email, Category, Description)"+
                            "VALUES(?, ?, ?, ?, ?)",
                    new Object[]{
                            editName.getText().toString(),
                            editPhoneNumber.getText().toString(),
                            editEmail.getText().toString(),
                            editCategory.getText().toString(),
                            editDescription.getText().toString()
                    },
                    ()-> {
                        Toast.makeText(this,"Insert Successful!",Toast.LENGTH_LONG).show();
                    },
                    (error)-> {
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    }
            );

            try {
                FillListView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}