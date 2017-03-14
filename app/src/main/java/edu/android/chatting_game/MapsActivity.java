package edu.android.chatting_game;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button mapBtn;
    private EditText Address;
    private Geocoder geocoder;
    private ImageButton imageButton;
    public static final String EXTRA_MAP="map";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         mapBtn = (Button)findViewById(R.id.mapBtn);
         Address = (EditText)findViewById(R.id.editAddress);
         geocoder = new Geocoder(this);
        imageButton=(ImageButton)findViewById(R.id.imageButton);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> list = null;
                String str = Address.getText().toString();
                try {
                    list = geocoder.getFromLocationName(str, 15);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    if (list.size() == 0) {
                        Address.setText("해당 주소가 없습니다.");
                    } else {
                        Address addr = list.get(0);
                        double lat = addr.getLatitude();
                        double lon = addr.getLongitude();

                        final LatLng mM=new LatLng(lat, lon);
                        CameraUpdate camera= CameraUpdateFactory.newLatLngZoom(mM,16);
                        mMap.animateCamera(camera);
                    }
                }
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
//              String s="latlng"+latLng;
//                Address.setText(s);
                final MarkerOptions options=new MarkerOptions();
                options.position(latLng);
                options.title("선택장소");
                mMap.addMarker(options);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapsActivity.this,ChatRoomActivity.class);
                        String msg=""+latLng;
                        intent.putExtra(EXTRA_MAP,msg);
                        startActivity(intent);
                    }
                });
            }
        });

    }

}
