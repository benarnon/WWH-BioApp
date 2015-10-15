package biclusterCreator;

/**
 * Created by user on 10/14/15.
 */
public class SampleJson {
    String date;
    Double latitude;
    Double longitude;

    public SampleJson(String date, Double latitude, Double longitude) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
