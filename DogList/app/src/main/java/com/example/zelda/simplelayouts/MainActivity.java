package com.example.zelda.simplelayouts;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        Person[] listItems = new Person[]{
                new Person(R.drawable.dog, "Jenney", "New York",
                        "www.allmydogs.com/jenny",
                        "White cute dog."),
                new Person(R.drawable.wallace,"Wallace", "Virginia",
                        "www.allmydogs.com/wallace",
                        "Brown dog."),
                new Person(R.drawable.david,"David", "North Carolina",
                        "www.allmydogs.com/david",
                        "Two white dogs."),
                new Person(R.drawable.mark, "Mark", "Denver",
                        "www.allmydogs.com/mark",
                        "Mother dog and her baby.")
        };
        setListAdapter(new PersonAdapter(this,
                        android.R.layout.simple_expandable_list_item_2,
                        listItems)
        ); }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Person person = (Person) l.getItemAtPosition(position);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.IMAGE, person.image);
        intent.putExtra(ProfileActivity.NAME, person.name);
        intent.putExtra(ProfileActivity.LOCATION, person.location);
        intent.putExtra(ProfileActivity.WEBSITE, person.website);
        intent.putExtra(ProfileActivity.DESCRIPTION, person.descr);
        startActivity(intent);
    }
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        Intent intent = new Intent(this, ProfileActivity.class);
//        startActivity(intent);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class PersonAdapter extends BaseAdapter {
        private final Context context;
        private final int layout;
        private Person[] listItems;
        public PersonAdapter(Context context, int layout, Person[] listItems) {
            this.context = context;
            this.layout = layout;
            this.listItems = listItems;
        }
        @Override
        public int getCount() {
            return listItems.length;
        }
        @Override
        public Object getItem(int i) {
            return listItems[i];
        }
        @Override
        public long getItemId(int i) {
            return i; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view==null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(layout, parent, false);
            }
            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
            TextView text2 = (TextView) view.findViewById(android.R.id.text2);
            text1.setText(listItems[position].name);
            text2.setText(listItems[position].website);
            return view;
        }
    }

//    private class PersonAdapter implements ListAdapter {
//        public PersonAdapter(MainActivity mainActivity, int simple_expandable_list_item_2, Person[] listItems) {
//        }
//    }
}
