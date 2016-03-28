package com.example.alejandro.d_it.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alejandro.d_it.Dates;
import com.example.alejandro.d_it.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MacBook on 23/03/16.
 */
public class DatesAdapter extends ArrayAdapter {

    // Atributos
    private RequestQueue requestQueue;
    JsonObjectRequest jsArrayRequest;
    private static final String URL_BASE = "http://d-it.azurewebsites.net/";
    private static final String URL_JSON = "/dates";
    private static final String url = "http://d-it.azurewebsites.net/dates";
    private static final String TAG = "PostAdapter";
    List<Dates> items;

    public DatesAdapter(Context context) {
        super(context,0);

        // Crear nueva cola de peticiones
        /*requestQueue= Volley.newRequestQueue(context);

        // Nueva petición JSONObject
        jsArrayRequest = new JsonArrayRequest( //Contiene 4 parámetros.
                Request.Method.GET,     //Contiene los métodos necesarios: GET, POST
                URL_BASE + URL_JSON,    //URL del recurso JSON
                //(String)null,                   //Son los pares clave-valor, si se fuese a realizar una petición POST
                new Response.Listener<JSONObject>() {   //Para definir una escucha que maneje los resultados de la petición.
                    @Override
                    public void onResponse(JSONObject response) {
                        items = parseJson(response);
                        Log.d(TAG, "No aqui: "+response.toString());
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());

                    }
                }
        );

        // Añadir petición a la cola
        requestQueue.add(jsArrayRequest);
        */

        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, URL_BASE+URL_JSON, new Response.Listener<JSONArray>()
        {

            public void onResponse(JSONArray jsonArray)
            {
                /*BufferedOutputStream bos;
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
                }*/

                for(int i=0;i<jsonArray.length();i++)
                {
                    items = parseJson(jsonArray);
                    System.out.println("JSONNNNNN: "+jsonArray.toString());
                    notifyDataSetChanged();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array "+volleyError.toString());
                Log.d(TAG, "Error Respuesta en JSON: " + volleyError.getMessage());
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Referencia del view procesado
        View listItemView;

        //Comprobando si el View no existe
        listItemView = null == convertView ? layoutInflater.inflate(
                R.layout.items_dates_today,
                parent,
                false) : convertView;


        // Obtener el item actual
        Dates item = items.get(position);

        // Obtener Views
        TextView txtName = (TextView) listItemView.
                findViewById(R.id.txtName);
        TextView txtHour = (TextView) listItemView.
                findViewById(R.id.txtHour);

        //final ImageView imagenPost = (ImageView) listItemView.
          //      findViewById(R.id.imagenPost);

        // getting movie data for the row
        Dates m = items.get(position);

        // thumbnail image
        //thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        //title.setText(m.getTitle());

        // Actualizar los Views
        txtName.setText(item.getPatient());
        txtHour.setText(item.getHour());

        // Petición para obtener la imagen
        /*ImageRequest request = new ImageRequest(
                URL_BASE + item.getImagen(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imagenPost.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imagenPost.setImageResource(R.drawable.error);
                        Log.d(TAG, "Error en respuesta Bitmap: "+ error.getMessage());
                    }
                });

        // Añadir petición a la cola
        requestQueue.add(request);
        */


        return listItemView;
    }

    public List<Dates> parseJson(JSONArray jsonArray){
        // Variables locales
        List<Dates> posts = new ArrayList();
        //JSONArray jsonArray= null;

        for(int i=0;i<jsonArray.length();i++)
        {
            try
            {
                JSONObject objeto = jsonArray.getJSONObject(i);

                Dates post = new Dates(
                        objeto.getString("patient"),
                        objeto.getString("day"),
                        objeto.getString("hour"),
                        objeto.getString("status"));

                //String nom = objeto.getString("nombre");
                //objeto.getString("imagen"));


                posts.add(post);

            } catch (JSONException e) {
                Log.e(TAG, "Error de parsing: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return posts;
    }
}
