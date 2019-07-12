package com.example.kannan.google_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.*;

public class Offence extends AppCompatActivity {

    ListView offences;
    Button add_offence;
    ArrayList<String> selected_offences = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offence);

        offences = (ListView) findViewById(R.id.list1);
        add_offence=(Button)findViewById(R.id.add_offence);

        final ArrayList<String> cate = new ArrayList<String>();
        cate.add("No Driving Licence(3(1) r/w 177 of MV Act)");
        cate.add("DL not in possession(130(1) r/w 177 of MV Act)");
        cate.add("No Uniform (41 r/w 177 ov MV Act )");
        cate.add("NO Seat Belt (CMVR 138(3) r/w 177 of MV Act)");
        cate.add("No Helmet(129 r/w 177 of MV Act)");
        cate.add("Records not in possession(130(3) r/w 177 of MV Act)");
        cate.add("No Number Plate(56 r/w 177 of MV Act)");
        cate.add("Number Plate not Proper(51 r/w 177 of MV Act)");
        cate.add("No Break Light/Indicator(120(2) r/w 177 of MV act)");
        cate.add("No Side Mirror(251 r/w 177 of MV Act)");
        cate.add("Tripple Ride(128 r/w 177 of MV Act)");
        cate.add("No Insurance Certificate(146 r/w 196 of MV Act)");
        cate.add("No Pollution Certificate(192(2) of MV Act)");
        cate.add("Overspeed (183 of MV Act)");
        cate.add("Rash and negligent Driving(184 of MV Act)");
        cate.add("Drunken Driving(185 of MV Act)");
        cate.add("Accident not Reported(134(b) r/w 187 of MV Act)");
        cate.add("Failure to Stop Demanded(132(1) r/w 179 of MV Act)");
        cate.add("left Side Overtaking(RRR 4 r/w 177 of MV Act)");
        cate.add("Dangerous Overtaking(RRR 6 r/w 177 of MV Act)");
        cate.add("One Way Violation(RRR 17 r/w 177 of MV Act)");
        cate.add("Violation of Law & Order (One Way) (119 r/w 179 of MV Act)");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, cate);

       offences.setAdapter(adapter);
       offences.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



        add_offence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_offences.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Nothing Selected",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), Add_fine.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", (Serializable) selected_offences);
                    intent.putExtra("BUNDLE", args);
                    startActivity(intent);
                }
            }
        });



        offences.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               SparseBooleanArray checked = offences.getCheckedItemPositions();
               for (int i = 0; i < offences.getAdapter().getCount(); i++) {
                   if (checked.get(i)) {
                       String items = (String) parent.getItemAtPosition(i);
                       selected_offences.add(items);
                       Set<String> aSet = new HashSet<String>(selected_offences);
                       selected_offences.clear();
                       selected_offences.addAll(aSet);

                   }
                   else  if (!checked.get(i))
                   {
                       String items = (String) parent.getItemAtPosition(i);
                       for(int k=0; k< selected_offences.size(); k++){
                           if(selected_offences.get(k).equals(items)) {
                               selected_offences.remove(k);
                           }
                       }
                   }
               }


           }
       });




    }

}