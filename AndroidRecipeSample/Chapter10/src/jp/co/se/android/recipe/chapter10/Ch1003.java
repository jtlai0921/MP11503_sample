package jp.co.se.android.recipe.chapter10;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class Ch1003 extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1001_main);
        SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));

        final GoogleMap gMap = fragment.getMap();
        gMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng point) {
                // 以經緯度為基準來取得地址
                String address = getAddressFromPoint(point);
                Toast.makeText(Ch1003.this, address, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 從經緯度來取得現在地點.
     * 
     * @param p
     * @return
     */
    private String getAddressFromPoint(LatLng p) {
        String addressValue = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(p.latitude,
                    p.longitude, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= address.getMaxAddressLineIndex(); i++) {
                    String addressLine = address.getAddressLine(i);
                    sb.append(addressLine);
                }
                addressValue = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressValue;
    }
}
