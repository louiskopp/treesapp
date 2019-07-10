package edu.hope.cs.treesap2.identify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import edu.hope.cs.treesap2.model.TreeLocation;
import edu.hope.cs.treesap2.view.MainActivity;
import edu.hope.cs.treesap2.view.MapsActivity;

public class Google_Map_Identifier extends IdentificationMethod {

    TreeLocation location;

    public Google_Map_Identifier(){super();}
    public Google_Map_Identifier(Context context) {super(context);}

    @Override
    public void identify() {
        location = new TreeLocation();

        Bundle bundle = new Bundle();
        Intent startMapsIntent = new Intent(parent, MapsActivity.class);
        bundle.putDouble("longitude", 0.0);
        bundle.putDouble("latitude", 0.0);
        startMapsIntent.putExtras(bundle);
        ((MainActivity)parent).startActivityForResult(startMapsIntent, 2000);
    }

    @Override
    public void setParent(Context context) {
        super.setParent(context);
    }

    @Override
    public String getMethodName() {
        return "Use Google Maps";
    }

    @Override
    public String getDescription() {
        return "Use Google Maps to identify a tree by its location";
    }

    @Override
    public TreeLocation getLocation() {
        return location;
    }

    public void setLocation(TreeLocation loc) {
        location = loc;
    }

    @Override
    public ArrayList<String> getPreferences()  {
        preferences = new ArrayList<>();
        preferences.add("Map zoom level: | zoom | SPINNER | zoom_array"); //last part indicates the resource used for the spinner, usually an array
        preferences.add("Marker on your location: | marker | ON_OFF_SWITCH");
        return preferences;
    }
}
