package com.wikijourney.wikijourney;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;


public class MapActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Launch an activity with the Map layout
        setContentView(R.layout.activity_map);
        MapView map = (MapView) findViewById(R.id.map);

        // We get the intent values
        Intent intent = new Intent();
        try {
            intent = getIntent();
        }
        finally {

        }
        double nsCoord = intent.getDoubleExtra("nsCoord", 0);
        double ewCoord = intent.getDoubleExtra("ewCoord", 0);

        // These lines initialize the map settings
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(9);

        // This starts the map at the desired point
        final GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);

        // Now we add a marker using osmBonusPack
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        // And we have to use this to refresh the map
        map.invalidate();

        // We can change some properties of the marker (don't forget to refresh the map !!)
        // startMarker.setIcon(getResources().getDrawable(R.drawable.ic_logo));
        startMarker.setTitle(getString(R.string.you_are_here));
        map.invalidate();

        // Now we can also calculate and draw roads
        // First we need to choose a road manager
        RoadManager roadManager = new OSRMRoadManager();

        // Then we add some waypoints
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(startPoint);
        GeoPoint endPoint = new GeoPoint(nsCoord, ewCoord);
        waypoints.add(endPoint);

        // And we get the road between the points, we build the polyline between them
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);

        // We add the road to the map, and we refresh the letter
        map.getOverlays().add(roadOverlay);
        map.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
