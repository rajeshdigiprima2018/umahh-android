package Modal;

/**
 * Created by Dell on 30-04-2019.
 */

public class Khutba_Modal {


    String Speaker_name;

    public String getSpeaker_name() {
        return Speaker_name;
    }

    public void setSpeaker_name(String speaker_name) {
        Speaker_name = speaker_name;
    }

    String mosque_id,startDate,startTime,title,khutba_id,id;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKhutba_id() {
        return khutba_id;
    }

    public void setKhutba_id(String khutba_id) {
        this.khutba_id = khutba_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMosque_id() {
        return mosque_id;
    }

    public void setMosque_id(String mosque_id) {
        this.mosque_id = mosque_id;
    }
}
