package com.example.alejandro.d_it;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.alejandro.d_it.Adapters.DatesTodayAdapter;
import com.example.alejandro.d_it.Adapters.DatesTomorrowAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class datesFragment extends Fragment  {

    // Atributos
    ListView listView;
    ArrayAdapter adapter;
    String url = "http://museosapp.azurewebsites.net/piezas";
    Button btnToday, btnTomorrow;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dates,container,false);
        btnToday = (Button) view.findViewById(R.id.btnHoy);
        btnTomorrow = (Button) view.findViewById(R.id.btnMañana);

        btnToday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast toast = Toast.makeText(getActivity(), "Citas de Hoy", Toast.LENGTH_SHORT);
                toast.show();
                showToday();
            }
        });

        btnTomorrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast toast = Toast.makeText(getActivity(), "Citas de Mañana", Toast.LENGTH_SHORT);
                toast.show();
                showTomorrow();
            }
        });

        // Obtener instancia de la lista
        listView= (ListView) view.findViewById(R.id.listView);

        //listView= (ListView)AppCompatActivity.findViewById(R.id.listView);

        Toast toast = Toast.makeText(getActivity(), "Citas de Hoy", Toast.LENGTH_SHORT);
        //toast.show();
        // Crear y setear adaptador
        adapter = new DatesTodayAdapter(getActivity());
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
                    try
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombre = jsonObject.getString("nombre");
                        String categoria = jsonObject.getString("categoria");
                        System.out.println("Contenido del JSON: "+nombre+" "+categoria);

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

    public void showToday(){
        // Crear y setear adaptador para las citas de hoy
        adapter = new DatesTodayAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    public void showTomorrow(){
        // Crear y setear adaptador para las citas de mañana
        adapter = new DatesTomorrowAdapter(getActivity());
        listView.setAdapter(adapter);
    }
}
