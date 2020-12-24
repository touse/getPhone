package com.example.getphone.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.getphone.Buyers.MainActivity;
import com.example.getphone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private EditText nameInput,phoneInput,emailInput,passwordInput,addressInput;
      private Button registerButton;
    private Button sellerLoginBegin;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
       mAuth=FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        sellerLoginBegin=findViewById(R.id.seller_already_have_account_btn);
       nameInput=findViewById(R.id.seller_name);
        phoneInput=findViewById(R.id.seller_phone);
        emailInput=findViewById(R.id.seller_email);
        passwordInput=findViewById(R.id.seller_password);
        addressInput=findViewById(R.id.seller_address);
        registerButton=findViewById(R.id.seller_registration_btn);

        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSeller();
            }
        });




    }

    private void registerSeller() {
        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        final String address = addressInput.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please write your valid email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            passwordInput.setError("Plz Enter 6 Minimum Password Is 6 ");
            passwordInput.requestFocus();
        }
        else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please write your valid address...", Toast.LENGTH_SHORT).show();
        }


        //  if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals(""))

        else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
//         Log.d("Check","check1444");
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            final DatabaseReference rootRef;
                            rootRef = FirebaseDatabase.getInstance().getReference();

                            String sid = mAuth.getCurrentUser().getUid();


                            HashMap<String, Object> sellerMap = new HashMap<>();
                            sellerMap.put("sid", sid);
                            sellerMap.put("phone", phone);
                            sellerMap.put("email", email);
                            sellerMap.put("address", address);
                            sellerMap.put("name", name);

                            rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loadingBar.dismiss();
                                            Toast.makeText(getApplicationContext(), "You are Registered Successfully..", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), SellerLoginActivity.class);
                                            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                        }
                    });

        }

//else
  //   {
    //     Toast.makeText(getApplicationContext(),"Please complete all fields..",Toast.LENGTH_SHORT).show();
    // }




    }
}
