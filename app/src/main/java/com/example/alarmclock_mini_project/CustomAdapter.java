package com.example.alarmclock_mini_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<Alarm> alarmList;
    private LayoutInflater layoutInflater;
    public CustomAdapter(Context c,List<Alarm>alarmList)
    {
        this.context=c;
        this.alarmList=alarmList;
        layoutInflater=(LayoutInflater.from(context));
    }
    @Override
    public int getCount()
    {
        return alarmList.size();
    }
    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=layoutInflater.inflate(R.layout.row_item,null);
        final Alarm selectedALarm=alarmList.get(i);
        final TextView nameTV=view.findViewById(R.id.nametextview);
        final TextView alarmTV=view.findViewById(R.id.timetextview);
        final AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        nameTV.setText(selectedALarm.getName());
        alarmTV.setText(selectedALarm.toString());
        final Intent serviceIntent=new Intent(context,AlarmReciever.class);
        final Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,selectedALarm.getHour());
        calendar.set(Calendar.MINUTE,selectedALarm.getMinute());
        calendar.set(Calendar.SECOND,0);
        if(calendar.getTimeInMillis()<System.currentTimeMillis())
        {
            calendar.add(Calendar.DATE,1);

        }
        ToggleButton toggleButton=view.findViewById(R.id.toggle);
        toggleButton.setChecked(selectedALarm.getStatus());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedALarm.setStatus(compoundButton.isChecked());
                DatabaseHelper db=new DatabaseHelper(context);
                db.updateAlarm(selectedALarm);
                MainActivity.alarmList.clear();
                List<Alarm>list=db.getALlALarms();
                alarmList.addAll(list);
                notifyDataSetChanged();
                if(!compoundButton.isChecked()&& selectedALarm.toString().equals(MainActivity.activeAlarm))
                {
                    serviceIntent.putExtra("extra","off");
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(context,i,serviceIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    context.sendBroadcast(serviceIntent);

                }
            }
        });
       if(selectedALarm.getStatus())
       {
           serviceIntent.putExtra("extra","on");
           serviceIntent.putExtra("active",selectedALarm.toString());
           PendingIntent pendingIntent=PendingIntent.getBroadcast(context,i,serviceIntent,PendingIntent.FLAG_UPDATE_CURRENT);
           alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

       }
        return view;
    }
}
