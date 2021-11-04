package Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell on 22-07-2019.
 */

public class data2 {


    @SerializedName("name")
    @Expose
    private Integer name;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("deletedAt")
    @Expose
    private String deletedAt;

    @SerializedName("updatedAt")
    @Expose
    private Integer updatedAt;

    @SerializedName("index")
    @Expose
    private String index;

    @SerializedName("juz_id")
    @Expose
    private String juz_id;

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getJuz_id() {
        return juz_id;
    }

    public void setJuz_id(String juz_id) {
        this.juz_id = juz_id;
    }
}
