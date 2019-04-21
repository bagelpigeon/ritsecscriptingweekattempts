package com.logparser.model;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;

public class LoginAttempt
{
    //entry that denotes an attempt at using an invalid user for ssh



    private String userName;
    private String ip;
    private boolean validUser;
    private int failedLogins = 0;
    private int successLogins = 0;
    private String city = "";
    private String country = "";

    //so far only accounts for failed attempts
    public LoginAttempt ( String[] data, boolean success )
    {
        this.userName = data[7];
        this.ip = data[9];
        this.validUser = success;
        determineLocation ();
    }

    public void addToFailedLogins ( int numOfTries )
    {
        failedLogins += numOfTries;
    }

    public void addToSuccessLogins ( )
    {
        successLogins += 1;
    }

    public boolean isValidUser( )
    {
        return validUser;
    }

    public int getFailedLogins( )
    {
        return failedLogins;
    }

    public int getSuccessLogins( )
    {
        return successLogins;
    }

    public String getIp( )
    {
        return ip;
    }

    public String getUserName( )
    {
        return userName;
    }

    public boolean compareUserName ( String userName )
    {
        if ( this.userName.equals( userName ) )
        {
            return true;
        }
        return false;
    }

    public int totalAttemptsAtLogin ( )
    {
        return failedLogins + successLogins;
    }

    public void printSummary ( )
    {
        System.out.println("User: " + userName + " | IP: " + ip + " | City, Country: " + city + ", " + country);
    }

    private void determineLocation ( )
    {

    }
}