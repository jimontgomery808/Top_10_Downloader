package com.example.josh.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Class that parses an XML File
 */
public class ParseApplications
{
    private String xmlData;                         // XML data to be parsed
    private ArrayList<Application> applications;    // Array of type Application (user created class)

    // Constructor
    public ParseApplications(String xmlData)
    {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();
    }

    // Getter method: returns ArrayList of type Application
    public ArrayList<Application> getApplications()
    {
        return applications;
    }

    // Method to process the XML file
    public boolean process()
    {
        boolean status = true;
        boolean inEntry = false;
        String textValue = "";
        Application currentRecord = null;

        try
        {
            // Creates instance of XmlPullParserFactory
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            // Points xpp to factory
            XmlPullParser xpp = factory.newPullParser();
            // Sets XML input to URL passed into this class
            xpp.setInput(new StringReader(this.xmlData));
            // Stores first event type into eventType
            int eventType = xpp.getEventType();

            // Loop will run until end of XML document
            while(eventType != XmlPullParser.END_DOCUMENT)
            {

                String tagName = xpp.getName();

                switch(eventType)
                {
                    case XmlPullParser.START_TAG:
                        if(tagName.equalsIgnoreCase("entry"))
                        {
                            inEntry = true;
                            currentRecord = new Application();

                        }
                        break;

                    case(XmlPullParser.TEXT):
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(inEntry)
                        {
                            if(tagName.equalsIgnoreCase("entry"))
                            {
                                applications.add(currentRecord);
                                inEntry = false;
                            }
                            else if(tagName.equalsIgnoreCase("name"))
                            {
                                currentRecord.setName(textValue);
                            }
                            else if(tagName.equalsIgnoreCase("artist"))
                            {
                                currentRecord.setArtist(textValue);
                            }
                            else if(tagName.equalsIgnoreCase("releaseDate"))
                            {
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;
                    default:
                        //Nothing else to do
                }

                eventType = xpp.next();
            }
        }
        catch (Exception e)
        {
            status = false;
            e.printStackTrace();
        }

        for(Application app: applications)
        {
            Log.d("ParseApplications", "*******************");
            Log.d("ParseApplications", "Name: " + app.getName());
            Log.d("ParseApplications", "Artist: " + app.getArtist());
            Log.d("ParseApplications", "Release Date: " + app.getReleaseDate());
        }
        return true;

    }
}
