package com.example.kannan.google_vision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Add_fine extends AppCompatActivity {

    ListView lv;
    ArrayList<String> text = new ArrayList<String>();
    TextView total_fine,total_fine_view;
    Button total_fine_button;
    double sum=0;
    List<FinedItem> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fine);
        lv = (ListView) findViewById(R.id.listview11);
       total_fine_view=(TextView)findViewById(R.id.textView_total_fine_view);
        total_fine=(TextView)findViewById(R.id.textView_total_fine);
        total_fine_button=(Button)findViewById(R.id.button_total_fine);

        total_fine_view.setVisibility(View.INVISIBLE);
        total_fine.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        final ArrayList<String> object = (ArrayList<String>) args.getSerializable("ARRAYLIST");

        for(int i =0; i<object.size(); i++){
            items.add(new FinedItem(object.get(i), 0.0));
        }

        lv.setAdapter(new MyCustomAdapter(getApplicationContext(), items));

    }



    public class MyCustomAdapter extends BaseAdapter { private List<FinedItem> mListItems;private LayoutInflater mLayoutInflater;

        public MyCustomAdapter(Context context,List<FinedItem> arrayList){

            mListItems = arrayList;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mListItems.size();
        }

        @Override
        public FinedItem getItem(int i) {
            return mListItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public  int getViewTypeCount(){
            return getCount();
        }

        @Override
        public  int getItemViewType(int position){
            return position;
        }


        @Override

        public View getView(final int position, View view, ViewGroup viewGroup) {


            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();

                view = mLayoutInflater.inflate(R.layout.offence_list, viewGroup, false);
                holder.offence_text = (TextView) view.findViewById(R.id.ofence_list_text_view);
                holder.fine_amount = (EditText) view.findViewById(R.id.fine_amount);

                final FinedItem myItem = getItem(position);


                holder.fine_amount.addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                String string = s.toString();
                                if(!string.isEmpty())
                                    myItem.amount = Double.parseDouble(string);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        }
                );

                view.setTag(holder);

            } else {
                holder = (ViewHolder)view.getTag();
            }

            final String stringItem = mListItems.get(position).name;


            if (stringItem != null) {

               holder.offence_text.setText(stringItem);

            } else {

                holder.offence_text.setText("Unknown");

            }
            total_fine_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < items.size(); i++) {

                            sum = sum + items.get(i).amount;
                        }

                    total_fine.setText(Double.toString(sum));

                    total_fine_button.setVisibility(View.INVISIBLE);
                    total_fine.setVisibility(View.VISIBLE);
                    total_fine_view.setVisibility(View.VISIBLE);
                }
            });
            return view;

        }

        private class ViewHolder {

            protected  TextView offence_text;
            protected  EditText fine_amount;


        }
    }

    class FinedItem{
        String name;
        double amount;

        FinedItem(String name, double fine){
            this.name = name;
            this.amount = fine;
        }
    }

}
