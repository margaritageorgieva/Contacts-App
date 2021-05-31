package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateAndDelete extends DBActivity {

    protected EditText editName, editPhoneNumber, editEmail, editCategory, editDescription;
    protected Button btnUpdate, btnDelete;
    protected String ID;

    private  void BackToMain(){
        finishActivity(200);
        Intent i = new Intent(UpdateAndDelete.this,
                MainActivity.class
        );
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_delete);

        editName=findViewById(R.id.editName);
        editPhoneNumber=findViewById(R.id.editPhoneNumber);
        editEmail=findViewById(R.id.editEmail);
        editCategory=findViewById(R.id.editCategory);
        editDescription=findViewById(R.id.editDescription);

        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);

        Bundle b = getIntent().getExtras();

        if(b!= null){
            ID = b.getString("ID");
            editName.setText(b.getString("Name"));
            editPhoneNumber.setText(b.getString("Tel"));
            editEmail.setText(b.getString("Email"));
            editCategory.setText(b.getString("Category"));
            editDescription.setText(b.getString("Description"));
        }

        btnDelete.setOnClickListener(view->{
            ExecSQL("DELETE FROM CONTACTS WHERE ID = ?",
                    new Object[]{ID},
                    new OnQuerySuccess() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(UpdateAndDelete.this,
                                    "Delete successful!",
                                    Toast.LENGTH_LONG).show();
                            BackToMain();
                        }
                    },
                    new OnError() {
                        @Override
                        public void OnQueryError(String error) {

                            Toast.makeText(UpdateAndDelete.this,
                                    error,Toast.LENGTH_LONG).show();
                        }
                    }
            );
        });


        btnUpdate.setOnClickListener(view->{
            ExecSQL("UPDATE CONTACTS SET " +
                            "Name = ?, " +
                            "Tel = ?, " +
                            "Email = ?, " +
                            "Category = ?, " +
                            "Description = ? " +
                            "WHERE ID = ? ",
                    new Object[]{
                            editName.getText().toString(),
                            editPhoneNumber.getText().toString(),
                            editEmail.getText().toString(),
                            editCategory.getText().toString(),
                            editDescription.getText().toString(),
                            ID
                    },
                    new OnQuerySuccess() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(UpdateAndDelete.this,
                                    "Update successful!",
                                    Toast.LENGTH_LONG).show();
                            BackToMain();
                        }
                    },
                    new OnError() {
                        @Override
                        public void OnQueryError(String error) {
                            Toast.makeText(UpdateAndDelete.this,
                                    error,Toast.LENGTH_LONG).show();
                        }
                    }
            );

        });

    }
}