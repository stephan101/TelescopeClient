package org.dddmuffi.myapplication;

import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class GoTo extends AppCompatActivity {

    private EditText rektText,deklText,rektText2,deklText2;
    private Location location;
    private Switch switchFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        rektText = (EditText) findViewById(R.id.rekteditText);
        rektText2 = (EditText) findViewById(R.id.rekteditText2);
        deklText = (EditText) findViewById(R.id.dekleditText);
        deklText2 = (EditText) findViewById(R.id.dekleditText2);
        switchFormat = (Switch)findViewById(R.id.switchFormat);

        rektText.setText(sharedPrefs.getString(getString(R.string.actrekt_key), "0.00000"));
        rektText2.setEnabled(false);
        rektText2.setFocusable(false);
        try {
            rektText2.setText(location.convert(Double.parseDouble(rektText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
        } catch (Exception e) {
            rektText2.setText(e.toString());
        }
        rektText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    rektText2.setText(location.convert(Double.parseDouble(rektText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
                } catch (Exception e) {
                    rektText2.setText(e.toString());
                }
                return false;
            }
        });

        rektText2.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    rektText.setText(Double.toString(location.convert(rektText2.getText().toString())));
                } catch (Exception e) {
                    rektText.setText(e.toString());
                }
                return false;
            }
        });

        deklText.setText(sharedPrefs.getString(getString(R.string.actdekl_key), "0.00000"));
        deklText2.setEnabled(false);
        deklText2.setFocusable(false);
        try {
            deklText2.setText(location.convert(Double.parseDouble(deklText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
        } catch (Exception e) {
            deklText2.setText(e.toString());
        }
        deklText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    deklText2.setText(location.convert(Double.parseDouble(deklText.getText().toString()), location.FORMAT_SECONDS).toString().replace(',', '.'));
                } catch (Exception e) {
                    deklText2.setText(e.toString());
                }
                return false;
            }
        });
        deklText2.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v,int actionId, KeyEvent event){
                try {
                    deklText.setText(Double.toString(location.convert(deklText2.getText().toString())));
                } catch (Exception e) {
                    deklText.setText(e.toString());
                }
                return false;
            }
        });
        switchFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    deklText.setEnabled(false);
                    deklText.setFocusable(false);
                    deklText.setClickable(false);
                    rektText.setEnabled(false);
                    rektText.setFocusable(false);
                    rektText.setClickable(false);

                    deklText2.setEnabled(true);
                    deklText2.setFocusable(true);
                    deklText2.setClickable(true);
                    deklText2.setFocusableInTouchMode(true);
                    rektText2.setEnabled(true);
                    rektText2.setFocusable(true);
                    rektText2.setFocusableInTouchMode(true);
                    rektText2.setClickable(true);
                } else {
                    deklText.setEnabled(true);
                    deklText.setFocusable(true);
                    deklText.setClickable(true);
                    deklText.setFocusableInTouchMode(true);
                    rektText.setEnabled(true);
                    rektText.setFocusable(true);
                    rektText.setClickable(true);
                    rektText.setFocusableInTouchMode(true);

                    deklText2.setEnabled(false);
                    deklText2.setFocusable(false);
                    deklText2.setClickable(false);
                    rektText2.setEnabled(false);
                    rektText2.setFocusable(false);
                    rektText2.setClickable(false);
                }
            }
        });
    }

    protected void onStop() {
        super.onStop();
        double actrekt=0;
        double actdekl=0;
        double mtime=0;
        actrekt=Double.parseDouble(rektText.getText().toString())*2147483648.0/12.0;
        actdekl=Double.parseDouble(deklText.getText().toString())*1073741824.0/90.0;
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(getString(R.string.actrekt_key), rektText.getText().toString());
        editor.putString(getString(R.string.actdekl_key), deklText.getText().toString());
        editor.commit();
        ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short) 0x0014);
        bb.putShort((short) 0);
        bb.putFloat((float) mtime);
        bb.putFloat((float) mtime);
        bb.putInt((int) actrekt);
        bb.putInt((int) actdekl);
        bb.position(0);
        MySocketClient mySocketClient = new MySocketClient(bb);
        mySocketClient.execute();
    }
}
