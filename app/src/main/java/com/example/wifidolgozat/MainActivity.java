package com.example.wifidolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private BottomNavigationView bottomNavigationView;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.wifiOn:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            textView.setText("Nincs jogosultság a wifi állapot módosítására");
                            Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                            startActivityForResult(panelIntent, 0);
                        }
                        wifiManager.setWifiEnabled(true);
                        textView.setText("Wifi bekapcsolva");
                        break;
                    case R.id.wifiOff:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            textView.setText("Nincs jogosultság a wifi állapot módosítására");
                            Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                            startActivityForResult(panelIntent, 0);
                        }
                        wifiManager.setWifiEnabled(false);
                        textView.setText("Wifi kikapcsolva");
                        break;
                    case R.id.wifiInfo:
                        ConnectivityManager conManager =
                                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (netInfo.isConnected()) {
                            int ip_number = wifiInfo.getIpAddress();
                            String ip = Formatter.formatIpAddress(ip_number);
                            textView.setText("IP: " + ip);
                        } else {
                            textView.setText("Nem csatlakoztál wifi hálózatra");
                        }
                        break;
                }


                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED
                    || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING
            ){
                textView.setText("Wifi bekapcsolva");
            }else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED
                    || wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING
            ){
                textView.setText("Wifi kikapcsolva");
            }
        }
    }

    private void init() {
        textView = findViewById(R.id.text_info);
        bottomNavigationView = findViewById(R.id.bottomNav);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }
}