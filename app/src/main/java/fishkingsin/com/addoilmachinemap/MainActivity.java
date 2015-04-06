package fishkingsin.com.addoilmachinemap;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
//import com.loopj.android;

public class MainActivity extends ActionBarActivity {
    static final String kAPIScheme_HTTP     = "http://";
    static final String kAPIScheme_HTTPS    = "https://";
    static final String kDevServerURL       = "xex.com.hk";
    static final String kGMpaURL       = "/projects/basara/js/gmap.json";
    static final String kAPIScheme = kAPIScheme_HTTPS;
    static final String kBaseURL = kDevServerURL;
    Location original;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapView mapView = new MapView(this);
        mapView.setAccessToken("pk.eyJ1IjoiZmlza2luZ3NpbiIsImEiOiJJOTIyM3BnIn0.gdobaG3Pzh-BomT1-8jPmw");
        mapView.setTileSource(new MapboxTileLayer("fiskingsin.lj4gno8f"));
        ILatLng center = new ILatLng() {
            @Override
            public double getLatitude() {
                return 22.2796095;
            }

            @Override
            public double getLongitude() {
                return 114.1661851;
            }

            @Override
            public double getAltitude() {
                return 0;
            }
        };
        mapView.setCenter(center);
        this.setContentView(mapView);

//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("http://www.google.com", new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onStart() {
//                // called before request is started
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                // called when response HTTP status is "200 OK"
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//            }
//
//            @Override
//            public void onRetry(int retryNo) {
//                // called when request is retried
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
