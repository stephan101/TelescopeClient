package org.dddmuffi.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MyLocationListActivity extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private MyLocationDbAdapter mDbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_list);
        mDbHelper = new MyLocationDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() {
        Cursor LocationsCursor = mDbHelper.fetchAllLocations();
        startManagingCursor(LocationsCursor);

        // Einen Array mit den gew�nschten Feldern erstellen (nur TITLE)
        String[] from = new String[]{MyLocationDbAdapter.KEY_TITLE};

        // ... und einen Array der Felder, die in die View einbezogen werden sollen
        int[] to = new int[]{R.id.text1};

        // Einfachen Cursor-Adapter erstellen und f�r Anzeige setzen
        SimpleCursorAdapter Locations =
                new SimpleCursorAdapter(this, R.layout.my_location_row,
                        LocationsCursor, from, to);
        setListAdapter(Locations);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mDbHelper.fetchLocation(id);
        Cursor location = mDbHelper.fetchLocation(id);
        startManagingCursor(location);
        String mTitleText= location.getString(
                location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_TITLE));
        String mBodyText = location.getString(
                location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_BODY));
        String mLongText = location.getString(
                location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_LONG));
        String mLatiText = location.getString(
                location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_LATI));
        String mAltiText= location.getString(
                location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_ALTI));
        Toast.makeText(MyLocationListActivity.this,mTitleText+" "+
                        getString(R.string.task_set_message),
                Toast.LENGTH_SHORT).show();
        double actlatitude=0;
        double actlongitude=0;
        double actaltitude=0;
        actlatitude=Double.parseDouble(mLatiText);
        actlongitude=Double.parseDouble(mLongText);
        actaltitude=Double.parseDouble(mAltiText);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(getString(R.string.acttitle_key),mTitleText);
        editor.putString(getString(R.string.actbody_key),mBodyText);
        editor.putString(getString(R.string.actlatitude_key),mLatiText);
        editor.putString(getString(R.string.actlongitude_key), mLongText);
        editor.putString(getString(R.string.actaltitude_key),mAltiText);
        editor.commit();
        ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short) 0x0014);
        bb.putShort((short) 1);
        bb.putFloat((float) actlongitude);bb.putFloat((float) actlatitude);bb.putFloat((float) actaltitude);
        bb.position(0);
        MySocketClient mySocketClient = new MySocketClient(bb);
        mySocketClient.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.location_list_menu_item_longpress, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.location_list_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_insert:
                createLocation();
                return true;
            case R.id.menu_settings:
//                Intent i = new Intent(this, TaskPreferences.class);
//                startActivity(i);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void createLocation() {
        Intent i = new Intent(this, MyLocationEditActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.menu_delete:
                mDbHelper.deleteLocation(info.id);
                fillData();
                return true;
            case R.id.menu_set:
                mDbHelper.fetchLocation(info.id);
                Cursor location = mDbHelper.fetchLocation(info.id);
                startManagingCursor(location);
                String mTitleText= location.getString(
                        location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_TITLE));
                String mBodyText = location.getString(
                    location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_BODY));
                String mLongText = location.getString(
                    location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_LONG));
                String mLatiText = location.getString(
                    location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_LATI));
                String mAltiText= location.getString(
                        location.getColumnIndexOrThrow(MyLocationDbAdapter.KEY_ALTI));
                Toast.makeText(MyLocationListActivity.this,mTitleText+" "+
                        getString(R.string.task_set_message),
                        Toast.LENGTH_SHORT).show();
                double actlatitude=0;
                double actlongitude=0;
                double actaltitude=0;
                actlatitude=Double.parseDouble(mLatiText);
                actlongitude=Double.parseDouble(mLongText);
                actaltitude=Double.parseDouble(mAltiText);
                final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(getString(R.string.acttitle_key),mTitleText);
                editor.putString(getString(R.string.actbody_key),mBodyText);
                editor.putString(getString(R.string.actlatitude_key),mLatiText);
                editor.putString(getString(R.string.actlongitude_key), mLongText);
                editor.putString(getString(R.string.actaltitude_key),mAltiText);
                editor.commit();
                ByteBuffer bb = ByteBuffer.allocate(160).order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short) 0x0014);
                bb.putShort((short) 1);
                bb.putFloat((float) actlongitude);bb.putFloat((float) actlatitude);bb.putFloat((float) actaltitude);
                bb.position(0);
                MySocketClient mySocketClient = new MySocketClient(bb);
                mySocketClient.execute();
                return true;
            case R.id.menu_edit:
                Intent i = new Intent(this, MyLocationEditActivity.class);
                i.putExtra("RowId", info.id);
                i.putExtra(MyLocationDbAdapter.KEY_ROWID, info.id);
                startActivityForResult(i, ACTIVITY_EDIT);
                return true;
        }
        return super.onContextItemSelected(item);
    }

}