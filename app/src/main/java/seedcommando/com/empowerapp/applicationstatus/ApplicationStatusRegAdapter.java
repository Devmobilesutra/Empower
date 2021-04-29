package seedcommando.com.empowerapp.applicationstatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.empowerapp.R;
import seedcommando.com.empowerapp.pojos.regularizationpoJo.statuspojo.ApplicationSetData;

/**
 * Created by commando4 on 3/28/2018.
 */

public class ApplicationStatusRegAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ApplicationSetData> mDataSource;

    public ApplicationStatusRegAdapter(Context context, ArrayList<ApplicationSetData> items) {
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
        View rowView = mInflater.inflate(R.layout.reg_status_list, parent, false);

        // Get title element
        TextView fromdate =
                rowView.findViewById(R.id.fromdate);
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
        ApplicationSetData applicationSetData= ( ApplicationSetData) getItem(position);
        //Log.e("Listview date",applicationStatusPOJO.getFromdate());

        fromdate.setText(applicationSetData.getFromDate());
        todate.setText(applicationSetData.getToDate());
        fromtime.setText(applicationSetData.getFromtime());
        totime.setText(applicationSetData.getTotime());
        days.setText(applicationSetData.getShift());
        hrs.setText(applicationSetData.getReason());
        two_TextView.setText(applicationSetData.getStatus());
        return rowView;
    }
}