package com.akshay.medirx.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.akshay.medirx.R;
import com.akshay.medirx.common.Constant;
import com.akshay.medirx.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    // Initialize variables
    ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        onClickListner();
    }

    private void onClickListner() {

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                String mCorporateID = binding.corporateIdEdt.getText().toString().trim().toLowerCase();
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                    if (!mCorporateID.isEmpty()) {

                       /* if (mCorporateID.equals("recura") ||
                                mCorporateID.equals("wintura") ||
                                mCorporateID.equals("cetzine") ||
                                mCorporateID.equals("beat") ||
                                mCorporateID.equals("zedex") ||
                                mCorporateID.equals("futura")
                        ) {*/
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra(Constant.URL, Constant.WEB_URL + mCorporateID);
                            startActivity(intent);
                             // Toast.makeText(MainActivity.this, Constant.WEB_URL + mCorporateID, Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.INVISIBLE);
                       /* } else {
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Invalid Corporate Passcode.", Toast.LENGTH_SHORT).show();
                        }*/

                    } else {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Please Enter Corporate Passcode.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(view, "Please check network connection.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}