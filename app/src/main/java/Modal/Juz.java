package Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell on 22-07-2019.
 */

public class Juz {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")

    @Expose
    private List<Modal.data2> data = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return success;
    }

    public void setStatus(String status) {
        this.success = status;
    }

    public List<Modal.data2> getData() {
        return data;
    }

    public void setData(List<Modal.data2> data) {
        this.data = data;
    }
}
