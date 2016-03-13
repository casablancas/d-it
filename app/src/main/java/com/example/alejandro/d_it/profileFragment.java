package com.example.alejandro.d_it;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class profileFragment extends Fragment {

    AppLocationService appLocationService;
    TextView txtAdress;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        appLocationService = new AppLocationService(getActivity().getApplicationContext());
        txtAdress = (TextView) view.findViewById(R.id.txtAdress);
        showAdress();
        return view;
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
}
