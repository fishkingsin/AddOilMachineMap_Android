package fishkingsin.com.addoilmachinemap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;

import java.util.Collection;

/**
 * Created by james on 19/4/15.
 */
public class MessageItems implements Cluster<MessageItem> {
    @Override
    public LatLng getPosition() {
        return null;
    }

    @Override
    public Collection<MessageItem> getItems() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
