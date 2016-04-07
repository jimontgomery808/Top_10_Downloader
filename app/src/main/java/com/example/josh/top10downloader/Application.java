package com.example.josh.top10downloader;

/**
 * Class containing data about an application
 */
public class Application
{
    // Variables
    private String name;        // app name
    private String artist;      // app artist
    private String releaseDate; // app release date

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    // Override toString method
    @Override
    public String toString()
    {
        return ("Name: " + this.getName() +
                "\nArtist: " + this.getArtist() +
                "\nRelease Date: " + this.getReleaseDate());
    }
}
