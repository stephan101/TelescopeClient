package org.dddmuffi.myapplication;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class MyLocation extends AppCompatActivity implements LocationListener {

    private EditText mLatiText,mLongText,mLatiText2,mLongText2,mAltiText;
    private Button mGPSButton;
    private Button mLocationButton;
    private CheckBox mAutoLocCheckBox;
    private LocationManager locationManager;
    private Location location;
    private Switch switchFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        switchFormat = (Switch)findViewById(R.id.switchFormat);
        mLatiText = (EditText) findViewById(R.id.lateditText);
        mLatiText2 = (EditText) findViewById(R.id.lateditText2);
        mLongText = (EditText) findViewById(R.id.longeditText);
        mLongText2 = (EditText) findViewById(R.id.longeditText2);

        mLatiText.setText(sharedPrefs.getString(getString(R.string.actlatitude_key), "47.00000"));

        mLatiText2.setEnabled(false);
        mLatiText2.setFocusable(false);
        try {
            mLatiText2.setText(location.convert(Double.parseDouble(mLatiText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
        } catch (Exception e) {
            mLatiText2.setText(e.toString());
        }

        mLatiText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    mLatiText2.setText(location.convert(Double.parseDouble(mLatiText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
                } catch (Exception e) {
                    mLatiText2.setText(e.toString());
                }
                return false;
            }
        });
        mLatiText2.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    mLatiText.setText(Double.toString(location.convert(mLatiText2.getText().toString())));
                } catch (Exception e) {
                    mLatiText.setText(e.toString());
                }
                return false;
            }
        });

        mLongText.setText(sharedPrefs.getString(getString(R.string.actlongitude_key), "25.00000"));
        mLongText2.setEnabled(false);
        mLongText2.setFocusable(false);
        try {
            mLongText2.setText(location.convert(Double.parseDouble(mLongText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
        } catch (Exception e) {
            mLongText2.setText(e.toString());
        }

        mLongText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    mLongText2.setText(location.convert(Double.parseDouble(mLongText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
                } catch (Exception e) {
                    mLongText2.setText(e.toString());
                }
                return false;
            }
        });
        mLongText2.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    mLongText.setText(Double.toString(location.convert(mLongText2.getText().toString())));
                } catch (Exception e) {
                    mLongText.setText(e.toString());
                }
                return false;
            }
        });

        switchFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    mLatiText.setEnabled(false);
                    mLatiText.setFocusable(false);
                    mLatiText.setClickable(false);
                    mLongText.setEnabled(false);
                    mLongText.setFocusable(false);
                    mLongText.setClickable(false);

                    mLatiText2.setEnabled(true);
                    mLatiText2.setFocusable(true);
                    mLatiText2.setClickable(true);
                    mLatiText2.setFocusableInTouchMode(true);
                    mLongText2.setEnabled(true);
                    mLongText2.setFocusable(true);
                    mLongText2.setFocusableInTouchMode(true);
                    mLongText2.setClickable(true);
                } else {
                    mLatiText.setEnabled(true);
                    mLatiText.setFocusable(true);
                    mLatiText.setClickable(true);
                    mLatiText.setFocusableInTouchMode(true);
                    mLongText.setEnabled(true);
                    mLongText.setFocusable(true);
                    mLongText.setClickable(true);
                    mLongText.setFocusableInTouchMode(true);

                    mLatiText2.setEnabled(false);
                    mLatiText2.setFocusable(false);
                    mLatiText2.setClickable(false);
                    mLongText2.setEnabled(false);
                    mLongText2.setFocusable(false);
                    mLongText2.setClickable(false);
                }
            }
        });
        mAltiText = (EditText) findViewById(R.id.alteditText);
        mAltiText.setText(sharedPrefs.getString(getString(R.string.actaltitude_key), "362.00000"));

        mGPSButton = (Button) findViewById(R.id.getGPSButton);
        mLocationButton = (Button) findViewById(R.id.setLocButton);
        mAutoLocCheckBox = (CheckBox) findViewById(R.id.autolocCheckBox);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mAutoLocCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoLocation(isChecked);
            }
        });
        mGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getLocation();
            }
        });
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                double actlatitude=0;
                double actlongitude=0;
                double actaltitude=0;
                actlatitude=Double.parseDouble(mLatiText.getText().toString());
                actlongitude=Double.parseDouble(mLongText.getText().toString());
                actaltitude=Double.parseDouble(mAltiText.getText().toString());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(getString(R.string.acttitle_key),"");
                editor.putString(getString(R.string.actbody_key),"");
                editor.putString(getString(R.string.actlatitude_key),mLatiText.getText().toString());
                editor.putString(getString(R.string.actlongitude_key), mLongText.getText().toString());
                editor.putString(getString(R.string.actaltitude_key),mAltiText.getText().toString());
                editor.commit();
                ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short) 0x0014);
                bb.putShort((short) 1);
                bb.putFloat((float) actlongitude);bb.putFloat((float) actlatitude);bb.putFloat((float) actaltitude);
                bb.position(0);
                MySocketClient mySocketClient = new MySocketClient(bb);
                mySocketClient.execute();
            }
        });
    }

    public void autoLocation(Boolean on) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        mLatiText.setEnabled(!on);
        mLongText.setEnabled(!on);
        mAltiText.setEnabled(!on);
        if (on){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
        }else{
            locationManager.removeUpdates(this);
        }

    }
    public void getLocation() {
        Toast.makeText(getBaseContext(), "Single Update", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        /*
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            // FAIL
        }
        */
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    @Override
    public void onLocationChanged(Location location) {
        double actlatitude=0;
        double actlongitude=0;
        double actaltitude=0;

        actlatitude=location.getLatitude();
        actlongitude=location.getLongitude();
        actaltitude=location.getAltitude();
        String msg = "New Latitude: " + actlatitude
                + "New Longitude: " + actlongitude
                + "New Heigh: " + actaltitude;
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        mLatiText.setText("" + actlatitude);
        mLatiText2.setText(""+location.convert(actlatitude, location.FORMAT_SECONDS).toString().replace(',', '.'));
        mLongText.setText("" + actlongitude);
        mLongText2.setText("" + location.convert(actlongitude, location.FORMAT_SECONDS).toString().replace(',','.'));
        mAltiText.setText("" + actaltitude);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(getString(R.string.acttitle_key),"");
        editor.putString(getString(R.string.actbody_key),"");
        editor.putString(getString(R.string.actlatitude_key), "" + actlatitude);
        editor.putString(getString(R.string.actlongitude_key), "" + actlongitude);
        editor.putString(getString(R.string.actaltitude_key), "" + actaltitude);
        editor.commit();

        ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short) 0x0014);
        bb.putShort((short) 1);
        bb.putFloat((float) actlongitude);bb.putFloat((float) actlatitude);bb.putFloat((float) actaltitude);        bb.position(0);
        MySocketClient mySocketClient = new MySocketClient(bb);
        mySocketClient.execute();

    }
    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    /*
    @Override
    public void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.removeUpdates(this);
    }
    */

    @Override
    protected void onStop() {
        super.onStop();
    }

}