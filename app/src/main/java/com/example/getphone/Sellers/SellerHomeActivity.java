package com.example.getphone.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getphone.Buyers.MainActivity;
import com.example.getphone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SellerHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            //openFragment(HomeFragment.newInstance("", ""));
                            mTextMessage.setText(R.string.title_home);

                            return true;


                        case R.id.navigation_add:
                            // openFragment(SmsFragment.newInstance("", ""));
                            Intent intentcat=new Intent(getApplicationContext(), SellerAddNewProductActivity.class);

                            startActivity(intentcat);
                            finish();
                            return true;


                        case R.id.navigation_log:
                            //  openFragment(NotificationFragment.newInstance("", ""));
                            // mTextMessage.setText(R.string.title_notifications);
                            final FirebaseAuth mAuth;
                            mAuth=FirebaseAuth.getInstance();
                            mAuth.signOut();

                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            return true;
                    }
                    return false;
                }
            };
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        bottomNavigation = findViewById(R.id.nav_view);

    }
}
