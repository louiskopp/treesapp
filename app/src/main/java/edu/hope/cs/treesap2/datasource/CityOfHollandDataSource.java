package edu.hope.cs.treesap2.datasource;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.hope.cs.treesap2.control.PrefManager;
import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

public class CityOfHollandDataSource extends DataSource {

    String internetFileName = "CoH_Tree_Inventory_6_12_18.csv";
    String localFileName = "COHTreeData.csv";

    Reader in;
    Iterable<CSVRecord> records;
    CSVRecord closestRecord;

    Context parent;
    boolean downloaded, alreadyRead;

    public CityOfHollandDataSource() {}

    public CityOfHollandDataSource(Context aParent) {
        parent = aParent;
    }

    public String getInternetFileName() { return internetFileBase + "/" + internetFileName; }

    @Override
    public String getSourceName() {
        return "City of Holland Tree Data";
    }

    @Override
    public String getDescription() {
        return "Tree Data from the City of Holland via ArcGIS.";
    }

    private void download(String earl, String filename) {
        String str;

        downloaded = false;
        try {
            URL url = new URL(earl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));

            while ((str = in.readLine()) != null) {
                out.write(str);
                out.newLine();
            }

            in.close();
            downloaded = true;
            alreadyRead = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("Error", "Unable to download datasource.");
        }
    }

    @Override
    public Boolean initialize(Context aParent, String initString) {

        parent = aParent;
        downloaded = false;
        alreadyRead = false;

        if(!localFileName.contains("/")) {
            localFileName = parent.getFilesDir() + "/" + localFileName;
        }
        File COHfile = new File(localFileName);

        // Check to see if db is downloaded
        if (! COHfile.exists()) {
            updateDataSource();
        }

        return true;
    }

    private boolean readData() {
        if (alreadyRead) return true;

        try {
            in = new FileReader(localFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            records = CSVFormat.EXCEL.withHeader().parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        alreadyRead = true;
        return true;
    }

    @Override
    public Tree search(TreeLocation location) {
        float[] results = new float[1];
        float closestDistance;
        float cap = PrefManager.getFloat("tree result", 10f);
        int entry, closestEntry;

        readData();
        closestDistance = 99999;
        closestEntry = 0;
        entry = -1;
        for (CSVRecord record : records) {
            entry++;
            try {
                Double lati = Double.valueOf(record.get("Latitude"));
                Double longi = Double.valueOf(record.get("Longitude"));

                Location.distanceBetween(lati, longi,
                                         location.getLatitude(), location.getLongitude(),
                                         results);
               // if (results[0] > 2.0) {
                    if (results[0] < closestDistance) {
                        closestDistance = results[0];
                        closestRecord = record;
                    }
                 /*   continue;
                //}

                //MATCH!  Build tree and return it.
                Tree tree = new Tree();
                tree.setCommonName(record.get("CommonName"));
                tree.setScientificName(record.get("Scientific"));
                tree.setLocation(new TreeLocation(lati, longi));
                tree.setID(record.get(Tree.TREE_ID));
                tree.setCurrentDBH(new Double(record.get("DBH")));
                if (record.get("Park").length() > 0)
                    tree.addInfo("Park", record.get("Park"));
                tree.setFound(true);
                tree.setIsClosest(true);

                return tree;*/
            } catch (Exception e) {
                continue;
            }
        }

        if (closestDistance > cap)
            return null;
        else {
            //MATCH!  Build tree and return it.
            Tree tree = new Tree();
            Double lat = new Double(closestRecord.get("Latitude"));
            Double longi = new Double(closestRecord.get("Longitude"));
            tree.setCommonName(closestRecord.get("CommonName"));
            tree.setScientificName(closestRecord.get("Scientific"));
            tree.setLocation(new TreeLocation(lat, longi));
            tree.setID(closestRecord.get(Tree.TREE_ID));
            tree.setCurrentDBH(new Double(closestRecord.get("DBH")));
            if (closestRecord.get("Park").length() > 0)
                tree.addInfo("Park", closestRecord.get("Park"));
            tree.setFound(true);
            tree.setIsClosest(true);

            return tree;
        }

    }

    @Override
    public Boolean isDownloadable() {
        return true;
    }

    @Override
    public Boolean checkForUpdate() {
        return !isUpToDate();
    }

    @Override
    public Boolean isUpToDate() {

        try {
            File COHfile = new File(localFileName);
            if (COHfile.exists()) {
                URL url = new URL(getInternetFileName());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("HEAD");
                c.connect();
                InputStream in = c.getInputStream();
                return COHfile.lastModified() >= c.getLastModified();
            } else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateDataSource() {
        Log.d("COHDataSource", "Downloading " + getSourceName() + " to " + localFileName);
        File COHfile = new File(localFileName);
        download(getInternetFileName(), localFileName);
        return true;
    }

    @Override
    public Iterable<CSVRecord> getCoordinates(Context context, String file) {
        localFileName = file;
        try {
            in = new FileReader(localFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            records = CSVFormat.EXCEL.withHeader().parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }



}
