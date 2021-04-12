package seedcommando.com.empowerapp.applicationstatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.empowerapp.R;
import seedcommando.com.empowerapp.pojos.WorkFromHome.WorkFromHomeSetDataPoJo;

/**
 * Created by commando4 on 3/15/2018.
 */

public class ApplicationStatusWFH extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<WorkFromHomeSetDataPoJo> mDataSource;

    public ApplicationStatusWFH(Context context, ArrayList<WorkFromHomeSetDataPoJo> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.application_status_wfh, parent, false);

        // Get title element
        TextView fromdate =
                rowView.findViewById(R.id.fromdate);
       LinearLayout from_to_time =
               rowView.findViewById(R.id.FROM_TO_TIME);
        TextView todate =
                rowView.findViewById(R.id.todate);
        TextView fromtime =
                rowView.findViewById(R.id.fromtime);
        TextView totime =
                rowView.findViewById(R.id.totime);
        TextView days =
                rowView.findViewById(R.id.days);
        TextView hrs =
                rowView.findViewById(R.id.hours);
        TextView two_TextView =
                rowView.findViewById(R.id.textView_statuss);
       WorkFromHomeSetDataPoJo applicationStatusPOJO= (WorkFromHomeSetDataPoJo) getItem(position);
        //Log.e("Listview date",applicationStatusPOJO.getFromdate());

        fromdate.setText(applicationStatusPOJO.getFromdate());
        todate.setText(applicationStatusPOJO.getTodate());
       if(applicationStatusPOJO.getFromtime().equals("") && applicationStatusPOJO.getTotime().equals("")){
            from_to_time.removeAllViews();
        }else {
            fromtime.setText(applicationStatusPOJO.getFromtime());
            totime.setText(applicationStatusPOJO.getTotime());
        }
        days.setText(applicationStatusPOJO.getDays());
       hrs.setText(applicationStatusPOJO.getHrs());
        two_TextView.setText(applicationStatusPOJO.getStatus());
        return rowView;
    }
}
