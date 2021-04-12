package seedcommando.com.empowerapp.applicationstatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.empowerapp.R;
import seedcommando.com.empowerapp.pojos.compoff.CompOffStatusSetData;

/**
 * Created by commando4 on 4/12/2018.
 */

public class ApplicationStatusCompoffAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CompOffStatusSetData> mDataSource;

    public ApplicationStatusCompoffAdapter(Context context, ArrayList<CompOffStatusSetData> items) {
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
        View rowView = mInflater.inflate(R.layout.compoff_status, parent, false);

        // Get title element
        TextView fromdate =
                rowView.findViewById(R.id.fromdate);
        TextView todate =
                rowView.findViewById(R.id.todate);

        TextView days =
                rowView.findViewById(R.id.days);
        TextView hrs =
                rowView.findViewById(R.id.hours);
        TextView two_TextView =
                rowView.findViewById(R.id.textView_statuss);
       CompOffStatusSetData applicationSetData= ( CompOffStatusSetData) getItem(position);
        //Log.e("Listview date",applicationStatusPOJO.getFromdate());

        fromdate.setText(applicationSetData.getTakenCompOffDate());
        todate.setText(applicationSetData.getTakenAgainst());

        days.setText(applicationSetData.getReasonDescription());
        hrs.setText(applicationSetData.getNoOfCompOffDays());
        two_TextView.setText(applicationSetData.getApplicationStatus());
        return rowView;
    }
}
