package com.example.herecomfh;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapOverlay;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;

public class MainActivity extends AppCompatActivity {
    private MapViewLite mapView;
    private static final String TAG = MainActivity.class.getCanonicalName();
    private Boolean maploaded = false;

//TODO: Events laden von der API und für jedes ein overlay erstellen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        Button switchBtn;

        loadMapScene();
        generateOverlay(mapView.getCamera().getTarget());

        new Thread(new loadCurrentLocationRunnable(new PositionProvider(this))).start();

    }

    private void loadMapScene(){
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
               if(errorCode == null){
                   mapView.getCamera().setTarget(new GeoCoordinates(48.210033, 16.363449));
                   mapView.getCamera().setZoomLevel(14);
                   maploaded = true;
               } else {
                   Log.d(TAG, "onLoadScene failed" + errorCode.toString());
               }
            }
        });
    }

    class loadCurrentLocationRunnable implements Runnable {

        PositionProvider positionProvider;

        public loadCurrentLocationRunnable(PositionProvider positionProvider) {
            this.positionProvider = positionProvider;
        }

        @Override
        public void run() {
            while(!maploaded){
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        positionProvider.startLocating(new PositionProvider.PlatformLocationListener() {// Platform loactionlistener wird hier definiert weil wir die mapview brauchen
                            @Override
                            public void onLocationUpdate(Location location) {//wird hier definiert weil wir die mapview brauchen
                                generateOverlay(location);
                                mapView.getCamera().setTarget(new GeoCoordinates(location.getLatitude(), location.getLongitude()));
                                mapView.getCamera().setZoomLevel(14);
                            }
                        });
                    }
                });
            }
        }
    }

    private void generateOverlay(Location location) {
        TextView textView = new TextView(this);
        textView.setText("Hier ähm ei!");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.design_default_color_on_primary);
        linearLayout.setPadding(10,10,10,10);

        linearLayout.addView(textView);

        GeoCoordinates geoCoordinates = new GeoCoordinates(location.getLatitude(), location.getLongitude());
        MapOverlay<LinearLayout> mapOverlay = new MapOverlay<>(linearLayout, geoCoordinates);
        mapView.addMapOverlay(mapOverlay);
    }

    private void generateOverlay(GeoCoordinates location) {
        TextView textView = new TextView(this);
        textView.setText("Hier ähm ei!");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.design_default_color_on_primary);
        linearLayout.setPadding(10,10,10,10);

        linearLayout.addView(textView);

        MapOverlay<LinearLayout> mapOverlay = new MapOverlay<>(linearLayout, location);
        mapView.addMapOverlay(mapOverlay);
    }
}