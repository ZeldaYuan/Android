package com.example.zelda.reminder;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class ReminderActivity extends ActionBarActivity {
    private ListView mListView;
    private RemindersDbAdapter mDbAdapter;
    private RemindersSimpleCursorAdapter mCursorAdapter;
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        mListView = (ListView) findViewById(R.id.reminder_list_view);
        mListView.setDivider(null);
        mDbAdapter = new RemindersDbAdapter(this);
        try {
            mDbAdapter.open();
            if (savedInstanceState == null) {
                //Clear all data
                mDbAdapter.deleteAllReminders();
                //Add some data
                mDbAdapter.createReminder("Wake them up at about 8:05AM", true);//Buy Learn Android Studio
                mDbAdapter.createReminder("Remind Cynthia and Alex homework", true); //Send Dad birthday gift
                mDbAdapter.createReminder("Talk to them about 10 minutes", false);//Dinner at the Gage on Friday
                mDbAdapter.createReminder("Ask them help me for the housework", false);//String squash racket
                mDbAdapter.createReminder("Shovel and salt walkways", false);
                mDbAdapter.createReminder("Ask them drink the milk at 9:00PM", true); //Prepare Advanced Android syllabus
                mDbAdapter.createReminder("Do my things, check email", false);//Do my things, check email  Buy new office chair
                mDbAdapter.createReminder("Call Auto-body shop for quote", false);
                mDbAdapter.createReminder("Renew membership to club", false);
                mDbAdapter.createReminder("Ask them go to bed no late than 10:00PM", true); //Buy new Galaxy Android phone
                mDbAdapter.createReminder("Sell old Android phone - auction", false);
                mDbAdapter.createReminder("Buy new paddles for kayaks", false);
                mDbAdapter.createReminder("Call accountant about tax returns", false);
                mDbAdapter.createReminder("Buy 300,000 shares of Google", false);
                mDbAdapter.createReminder("Call the Dalai Lama back", true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = mDbAdapter.fetchAllReminders();
        //from columns defined in the db
        String[] from = new String[]{
                RemindersDbAdapter.COL_CONTENT
        };
        //to the ids of views in the layout
        int[] to = new int[]{
                R.id.row_text
        };
        mCursorAdapter = new RemindersSimpleCursorAdapter(
                //context
                ReminderActivity.this,
                //the layout of the row
                R.layout.reminders_row,
                //cursor
                cursor,
                //from columns defined in the db
                from,
                //to the ids of views in the layout
                to,
                //flag - not used
                0);
        //the cursorAdapter (controller) is now updating the listView (view)
        //with data from the db (model)
        mListView.setAdapter(mCursorAdapter);

        //when we click an individual item in the listview
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) { AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this); ListView modeListView = new ListView(ReminderActivity.this);
                String[] modes = new String[] { "Edit Reminder", "Delete Reminder" }; ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(ReminderActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //edit reminder
                        if (position == 0) {
                            int nId = getIdFromPosition(masterListPosition);
                            Reminder reminder = mDbAdapter.fetchReminderById(nId);
                            fireCustomDialog(reminder);
                            //delete reminder
                        } else {
                            mDbAdapter.deleteReminderById(getIdFromPosition(masterListPosition));
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                        }
                        dialog.dismiss();
                    }
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        //edit reminder
//                        if (position == 0) {
//                            Toast.makeText(ReminderActivity.this, "edit "
//                                    + masterListPosition, Toast.LENGTH_SHORT).show();
//                            //delete reminder
//                        } else {
//                            Toast.makeText(ReminderActivity.this, "delete "
//                                    + masterListPosition, Toast.LENGTH_SHORT).show();
//                        }
//                        dialog.dismiss();
//                    }
                });
            }
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ReminderActivity.this, "clicked " + position,
//                        Toast.LENGTH_SHORT).show();
//            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                        checked) { }
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_reminder:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (mListView.isItemChecked(nC)) {
                                    mDbAdapter.deleteReminderById(getIdFromPosition(nC));
                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }
                        return false;
                    }
                    @Override
                    public void onDestroyActionMode(ActionMode mode) { }
                });
        }

        }

    private int getIdFromPosition(int nC) {
        return (int)mCursorAdapter.getItemId(nC);
    }

    private void fireCustomDialog(final Reminder reminder){
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (reminder != null);
        //this is for an edit
        if (isEditOperation){
            titleView.setText("Edit Reminder");
            checkBox.setChecked(reminder.getImportant() == 1);
            editCustom.setText(reminder.getContent());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = editCustom.getText().toString();
                if (isEditOperation) {
                    Reminder reminderEdited = new Reminder(reminder.getId(),
                            reminderText, checkBox.isChecked() ? 1 : 0);
                    mDbAdapter.updateReminder(reminderEdited);
                    //this is for new reminder
                } else {
                    mDbAdapter.createReminder(reminderText, checkBox.isChecked());
                }
                mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                dialog.dismiss();
            }
        });
        Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                //create new Reminder
                fireCustomDialog(null);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }
    }
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_new:
//                //create new Reminder
//                Log.d(getLocalClassName(), "create new Reminder");
//                return true;
//            case R.id.action_exit:
//                finish();
//                return true;
//            default:
//                return false;
//        }
//    }
//Abbreviated for brevity
    }
