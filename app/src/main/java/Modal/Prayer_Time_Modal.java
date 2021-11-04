package Modal;

/**
 * Created by Dell on 30-04-2019.
 */

public class Prayer_Time_Modal  {

    String day,day_aerobic,time,mosque_id,status,prayer_id;
int Type = 0 ; // 0,1,2

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay_aerobic() {
        return day_aerobic;
    }

    public void setDay_aerobic(String day_aerobic) {
        this.day_aerobic = day_aerobic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMosque_id() {
        return mosque_id;
    }

    public void setMosque_id(String mosque_id) {
        this.mosque_id = mosque_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrayer_id() {
        return prayer_id;
    }

    public void setPrayer_id(String prayer_id) {
        this.prayer_id = prayer_id;
    }
}
