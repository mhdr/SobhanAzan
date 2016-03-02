package PlayAzan;

/**
 * Created by Mahmood on 3/2/2016.
 */
public class Azan {
    private int AzanId;
    private String AzanDateTime;
    private int AzanType;

    public Azan(int azanId,String azanDateTime,int azanType)
    {
        this.AzanId=azanId;
        this.AzanDateTime=azanDateTime;
        this.AzanType=azanType;
    }

    public int getAzanId() {
        return AzanId;
    }

    public void setAzanId(int azanId) {
        AzanId = azanId;
    }

    public String getAzanDateTime() {
        return AzanDateTime;
    }

    public void setAzanDateTime(String azanDateTime) {
        AzanDateTime = azanDateTime;
    }

    public int getAzanType() {
        return AzanType;
    }

    public void setAzanType(int azanType) {
        AzanType = azanType;
    }

    @Override
    public String toString() {
        return "Azan{" +
                "AzanId=" + AzanId +
                ", AzanDateTime='" + AzanDateTime + '\'' +
                ", AzanType=" + AzanType +
                '}';
    }
}
