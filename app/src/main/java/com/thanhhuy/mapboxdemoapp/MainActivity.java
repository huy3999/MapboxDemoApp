package com.thanhhuy.mapboxdemoapp;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

// classes needed to initialize map
// classes needed to add the location component
// classes needed to add a marker
// classes to calculate a route
// classes needed to launch navigation UI


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button btnNavigation, btnMyLocation;

    private Button chooseCityButton;
    private EditText latEditText;
    private EditText longEditText;
    private TextView geocodeResultTextView;
    private MapboxMap map;
    private GeoFire geoFire;
    private Map<String,Marker> markers;

    private Handler handler;
    private Runnable runnable;
    private final String ID = "bike_location";
    private final String URL_GET_DATA= "https://mapboxdemo-7716b.firebaseio.com/.json";
    //private final String URL_GET_DATA= "https://svnckh2k19.firebaseio.com/.json";

    DatabaseReference reference;
    public double lat = 0;
    public double lng = 0;
    public LatLng pointCoor;

    ConstraintLayout constraintInfo;
    ConstraintLayout constraintBubble;
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;
    View customView;

    String timeStamp;
    TextView txtTime;
    Button btnGetRoute;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        getData();

    }
    public void getData() {
        reference = FirebaseDatabase.getInstance().getReference().child("Devices").child("0020CD4E5A48911B");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    String lati = dataSnapshot.child("lat").getValue().toString();
                    String lngi = dataSnapshot.child("lon").getValue().toString();
                    timeStamp = dataSnapshot.child("timestamp").getValue().toString();

                    try {
                        lat = Double.parseDouble(lati);
                        lng = Double.parseDouble(lngi);
                    } catch (NumberFormatException ex) { // handle your exception

                    }


                    pointCoor = new LatLng(lat, lng);
                    //getRouteToBike(point);

                   // getRouteToBike(point);


                    Log.d("firebase", "get data successful");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("firebase","unsuccessful");
            }
        });


    }

    private void showMyCurrentLocation(){
         mapboxMap.setStyle(getString(R.string.navigation_guidance_custom), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                getData();
                enableLocationComponent(style);
                symbolLayer();
                //addDestinationIconSymbolLayer(style);
                //addBikeIconSymbolLayer(style);
                setCoordinateEditTexts(pointCoor);
                txtTime.setText(String.valueOf(timeStamp));
                Point destinationPoint = Point.fromLngLat(pointCoor.getLongitude(), pointCoor.getLatitude());
                Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                        locationComponent.getLastKnownLocation().getLatitude());


                Timber.e("Error get route " + lat);

                getRoute(originPoint,destinationPoint);
                btnNavigation.setEnabled(true);
                btnNavigation.setBackgroundResource(R.color.mapboxGreen);
                markerViewManager = new MarkerViewManager(mapView, mapboxMap);
//
//// Use an XML layout to create a View object
//
                customView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.marker_view_bubble, null);
                customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                // Set the View's TextViews with content
                TextView txtMarker = customView.findViewById(R.id.txtMarker);
//                titleTextView.setText(R.string.draw_marker_options_title);
                txtMarker.setText("My device");

                if(pointCoor!=null) {
                    markerView = new MarkerView(new LatLng(pointCoor), customView);
                    markerViewManager.addMarker(markerView);
                }
            }
        });


    }
    public void onClickConstraint(View customView){
        constraintInfo.setVisibility(View.VISIBLE);
        btnMyLocation.setVisibility(View.INVISIBLE);
        btnNavigation.setVisibility(View.INVISIBLE);
        //btnGetRoute.setEnabled(true);

    }

    private void initTextViews() {
        latEditText = findViewById(R.id.geocode_latitude_editText);
        longEditText = findViewById(R.id.geocode_longitude_editText);
//        geocodeResultTextView = findViewById(R.id.geocode_result_message);
        constraintInfo = findViewById(R.id.constraintInfo);
        //constraintBubble = findViewById(R.id.constraintBubble);
        txtTime = findViewById(R.id.txtTime);

    }

    private void initButtons() {
//        final Button mapCenterButton = findViewById(R.id.map_center_button);
//        Button startGeocodeButton = findViewById(R.id.start_geocode_button);
//        chooseCityButton = findViewById(R.id.choose_city_spinner_button);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnGetRoute = findViewById(R.id.btnMyLocation);
        //constraintBubble = findViewById(R.id.constraintBubble);
//        startGeocodeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//// Make sure the EditTexts aren't empty
//
//
//
//                if (TextUtils.isEmpty(latEditText.getText().toString())) {
//                    latEditText.setError(getString(R.string.fill_in_a_value));
//                } else if (TextUtils.isEmpty(longEditText.getText().toString())) {
//                    longEditText.setError(getString(R.string.fill_in_a_value));
//                } else {
//                    if (latCoordinateIsValid(Double.valueOf(latEditText.getText().toString()))
//                            && longCoordinateIsValid(Double.valueOf(longEditText.getText().toString()))) {
//// Make a geocoding search with the values inputted into the EditTexts
//                        makeGeocodeSearch(new LatLng(Double.valueOf(latEditText.getText().toString()),
//                                Double.valueOf(longEditText.getText().toString())));
//
//                    }
//
//                    else {
//                        Toast.makeText(MainActivity.this, R.string.make_valid_lat, Toast.LENGTH_LONG).show();
//                    }
//
//                }
//            }
//        });
//
//        chooseCityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCityListMenu();
//            }
//        });
//        mapCenterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//// Get the map's target
//                LatLng target = mapboxMap.getCameraPosition().target;
//
//// Fill the coordinate EditTexts with the target's coordinates
//                setCoordinateEditTexts(target);
//
//// Make a geocoding search with the target's coordinates
//                makeGeocodeSearch(target);
//
//
//            }
//        });
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyCurrentLocation();
            }
        });

    }

    private boolean latCoordinateIsValid(double value) {
        return value >= -90 && value <= 90;
    }

    private boolean longCoordinateIsValid(double value) {
        return value >= -180 && value <= 180;
    }

    private void setCoordinateEditTexts(LatLng latLng) {
        latEditText.setText(String.valueOf(latLng.getLatitude()));
        longEditText.setText(String.valueOf(latLng.getLongitude()));

    }
    private void getRouteButtonEvent(){
        btnGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapboxMap.setStyle(getString(R.string.navigation_guidance_custom), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        getData();
                        enableLocationComponent(style);

                        Point destinationPoint = Point.fromLngLat(pointCoor.getLongitude(), pointCoor.getLatitude());
                        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                                locationComponent.getLastKnownLocation().getLatitude());


                        Timber.e("Error get route " + lat);

                        getRoute(originPoint,destinationPoint);
                        btnNavigation.setEnabled(true);
                        btnNavigation.setBackgroundResource(R.color.mapboxGreen);
                    }
                });
            }
        });

    }

//    private void showCityListMenu() {
//        List<String> modes = new ArrayList<>();
//        modes.add("Vancouver");
//        modes.add("Helsinki");
//        modes.add("Lima");
//        modes.add("Osaka");
//        ArrayAdapter<String> profileAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, modes);
//        final ListPopupWindow listPopup = new ListPopupWindow(this);
//        listPopup.setAdapter(profileAdapter);
//        listPopup.setAnchorView(chooseCityButton);
//        listPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long longg) {
//                LatLng cityLatLng = new LatLng();
//                if (position == 0) {
//// Vancouver
//                    cityLatLng = new LatLng(49.2827, -123.1207);
//                    setCoordinateEditTexts(cityLatLng);
//                } else if (position == 1) {
//// Helsinki
//                    cityLatLng = new LatLng(60.1698, 24.938);
//                    setCoordinateEditTexts(cityLatLng);
//                } else if (position == 2) {
//// Lima
//                    cityLatLng = new LatLng(-12.0463, -77.0427);
//                    setCoordinateEditTexts(cityLatLng);
//                } else if (position == 3) {
//// Osaka
//                    cityLatLng = new LatLng(34.693, 135.5021);
//                    setCoordinateEditTexts(cityLatLng);
//                }
//                animateCameraToNewPosition(cityLatLng);
//                makeGeocodeSearch(cityLatLng);
//                listPopup.dismiss();
//            }
//        });
//        listPopup.show();
//    }

//    private void makeGeocodeSearch(final LatLng latLng) {
//        try {
//// Build a Mapbox geocoding request
//            MapboxGeocoding client = MapboxGeocoding.builder()
//                    .accessToken(getString(R.string.access_token))
//                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
//                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
//                    .mode(GeocodingCriteria.MODE_PLACES)
//                    .build();
//            client.enqueueCall(new Callback<GeocodingResponse>() {
//                @Override
//                public void onResponse(Call<GeocodingResponse> call,
//                                       Response<GeocodingResponse> response) {
//
//                    if (response.body() != null) {
//                        List<CarmenFeature> results = response.body().features();
//                        if (results.size() > 0) {
//
//// Get the first Feature from the successful geocoding response
//                            CarmenFeature feature = results.get(0);
//                            geocodeResultTextView.setText(feature.toString());
//                            animateCameraToNewPosition(latLng);
//                            //addMarker();
//
//
//
//                        } else {
//                            Toast.makeText(MainActivity.this, R.string.no_results,Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
//                    Timber.e("Geocoding Failure: " + throwable.getMessage());
//                }
//            });
//        } catch (ServicesException servicesException) {
//            Timber.e("Error geocoding: " + servicesException.toString());
//            servicesException.printStackTrace();
//        }
//    }

    private void animateCameraToNewPosition(LatLng latLng) {
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(13)
                        .build()), 1500);


        //

    }





    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        //symbolLayer();

//        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
//        symbolLayerIconFeatureList.add(Feature.fromGeometry(
//                Point.fromLngLat(pointCoor.getLongitude(),pointCoor.getLatitude())));
//        symbolLayerIconFeatureList.add(Feature.fromGeometry(
//                Point.fromLngLat(-54.14164, -33.981818)));
//        symbolLayerIconFeatureList.add(Feature.fromGeometry(
//                Point.fromLngLat(-56.990533, -30.583266)));
//
//        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/huy3999/ck1g24eng30p01con8j8r5bdb")
//
//// Add the SymbolLayer icon image to the map style
//                .withImage("bike-icon-id", BitmapFactory.decodeResource(
//                        MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))
//
//// Adding a GeoJson source for the SymbolLayer icons.
//                .withSource(new GeoJsonSource("bike-source-id",
//                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
//
//// Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
//// marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
//// the coordinate point. This is offset is not always needed and is dependent on the image
//// that you use for the SymbolLayer icon.
//                .withLayer(new SymbolLayer("bike-layer-id", "bike-source-id")
//                        .withProperties(PropertyFactory.iconImage("bike-icon-id"),
//                                iconAllowOverlap(true),
//                                iconOffset(new Float[] {0f, -9f}))
//                ), new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//
//
//            }
//        });

        mapboxMap.setStyle(getString(R.string.navigation_guidance_custom), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
//

// Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.
    getData();
    enableLocationComponent(style);
    initTextViews();
    initButtons();
    //getRouteButtonEvent();
    //symbolLayer();
    //addDestinationIconSymbolLayer(style);
    //addBikeIconSymbolLayer(style);
    mapboxMap.addOnMapClickListener(MainActivity.this);
    btnNavigation = findViewById(R.id.startButton);
                btnNavigation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean simulateRoute = true;
            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(simulateRoute)
                    .build();
// Call this method with Context from within an Activity
            NavigationLauncher.startNavigation(MainActivity.this, options);
        }
    });
                constraintInfo.setVisibility(View.INVISIBLE);
                //btnMyLocation.setEnabled(true);
                //btnGetRoute.setEnabled(false);

     //Initialize the MarkerViewManager
//    markerViewManager = new MarkerViewManager(mapView, mapboxMap);
//
//// Use an XML layout to create a View object
//
//    customView = LayoutInflater.from(MainActivity.this).inflate(
//            R.layout.marker_view_bubble, null);
//                customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//
//    // Set the View's TextViews with content
//    TextView txtMarker = customView.findViewById(R.id.txtMarker);
////                titleTextView.setText(R.string.draw_marker_options_title);
//                txtMarker.setText("My device");
//
//                if(pointCoor!=null) {
//        markerView = new MarkerView(new LatLng(pointCoor), customView);
//        markerViewManager.addMarker(markerView);
//    }
//    btnGetRoute.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            getRouteButtonEvent();
//        }
//    });
//
            }
        });
    }
    public void symbolLayer(){
        getData();
        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(pointCoor.getLongitude(),pointCoor.getLatitude())));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-54.14164, -33.981818)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-56.990533, -30.583266)));



        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/huy3999/ck1g24eng30p01con8j8r5bdb")

//
// Add the SymbolLayer icon image to the map style
                .withImage("bike-icon-id", BitmapFactory.decodeResource(
                        MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))

// Adding a GeoJson source for the SymbolLayer icons.
                .withSource(new GeoJsonSource("bike-source-id",
                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

// Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
// marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
// the coordinate point. This is offset is not always needed and is dependent on the image
// that you use for the SymbolLayer icon.
                .withLayer(new SymbolLayer("bike-layer-id", "bike-source-id")
                        .withProperties(PropertyFactory.iconImage("bike-icon-id"),
                                iconAllowOverlap(true),
                                iconOffset(new Float[] {0f, -9f}))));
//        customView = LayoutInflater.from(MainActivity.this).inflate(
//                R.layout.marker_view_bubble, null);
//        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//
//        // Set the View's TextViews with content
//        TextView txtMarker = customView.findViewById(R.id.txtMarker);
////                titleTextView.setText(R.string.draw_marker_options_title);
//        txtMarker.setText("My device");
//
//        if(pointCoor!=null) {
//            markerView = new MarkerView(new LatLng(pointCoor), customView);
//            markerViewManager.addMarker(markerView);
//        }
    }




//    private  void addBikeIconSymbolLayer(@NonNull Style bikeStyle){
//
//        bikeStyle.addImage("bike-icon-id",
//                BitmapFactory.decodeResource(this.getResources(),R.drawable.mapbox_marker_icon_default));
//        GeoJsonSource geoJsonSource = null;
//        try {
//            bikeStyle.addSource(new GeoJsonSource("bike-source-id",new URL(URL_GET_DATA)));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        SymbolLayer countLayer = new SymbolLayer("bike-symbol-layer-id", "bike-source-id");
//
//        countLayer.withProperties(
//                iconImage("bike-icon-id"),
//                iconAllowOverlap(true),
//                iconIgnorePlacement(true)
//        );
//        bikeStyle.addLayer(countLayer);
//        handler = new Handler();
//        runnable = new RefreshData(bikeStyle,handler);
//        handler.postDelayed(runnable,2000);
//    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

//        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
//        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
//                locationComponent.getLastKnownLocation().getLatitude());
//
//        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("bike-source-id");
//        if (source != null) {
//            source.setGeoJson(Feature.fromGeometry(destinationPoint));
//        }


        //setCoordinateEditTexts(point);
        constraintInfo.setVisibility(View.INVISIBLE);
        btnMyLocation.setVisibility(View.VISIBLE);
        btnNavigation.setVisibility(View.VISIBLE);
//        getRoute(originPoint, destinationPoint);
//        btnNavigation.setEnabled(true);
//        btnNavigation.setBackgroundResource(R.color.mapboxBlue);
        return true;

    }


    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Activate the MapboxMap LocationComponent to show user location
// Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private class RefreshData implements Runnable {
        private  Style style;
        private Handler handler;
        public RefreshData(Style style, Handler handler) {
            this.style = style;
            this.handler = handler;
        }

        @Override
        public void run() {

            //((GeoJsonSource)style.getSource("bike-source-id")).setUrl(URL_GET_DATA);
            handler.postDelayed(this, 2000);
        }
    }
}