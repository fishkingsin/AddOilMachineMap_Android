package fishkingsin.com.addoilmachinemap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.loopj.android.http.*;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapsActivity extends FragmentActivity implements ClusterManager.OnClusterItemClickListener<MessageItem>,
        ClusterManager.OnClusterClickListener, GoogleMap.OnMarkerClickListener
 {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    static final String TAG_MAIN_VIEW = "MainActivity";
    static final String kAPIScheme_HTTP     = "http://";
    static final String kAPIScheme_HTTPS    = "https://";
    static final String kDevServerURL       = "xex.com.hk";
    static final String kGMpaURL       = "/projects/basara/js/gmap.json";
    static final String kAPIScheme = kAPIScheme_HTTP;
    static final String kBaseURL = kDevServerURL;
    private ClusterManager<MessageItem> mClusterManager;
    JSONObject allMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */


    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return mMap;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                setUpMap();

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(22.2796095,114.1661851));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        getMap().moveCamera(center);
        getMap().animateCamera(zoom);

        mClusterManager = new ClusterManager<MessageItem>(this, getMap());


        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(this);

        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(this);
        AsyncHttpClient client = new AsyncHttpClient();
        String urlPath = kAPIScheme+kBaseURL+kGMpaURL;
        Log.d(TAG_MAIN_VIEW, "urlPath : " + urlPath);
        client.get(urlPath, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG_MAIN_VIEW, "onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                // called when response HTTP status is "200 OK"
                if (statusCode == 200) {
                    String byteStr = new String(response);
                    Log.d(TAG_MAIN_VIEW, byteStr);
                    try {

                        allMessage = new JSONObject(byteStr);
                        JSONArray jsonArray = allMessage.getJSONArray("messages");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = (JSONObject) jsonArray.get(i);
                                LatLng position = new LatLng(o.getDouble("lat"), o.getDouble("lng"));
                                MessageItem offsetItem = new MessageItem(position, o.getString("message"), o.getString("name"), o.getString("location"));
                                mClusterManager.addItem(offsetItem);
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(TAG_MAIN_VIEW, e.toString());
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }

    public boolean onClusterItemClick(MessageItem messageItem) {

        return false;
    }

     @Override
     public boolean onClusterClick(Cluster cluster) {


         return false;
     }

     @Override
     public boolean onMarkerClick(Marker marker) {

         return false;
     }

     private class MessageRenderer extends DefaultClusterRenderer<MessageItem> {
         private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
         private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
         private final ImageView mImageView;
         private final ImageView mClusterImageView;
         private final int mDimension;

         public MessageRenderer() {
             super(getApplicationContext(), getMap(), mClusterManager);

             View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
             mClusterIconGenerator.setContentView(multiProfile);
             mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

             mImageView = new ImageView(getApplicationContext());
             mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
             mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
             int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
             mImageView.setPadding(padding, padding, padding, padding);
             mIconGenerator.setContentView(mImageView);
         }

         @Override
         protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
             // Draw a single person.
             // Set the info window to show their name.
             mImageView.setImageResource(person.profilePhoto);
             Bitmap icon = mIconGenerator.makeIcon();
             markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
         }

         @Override
         protected void onBeforeClusterRendered(Cluster<MessageItem> cluster, MarkerOptions markerOptions) {
             // Draw multiple people.
             // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
             List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
             int width = mDimension;
             int height = mDimension;

             for (MessageItem p : cluster.getItems()) {
                 // Draw 4 at most.
                 if (profilePhotos.size() == 4) break;
//                 Drawable drawable = getResources().getDrawable(p.profilePhoto);
//                 drawable.setBounds(0, 0, width, height);
//                 profilePhotos.add(drawable);
             }
             MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
             multiDrawable.setBounds(0, 0, width, height);

             mClusterImageView.setImageDrawable(multiDrawable);
             Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
             markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
         }

         @Override
         protected boolean shouldRenderAsCluster(Cluster cluster) {
             // Always render clusters.
             return cluster.getSize() > 1;
         }
     }
 }
