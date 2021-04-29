package seedcommando.com.empowerapp.applicationstatus;

/**
 * Created by commando1 on 9/20/2017.
 */

public class ApplicationStatusPOJO {

    String leavetype,status,fromdate,todate;

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    @Override
    public String toString() {
        return "ApplicationStatusPOJO{" +
                "leavetype='" + leavetype + '\'' +
                ", status='" + status + '\'' +
                ", fromdate='" + fromdate + '\'' +
                ", todate='" + todate + '\'' +
                '}';
    }
}
