package edu.hope.cs.treesap2.identify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import edu.hope.cs.treesap2.model.TreeLocation;
import edu.hope.cs.treesap2.view.MainActivity;

public class GPS_Identifier extends IdentificationMethod {

    TreeLocation location;

    @Override
    public void identify() {
        location = new TreeLocation();

        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", 0.0);
        bundle.putDouble("latitude", 0.0);
        Intent startDeviceIntent = new Intent(parent, Big_Red_Button_Identifier.class);
        startDeviceIntent.putExtras(bundle);
        ((MainActivity)parent).startActivityForResult(startDeviceIntent, 1000);
    }

    public GPS_Identifier() {
        super();
    }

    public GPS_Identifier(Context context) {
        super(context);
    }

    @Override
    public void setParent(Context context) {
        super.setParent(context);
    }

    @Override
    public String getMethodName() {
        return "Use Phone GPS Sensor";
    }

    @Override
    public String getDescription() {
        return "Identify trees by using this phone's GPS sensor";
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
        preferences.add("Return tree results if tree is within ___ meters: | tree result | EDIT_TEXT | true | 10.0");
        return preferences;
    }
}
