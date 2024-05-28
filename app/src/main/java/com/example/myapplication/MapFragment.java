package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSION_REQUEST_CODE = 1000;

    private MapView mMap;
    private NaverMap nMap;
    private FusedLocationSource locationSource;
    MainActivity mainActivity;
    private LinearLayout lay;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NaverMapSdk.getInstance(mainActivity).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("ac0rlcv4ex")
        );
        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // request code와 권한 획득 여부 확인
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                nMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        nMap = naverMap;
        nMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        NaverMapOptions options = new NaverMapOptions().locationButtonEnabled(true);
        nMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        ActivityCompat.requestPermissions(mainActivity, mainActivity.PERMISSIONS, PERMISSION_REQUEST_CODE);

        Marker marker11 = new Marker(), marker12 = new Marker(), marker13 = new Marker(), marker14 = new Marker();
        Marker marker21 = new Marker(), marker22 = new Marker(), marker23 = new Marker(), marker24 = new Marker();
        Marker marker31 = new Marker(), marker32 = new Marker(), marker33 = new Marker(), marker34 = new Marker();
        Marker marker41 = new Marker(), marker42 = new Marker(), marker43 = new Marker(), marker44 = new Marker();
        Marker marker51 = new Marker(), marker52 = new Marker(), marker53 = new Marker(), marker54 = new Marker();

        marker11.setPosition(new LatLng(36.629303542675466,127.45645127961879 ));
        marker12.setPosition(new LatLng(36.62982977003304,127.45551802333405 ));
        marker13.setPosition(new LatLng(36.62909285685874,127.45499100698116 ));
        marker14.setPosition(new LatLng(36.62838106021,127.45319239979781  ));
        marker11.setCaptionText("1-1");
        marker12.setCaptionText("1-2");
        marker13.setCaptionText("1-3");
        marker14.setCaptionText("1-4");

        marker21.setPosition(new LatLng(36.627424936130694,127.45579734749293 ));
        marker22.setPosition(new LatLng(36.627873719624155,127.45567421370473 ));
        marker23.setPosition(new LatLng(36.62854645671223,127.4547194695424 ));
        marker24.setPosition(new LatLng(36.62851119699967,127.45392267141636 ));
        marker21.setCaptionText("2-1");
        marker22.setCaptionText("2-2");
        marker23.setCaptionText("2-3");
        marker24.setCaptionText("2-4");

        marker31.setPosition(new LatLng(36.62770082799416,127.4531604668003 ));
        marker32.setPosition(new LatLng(36.627231848479425,127.45444622077437 ));
        marker33.setPosition(new LatLng(36.62661131827989,127.45411835914746 ));
        marker34.setPosition(new LatLng(36.6261505156698,127.45443987243384 ));
        marker31.setCaptionText("3-1");
        marker32.setCaptionText("3-2");
        marker33.setCaptionText("3-3");
        marker34.setCaptionText("3-4");

        marker41.setPosition(new LatLng(36.627667414652,127.4583618135315 ));
        marker42.setPosition(new LatLng(36.62846483246125,127.45838330600718 ));
        marker43.setPosition(new LatLng(36.62908409312564,127.45728012316792 ));
        marker44.setPosition(new LatLng(36.62829158823883,127.45715245902252 ));
        marker41.setCaptionText("4-1");
        marker42.setCaptionText("4-2");
        marker43.setCaptionText("4-3");
        marker44.setCaptionText("4-4");

        marker51.setPosition(new LatLng(36.627504788982925,127.4567117890889 ));
        marker52.setPosition(new LatLng(36.6270701334843,127.45726263524358 ));
        marker53.setPosition(new LatLng(36.6260172925192,127.45687630056167 ));
        marker54.setPosition(new LatLng(36.626156740720084,127.45575913527546 ));
        marker51.setCaptionText("5-1");
        marker52.setCaptionText("5-2");
        marker53.setCaptionText("5-3");
        marker54.setCaptionText("5-4");

        marker11.setMap(nMap);
        marker12.setMap(nMap);
        marker13.setMap(nMap);
        marker14.setMap(nMap);

        marker21.setMap(nMap);
        marker22.setMap(nMap);
        marker23.setMap(nMap);
        marker24.setMap(nMap);

        marker31.setMap(nMap);
        marker32.setMap(nMap);
        marker33.setMap(nMap);
        marker34.setMap(nMap);

        marker41.setMap(nMap);
        marker42.setMap(nMap);
        marker43.setMap(nMap);
        marker44.setMap(nMap);

        marker51.setMap(nMap);
        marker52.setMap(nMap);
        marker53.setMap(nMap);
        marker54.setMap(nMap);

    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.map_layout, container, false);
        mMap = rootView.findViewById(R.id.map_view);
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        mMap.onStart();
    }

    @Override
    public void onStop () {
        super.onStop();
        mMap.onStop();

    }

    @Override
    public void onSaveInstanceState (@Nullable Bundle outState){
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mainActivity.viewPager2.setUserInputEnabled(false);
        mMap.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMap.onLowMemory();
    }

}
