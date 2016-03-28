package com.example.alejandro.d_it;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class profileFragment extends Fragment {

    AppLocationService appLocationService;
    TextView txtAdress;
    TextView txtDatesToday;
    TextView txtDatesTomorrow;
    ImageView btnCall;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        appLocationService = new AppLocationService(getActivity().getApplicationContext());

        txtAdress = (TextView) view.findViewById(R.id.txtAdress);
        txtDatesToday = (TextView) view.findViewById(R.id.txtDatesToday);
        txtDatesTomorrow = (TextView) view.findViewById(R.id.txtDatesTomorrow);
        btnCall = (ImageView) view.findViewById(R.id.btnCall);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLlamada();
            }
        });

        showAdress();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showTodayDates();
        showTomorrowDates();
    }

    public void realizarLlamada()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:2221026541"));//(222)2463215
        profileFragment.this.startActivity(callIntent);
    }

    public void showAdress()
    {
        Location location = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);

        //you can hard-code the lat & long if you have issues with getting it
        //remove the below if-condition and use the following couple of lines
        //double latitude = 37.422005;
        //double longitude = -122.084095

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                /*System.out.println("ADDRESS: "+
                        addresses.get(0).getAddressLine(0)+" "+
                                addresses.get(0).getAddressLine(1)+" "+
                                addresses.get(0).getAddressLine(2)
                );*/
                txtAdress.setText(addresses.get(0).getAddressLine(0));
                System.out.println("Ciudad: "+addresses.get(0).getAddressLine(2));
            } catch (IOException e) {
                e.printStackTrace();
            }

            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getActivity().getApplicationContext(), new GeocoderHandler());
        } else {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity().getApplicationContext());
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().getApplicationContext().startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            //tvAddress.setText(locationAddress);
            System.out.print("Dirección... " + locationAddress);
            //Log.e("a");

        }
    }

    public void showTodayDates(){
        String url = "http://d-it.azurewebsites.net/dates/today";
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,url,new Response.Listener<JSONArray>()
        {

            public void onResponse(JSONArray jsonArray)
            {
                BufferedOutputStream bos;
                File cache = new File(Environment.getExternalStorageDirectory() + File.separator + "cache.json");
                try {
                    cache.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    bos = new BufferedOutputStream(new FileOutputStream(cache));
                    //bos.write(jsonArray.toString().getBytes());
                    //bos.flush();
                    //bos.close();

                }catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }catch(IOException a)
                {
                    a.printStackTrace();
                }
                finally{
                    System.gc();
                }

                for(int i=0;i<jsonArray.length();i++)
                {
                    int cont = jsonArray.length();
                    String datesToday = String.valueOf(cont);
                    try
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombre = jsonObject.getString("patient");
                        System.out.println("Numero de citas HOY: "+cont);
                        System.out.println("Contenido del JSON en pacientes: "+nombre);
                        txtDatesToday.setText(datesToday);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array " + volleyError.toString());
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);
    }

    public void showTomorrowDates(){
        String url = "http://d-it.azurewebsites.net/dates/tomorrow";
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,url,new Response.Listener<JSONArray>()
        {

            public void onResponse(JSONArray jsonArray)
            {
                BufferedOutputStream bos;
                File cache = new File(Environment.getExternalStorageDirectory() + File.separator + "cache.json");
                try {
                    cache.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    bos = new BufferedOutputStream(new FileOutputStream(cache));
                    //bos.write(jsonArray.toString().getBytes());
                    //bos.flush();
                    //bos.close();

                }catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }catch(IOException a)
                {
                    a.printStackTrace();
                }
                finally{
                    System.gc();
                }

                for(int i=0;i<jsonArray.length();i++)
                {
                    int cont = jsonArray.length();
                    String datesTomorrow = String.valueOf(cont);
                    try
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombre = jsonObject.getString("patient");
                        System.out.println("Numero de citas MAÑANA: "+cont);
                        System.out.println("Contenido del JSON en pacientes: "+nombre);
                        txtDatesTomorrow.setText(datesTomorrow);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array " + volleyError.toString());
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);
    }
}
