package org.dddmuffi.myapplication;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MyLocationEditActivity extends Activity implements LocationListener {


    private EditText mTitleText, mBodyText, mLongText, mLatiText, mLatiText2, mLongText2, mAltiText;
    private Button mGPSButton, mConfirmButton;
    private LocationManager locationManager;
    private Location location;
    private Long mRowId;
    private MyLocationDbAdapter mDbHelper;
    private Switch switchFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new MyLocationDbAdapter(this);
        setContentView(R.layout.activity_my_location_edit);
        switchFormat = (Switch)findViewById(R.id.switchFormat);
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mLongText = (EditText) findViewById(R.id.longeditText);
        mLongText2 = (EditText) findViewById(R.id.longeditText2);
        mLatiText = (EditText) findViewById(R.id.lateditText);
        mLatiText2 = (EditText) findViewById(R.id.lateditText2);
        mAltiText = (EditText) findViewById(R.id.body);
        mGPSButton = (Button) findViewById(R.id.getGPSButton);
        mConfirmButton = (Button) findViewById(R.id.confirm);

        mRowId = savedInstanceState != null
                ? savedInstanceState.getLong(MyLocationDbAdapter.KEY_ROWID)
                : null;
        registerButtonListenersAndSetDefaultText();

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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getLocation();
            }
        });
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
/*
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
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
*/
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

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null
                    ? extras.getLong(MyLocationDbAdapter.KEY_ROWID)
                    : null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbHelper.open();
        setRowIdFromIntent();
        populateFields();
    }
    private void registerButtonListenersAndSetDefaultText() {

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveState();
                setResult(RESULT_OK);
                Toast.makeText(MyLocationEditActivity.this,
                        getString(R.string.task_saved_message),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void populateFields()  {
        if (mRowId != null) {
            Cursor reminder = mDbHelper.fetchLocation(mRowId);
            startManagingCursor(reminder);
            mTitleText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_TITLE)));
            mBodyText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_BODY)));
            mLongText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_LONG)));
            mLatiText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_LATI)));
            mAltiText.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_ALTI)));
            try {
                mLatiText2.setText(location.convert(Double.parseDouble(mLatiText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
            } catch (Exception e) {
                mLatiText2.setText(e.toString());
            }
            try {
                mLongText2.setText(location.convert(Double.parseDouble(mLongText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
            } catch (Exception e) {
                mLongText2.setText(e.toString());
            }
        } else {
/*            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(this);
            String defaultTitleKey = getString(R.string.pref_task_title_key);
            String defaultTimeKey =
                    getString(R.string.pref_default_time_from_now_key);

            String defaultTitle = prefs.getString(defaultTitleKey, "");
            String defaultTime = prefs.getString(defaultTimeKey, "");
            if("".equals(defaultTitle) == false)
                mTitleText.setText(defaultTitle);
            if("".equals(defaultTime) == false)
                mCalendar.add(Calendar.MINUTE, Integer.parseInt(defaultTime));
                */
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(MyLocationDbAdapter.KEY_ROWID, mRowId);
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        String actlong = mLongText.getText().toString();
        String actlati = mLatiText.getText().toString();
        String actalti = mAltiText.getText().toString();
        if (mRowId == null) {
            long id = mDbHelper.createLocation(title, body, actlong, actlati, actalti);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper
                    .updateLocation(mRowId, title, body, actlong, actlati, actalti);
        }
    }
}
