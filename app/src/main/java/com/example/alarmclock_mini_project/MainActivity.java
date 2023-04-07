package com.example.alarmclock_mini_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


public static String activeAlarm="";
private ListView listView;
//private static final int RE
private static final int REQUEST_CODE=1000;
public static List<Alarm> alarmList=new ArrayList<>();
private CustomAdapter customAdapter;
private DatabaseHelper db=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.add);

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult activityResult) {


                        int result = activityResult.getResultCode();
                        Intent data = activityResult.getData();

//                        boolean needRefresh = false;
                        if (result == RESULT_OK)
                    {
                        listView = findViewById(R.id.listview);
                        List<Alarm> list = db.getALlALarms();
                        alarmList.addAll(list);
                        customAdapter = new CustomAdapter(getApplicationContext(), alarmList);
                        listView.setAdapter(customAdapter);

                        boolean needRefresh =data.getExtras().getBoolean("needRefresh");
            if(needRefresh)
            {


//                alarmList.clear();
//                List<Alarm>list1=db.getALlALarms();
//                alarmList.addAll(list1);
//                customAdapter.notifyDataSetChanged();
            }
                    }

                    }
                    }

        );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
////                startActivityForResult(intent,REQUEST_CODE);
                activityResultLauncher.launch(intent);
//                startActivity(intent);
            }

        });



    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode== Activity.RESULT_OK&&requestCode==REQUEST_CODE)
//        {
//            boolean needRefresh =data.getExtras().getBoolean("needRefresh");
//            if(needRefresh)
//            {
//                alarmList.clear();
//                List<Alarm>list=db.getALlALarms();
//                alarmList.addAll(list);
//                customAdapter.notifyDataSetChanged();
//            }
//        }
//    }
}