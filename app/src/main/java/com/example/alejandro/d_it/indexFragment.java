package com.example.alejandro.d_it;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alejandro.d_it.Adapters.DatesTodayAdapter;
import com.example.alejandro.d_it.Adapters.NewsAdapter;

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

public class indexFragment extends Fragment {

    AppLocationService appLocationService;
    TextView txtCity;
    TextView txtNumPatients;
    String url = "http://d-it.azurewebsites.net/dates";
    // Atributos
    ListView listView;
    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        appLocationService = new AppLocationService(getActivity().getApplicationContext());

        txtCity = (TextView) view.findViewById(R.id.txtCity);
        txtNumPatients = (TextView) view.findViewById(R.id.txtNumDates);

        // Obtener instancia de la lista
        listView= (ListView) view.findViewById(R.id.listViewNews);

        // Crear y setear adaptador
        adapter = new NewsAdapter(getActivity());
        listView.setAdapter(adapter);

        showAdress();
        showAllDates();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllDates();

    }

    public void showAllDates(){
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
                    String dates = String.valueOf(cont);
                    try
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombre = jsonObject.getString("patient");
                        System.out.println("Numero de citas: "+cont);
                        System.out.println("Contenido del JSON en pacientes: "+nombre);
                        txtNumPatients.setText(dates);

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
                txtCity.setText(addresses.get(0).getAddressLine(2));
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
}
