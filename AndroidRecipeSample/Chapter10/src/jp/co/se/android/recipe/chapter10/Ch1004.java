package jp.co.se.android.recipe.chapter10;

import java.util.ArrayList;
import java.util.List;

import jp.co.se.android.recipe.chapter10.ParseJson.Leg;
import jp.co.se.android.recipe.chapter10.ParseJson.Route;
import jp.co.se.android.recipe.chapter10.ParseJson.Step;
import jp.co.se.android.recipe.chapter10.RequestDirectionsTask.RequestDirectionsTaskCallback;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Ch1004 extends FragmentActivity {
    public static final String TRAVEL_MODE = "driving";// default

    GoogleMap mGmap;
    ArrayList<LatLng> mMarkerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1004_main);

        // ��l��
        mMarkerPoints = new ArrayList<LatLng>();

        // ����GoogleMap�����
        SupportMapFragment mapfragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mGmap = mapfragment.getMap();

        // �]�w��l��m
        LatLng location = new LatLng(34.802556297454004, 135.53884506225586);
        mGmap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
        mGmap.setMyLocationEnabled(true);

        // �]�w�I���a�Ϯɪ��ƥ�
        mGmap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // �Y�w�I���F�⦸�h�M�����|
                if (mMarkerPoints.size() >= 2) {
                    mMarkerPoints.clear();
                    mGmap.clear();
                }

                // �O�����I�����y��
                mMarkerPoints.add(point);

                // �b���I�����I�s�W�е�
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                if (mMarkerPoints.size() == 1) {
                    markerOptions.title("A");
                } else if (mMarkerPoints.size() == 2) {
                    markerOptions.title("B");
                }
                mGmap.addMarker(markerOptions);

                // �Y�O�ĤG���I���ɫh�}�l�j�M���u
                if (mMarkerPoints.size() >= 2) {
                    LatLng origin = mMarkerPoints.get(0);
                    LatLng dest = mMarkerPoints.get(1);
                    routeSearch(origin, dest);
                }
            }
        });

        mGmap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                if (title.equals("A")) {
                    marker.setSnippet("");
                } else if (title.equals("B")) {
                    marker.setSnippet("");
                }

                return false;
            }
        });
    }

    /**
     * �j�M���|
     * 
     * @param origin
     * @param dest
     */
    private void routeSearch(LatLng origin, LatLng dest) {
        // ��ܶi�׹�ܮ�
        ProgressDialogFragment.newInstance("�˯���...").show(
                getSupportFragmentManager(), ProgressDialogFragment.FRG_TAG);

        // �i��q�T
        RequestDirectionsTask task = new RequestDirectionsTask(this, origin,
                dest, TRAVEL_MODE);
        task.setRequestDirectionsTaskCallback(
                new RequestDirectionsTaskCallback() {

                    @Override
                    public void onSucceed(List<Route> routes) {
                        // �����i�׹�ܮ�
                        FragmentManager fm = getSupportFragmentManager();
                        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fm
                                .findFragmentByTag(ProgressDialogFragment.FRG_TAG);
                        if (progressDialogFragment != null) {
                            progressDialogFragment.dismissAllowingStateLoss();
                        }

                        // �}�lø�s���u
                        if (routes.size() > 0) {
                            PolylineOptions lineOptions = new PolylineOptions();
                            for (Route route : routes) {
                                for (Leg leg : route.getLegs()) {
                                    for (Step step : leg.getSteps()) {
                                        lineOptions.addAll(step
                                                .getPolylinePoints());
                                        lineOptions.width(10);
                                        lineOptions.color(0x550000ff);
                                    }
                                }
                            }
                            // ø��
                            mGmap.addPolyline(lineOptions);
                        } else {
                            mGmap.clear();
                            Toast.makeText(Ch1004.this, "�L�k���o���u��T",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        Toast.makeText(Ch1004.this,
                                "���u�j�M���o�ͤF���~(" + e + ")",
                                Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }
}
