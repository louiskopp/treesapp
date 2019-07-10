package edu.hope.cs.treesap2.identify;

import android.content.Context;

import edu.hope.cs.treesap2.control.IntentIntegrator;
import edu.hope.cs.treesap2.model.TreeLocation;
import edu.hope.cs.treesap2.view.MainActivity;

public class Barcode_Identifier extends IdentificationMethod {

    TreeLocation location;

    public Barcode_Identifier(){
        super();
    }

    public Barcode_Identifier(Context context){
        super(context);
    }

    @Override
    public String getMethodName() {
        return "Use Barcode Scanner";
    }

    @Override
    public String getDescription() {
        return "Use a barcode scanner to identify a tree by a barcode.";
    }

    @Override
    public void identify() {
        IntentIntegrator scanIntegrator = new IntentIntegrator((MainActivity)parent);
        scanIntegrator.initiateScan();
    }

    @Override
    public void setParent(Context context) {
        super.setParent(context);
    }

    @Override
    public TreeLocation getLocation() {
        return null;
    }

    public void setLocation(TreeLocation loc){location = loc;}

}
