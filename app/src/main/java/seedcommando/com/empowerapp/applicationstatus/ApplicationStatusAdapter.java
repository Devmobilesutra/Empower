package seedcommando.com.empowerapp.applicationstatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.empowerapp.R;

/**
 * Created by commando1 on 9/20/2017.
 */

public class ApplicationStatusAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ApplicationStatusPOJO> mDataSource;

    public ApplicationStatusAdapter(Context context, ArrayList<ApplicationStatusPOJO> items) {
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
        View rowView = mInflater.inflate(R.layout.application_status_list_member, parent, false);

        // Get title element
        TextView name =
                rowView.findViewById(R.id.textView_leave_type);
        TextView one_TextView =
                rowView.findViewById(R.id.textView_date);
        TextView two_TextView =
                rowView.findViewById(R.id.textView_statuss);
        ApplicationStatusPOJO applicationStatusPOJO= (ApplicationStatusPOJO) getItem(position);
        name.setText(applicationStatusPOJO.leavetype);
        one_TextView.setText("FROM "+applicationStatusPOJO.fromdate+" TO "+applicationStatusPOJO.todate);
        two_TextView.setText(applicationStatusPOJO.status);
        return rowView;
    }
}
