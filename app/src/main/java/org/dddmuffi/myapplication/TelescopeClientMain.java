package org.dddmuffi.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TelescopeClientMain extends AppCompatActivity  {

    private Boolean vibrateBoolean;
    private TextView textaction, textResponse, textAddress, textcalobject,textloctb,textloclla;
    private Button buttonConnect, buttonClear, buttonUp, buttonDown, buttonLeft, buttonRight;
    private Switch switchTrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello__world);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);



        final Animation animTranslateup = AnimationUtils.loadAnimation(this, R.anim.anim_translate_up);
        final Animation animTranslatedown = AnimationUtils.loadAnimation(this, R.anim.anim_translate_down);
        final Animation animRotateleft = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_left);
        final Animation animRotateright = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_right);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textaction=(TextView) findViewById(R.id.actualAction);
        textAddress = (TextView)findViewById(R.id.ipaddress);
        textloctb = (TextView)findViewById(R.id.loctb);
        textloclla = (TextView)findViewById(R.id.loclla);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        buttonUp = (Button)findViewById(R.id.buttonUp);
        buttonDown = (Button)findViewById(R.id.buttonDown);
        buttonLeft = (Button)findViewById(R.id.buttonLeft);
        buttonRight = (Button)findViewById(R.id.buttonRight);
        switchTrace = (Switch)findViewById(R.id.switchTrace);
        textResponse = (TextView)findViewById(R.id.response);
        textcalobject = (TextView)findViewById(R.id.calobject);
        textcalobject.setText(sharedPrefs.getString(getString(R.string.preference_calobject_key), ""));
        textAddress.setText(sharedPrefs.getString(getString(R.string.preference_ipadresse_key), "")+
                ":"+sharedPrefs.getString(getString(R.string.preference_port_key), ""));
        textloctb.setText(sharedPrefs.getString(getString(R.string.acttitle_key), "")+
                "-"+sharedPrefs.getString(getString(R.string.actbody_key), ""));
        textloclla.setText((cutString(sharedPrefs.getString(getString(R.string.actlongitude_key), "0.000"),5))+
                "/"+(cutString(sharedPrefs.getString(getString(R.string.actlatitude_key), "0.000"),5))+
                "/"+(cutString(sharedPrefs.getString(getString(R.string.actaltitude_key), "0.000"),5)));
        setVibrateBoolean(sharedPrefs.getBoolean(getString(R.string.preference_vibrate_key), true));

        switchTrace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textaction.setText("Switch Trace");
                if(getVibrateBoolean()){vibe.vibrate(200);}
                if (isChecked == true) {
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 7);
                    bb.putShort((short) 0);bb.putShort((short) 0);bb.putShort((short) 0);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();

                } else {
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 7);
                    bb.putShort((short) 0);bb.putShort((short) 0);bb.putShort((short) 0);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();

                }

            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textaction.setText("Connect");
                if(getVibrateBoolean()){vibe.vibrate(200);}
                ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short) 0x0014);
                bb.putShort((short) 8);
                bb.position(0);
                MySocketClient mySocketClient = new MySocketClient(bb);
                mySocketClient.execute();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getVibrateBoolean()){vibe.vibrate(200);}
                textResponse.setText("");
            }
        });

        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textaction.setText("UP press");
                    v.startAnimation(animTranslateup);
                    if(getVibrateBoolean()){vibe.vibrate(200);}
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 5);
                    bb.putShort((short) 1);bb.putShort((short) 1);bb.putShort((short) 0);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textaction.setText("UP release");
                    v.clearAnimation();
                    vibe.cancel();
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 5);
                    bb.putShort((short) 1);bb.putShort((short) 0);bb.putShort((short) 0);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();

                }
                return false;
            }

        });
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textaction.setText("Down press");
                    v.startAnimation(animTranslatedown);
                    if(getVibrateBoolean()){vibe.vibrate(200);}
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 5);
                    bb.putShort((short) 1);bb.putShort((short) 1);bb.putShort((short) 1);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textaction.setText("Down release");
                    v.clearAnimation();
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 5);
                    bb.putShort((short) 1);bb.putShort((short) 0);bb.putShort((short) 1);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();
                }
                return false;
            }

        });
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textaction.setText("Left press");
                    v.startAnimation(animRotateleft);
                    if(getVibrateBoolean()){vibe.vibrate(200);}
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 5);
                    bb.putShort((short) 0);bb.putShort((short) 1);bb.putShort((short) 0);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textaction.setText("Left release");
                    v.clearAnimation();
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) 0x0014);
                    bb.putShort((short) 5);
                    bb.putShort((short) 0);bb.putShort((short) 0);bb.putShort((short) 0);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();
                }
                return false;
            }

        });
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textaction.setText("Right press");
                    v.startAnimation(animRotateright);
                    if(getVibrateBoolean()){vibe.vibrate(200);}
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short)0x0014);
                    bb.putShort((short)5);
                    bb.putShort((short)0);bb.putShort((short)1);bb.putShort((short)1);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textaction.setText("Right release");
                    v.clearAnimation();
                    ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short)0x0014);
                    bb.putShort((short)5);
                    bb.putShort((short) 0);bb.putShort((short) 0);bb.putShort((short) 1);
                    bb.position(0);
                    MySocketClient mySocketClient = new MySocketClient(bb);
                    mySocketClient.execute();
                }
                return false;
            }

        });

    };
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(Menu.NONE, 0, 0, "Exit");
        getMenuInflater().inflate(R.menu.menu_hello__world, menu);
        return true;
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (item.getItemId()==0) {
            finish();
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (id == R.id.action_calobjectslist) {
            startActivity(new Intent(this, CalObjectlist.class));
            return true;
        } else if (id == R.id.action_mylocation) {
            startActivity(new Intent(this, MyLocation.class));
            return true;
        } else if (id == R.id.action_mylocationlist) {
            startActivity(new Intent(this, MyLocationListActivity.class));
            return true;
        } else if (id == R.id.action_goTo) {
            startActivity(new Intent(this, GoTo.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
            System.out.println(key);
            textAddress.setText(sharedPrefs.getString(getString(R.string.preference_ipadresse_key), "") +
                    ":" + sharedPrefs.getString(getString(R.string.preference_port_key), ""));
            textloctb.setText(sharedPrefs.getString(getString(R.string.acttitle_key), "")+
                    "-"+sharedPrefs.getString(getString(R.string.actbody_key), ""));
            textloclla.setText((cutString(sharedPrefs.getString(getString(R.string.actlongitude_key), "0.000"),5))+
                    "/"+(cutString(sharedPrefs.getString(getString(R.string.actlatitude_key), "0.000"),5))+
                    "/"+(cutString(sharedPrefs.getString(getString(R.string.actaltitude_key), "0.000"),5)));
            setVibrateBoolean(sharedPrefs.getBoolean(getString(R.string.preference_vibrate_key), true));
            textcalobject.setText(sharedPrefs.getString(getString(R.string.preference_calobject_key), ""));
            textResponse.setText(sharedPrefs.getString("SocketError",""));
        }
    };
    public Boolean getVibrateBoolean() {
        return vibrateBoolean;
    }
    public void setVibrateBoolean(Boolean vibrateBoolean) {
        this.vibrateBoolean = vibrateBoolean;
    }

    public String cutString(String text, int cutlength){
        if (text.length()>cutlength){
            return text.substring(0,cutlength);
        }
        return text;
    }

}
