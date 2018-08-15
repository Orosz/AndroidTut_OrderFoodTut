package com.orosz.myapp.orderfoodtut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orosz.myapp.orderfoodtut.Common.Common;
import com.orosz.myapp.orderfoodtut.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText editPhone, editPassword;
    Button btnSignn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
//        editPhone = (MaterialEditText) findViewById(R.id.editPhone);

        editPassword = (EditText)findViewById(R.id.editPassword);
        editPhone = (EditText) findViewById(R.id.editPhone);

        btnSignn = (Button) findViewById(R.id.btnSingIn);

        //Init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait..");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check if user does exist in the data base
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {

                            mDialog.dismiss();

                            //Get User information
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);

                            if (user.getPassword().equals(editPassword.getText().toString())) {

                                //Toast.makeText(SignIn.this, "Sign in successfully !", Toast.LENGTH_LONG).show();

                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();

                            } else {

                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Wrong Password !", Toast.LENGTH_LONG).show();

                            }

                        } else {

                            Toast.makeText(SignIn.this, "User not registered !", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
