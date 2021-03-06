package seedcommando.com.empowerapp.pojos.shiftallocation.shifts;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import seedcommando.com.empowerapp.pojos.shiftallocation.*;
import seedcommando.com.empowerapp.pojos.shiftallocation.Data;

/**
 * Created by commando4 on 8/16/2018.
 */

public class ShiftsData {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    private ArrayList<seedcommando.com.empowerapp.pojos.shiftallocation.shifts.Data> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<seedcommando.com.empowerapp.pojos.shiftallocation.shifts.Data> getData() {
        return data;
    }

    public void setData(ArrayList<seedcommando.com.empowerapp.pojos.shiftallocation.shifts.Data> data) {
        this.data = data;
    }
}
