package org.dddmuffi.myapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalObjectlist extends AppCompatActivity {
    private Button buttonGetCalList;
    private TextView textcalobject;
    private String ShareResponse="SocketResponse";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_objectlist);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ShareResponse,"");
        editor.commit();

        sharedPrefs.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        buttonGetCalList = (Button)findViewById(R.id.getCalList);
        textcalobject = (TextView)findViewById(R.id.calobject);
        buttonGetCalList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short) 0x0014);
                bb.putShort((short) 99);
                bb.putShort((short) 30);
                bb.position(0);
                MySocketClient mySocketClient = new MySocketClient(bb);
                mySocketClient.execute();
            }
        });

        List valueList = new ArrayList<String>();
        String commaSeparated="Empty";
        valueList= Arrays.asList(commaSeparated.split(","));
        ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_cal_row, valueList);
        final ListView lv = (ListView)findViewById(R.id.objectlist);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), "Sie haben " + lv.getAdapter().getItem(arg2).toString() + " gewählt",
                        Toast.LENGTH_SHORT).show();
                int objectnumber=Integer.parseInt(lv.getAdapter().getItem(arg2).toString().substring(0, lv.getAdapter().getItem(arg2).toString().indexOf("-")));
                ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short) 0x0014);
                bb.putShort((short) 6);
                bb.putShort((short) objectnumber);
                bb.position(0);
                MySocketClient mySocketClient = new MySocketClient(bb);
                mySocketClient.execute();
                textcalobject.setText(lv.getAdapter().getItem(arg2).toString());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(getString(R.string.preference_calobject_key), textcalobject.getText().toString());
                editor.commit();
            }
        });
    }

    SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
            if (!sharedPrefs.getString(ShareResponse,"").equals("")) {
                List valueList = new ArrayList<String>();
                valueList = Arrays.asList(sharedPrefs.getString(ShareResponse,"").split(","));
                ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_cal_row, valueList);
                final ListView lv = (ListView) findViewById(R.id.objectlist);
                lv.setAdapter(adapter);
            }
        }
    };

}
