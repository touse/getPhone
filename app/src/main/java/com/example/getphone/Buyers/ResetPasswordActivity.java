package com.example.getphone.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getphone.Prevalent.Prevalent;
import com.example.getphone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity
{
    private String check = "";
      private TextView pageTitle,titleQuestion;
      private EditText phoneNumber,question1,question2;
      private Button varifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check = getIntent().getStringExtra("check");
        pageTitle=findViewById(R.id.page_title);
        titleQuestion=findViewById(R.id.title_questions);
       phoneNumber=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question_1);
        question2=findViewById(R.id.question_2);
        varifyButton=findViewById(R.id.verify_btn);

    }



    @Override
    protected void onStart()
    {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings"))
        {
            pageTitle.setText("Set Questions");
titleQuestion.setText("Plesase anser secirty qerdvbuion");
            varifyButton.setText("Set");

            displaypreiousanswwer();

            varifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                   SetAnswes();

                }

            });

        }
        else if (check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);

            varifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     varifyUser();
                }
            });
        }
    }


    private void SetAnswes()
    {

        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();


        if(question1.equals("")  && question2.equals(""))
        {
            Toast.makeText(getApplicationContext(),"both qqq",Toast.LENGTH_SHORT).show();
        }


        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());


            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);


            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"seccuesful compele",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);

                    }

                }
            });


        }
    }

    private void displaypreiousanswwer()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String ans1=dataSnapshot.child("answer1").getValue().toString();
                String ans2=dataSnapshot.child("answer2").getValue().toString();

                question1.setText(ans1);
                question2.setText(ans2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private  void varifyUser()
    {
          final String phone=phoneNumber.getText().toString();
        final String answer1=question1.getText().toString().toLowerCase();
        final String answer2=question2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals(""))
        {

            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        String mPhone=dataSnapshot.child("phone").getValue().toString();

                        if(dataSnapshot.hasChild("Security Questions"))
                        {
                            String ans1=dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2=dataSnapshot.child("Security Questions").child("answer2").getValue().toString();


                            if(!ans1.equals(answer1))
                            {
                                Toast.makeText(getApplicationContext(),"your first answer is Wrong",Toast.LENGTH_SHORT).show();
                            }
                            else if(!ans2.equals(answer2))
                            {
                                Toast.makeText(getApplicationContext(),"your second answer is Wrong",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword=new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password Here...");
                                builder.setView(newPassword);


                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, final int i) {

                                        if(!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password").setValue(newPassword.getText()
                                                    .toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(getApplicationContext(),"Password Changed Successfully..",Toast.LENGTH_SHORT).show();

                                                        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.cancel();
                                    }
                                });
                                          builder.show();


                            }

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"You have not the set security Questions",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"this phone number not Exits..",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        else {
            Toast.makeText(getApplicationContext(),"Please fill the form",Toast.LENGTH_SHORT).show();
        }

    }


}
