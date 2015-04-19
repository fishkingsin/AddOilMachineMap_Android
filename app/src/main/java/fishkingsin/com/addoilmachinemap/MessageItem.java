package fishkingsin.com.addoilmachinemap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
/**
 * Created by james on 19/4/15.
 */
public class MessageItem implements ClusterItem {
    private final LatLng mPosition;
    private String mMessage;
    private String mName;
    private String mLocation;
    public MessageItem(LatLng position ,String message, String name, String location) {

        mPosition = position;
        mMessage = message;
        mName = name;
        mLocation = location;

    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getName() {
        return mName;
    }


}
