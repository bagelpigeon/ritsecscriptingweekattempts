package com.logparser.model;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;

public class LoginAttempt
{
    //entry that denotes an attempt at using an invalid user for ssh
    private String userName;
    private ArrayList<String> ipList = new ArrayList<String>();
    private DatabaseReader cityReader;
    private String ip;
    private boolean validUser;
    private int failedLogins = 0;
    private int successLogins = 0;
    private String city = "";
    private String country = "";

    //might needs a special case for root due to people attempting to pair it with
    // random passwords
    //so far only accounts for failed attempts
    public LoginAttempt ( String userName, String ip, boolean success, DatabaseReader city )
    {
        this.userName = userName;
        //needs to be turned into list later on
        //this.ipList.add(ip);
        this.ip = ip;
        this.validUser = success;
        cityReader = city;
        determineLocation (ip);
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

    public ArrayList<String> getIps( )
    {
        return ipList;
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
        String ipList = new String("");
        System.out.println("User: " + userName + " | IP: " + ip + " | Location: " + city + ", " + country
        + " | Num of Failures:" + failedLogins + " | Num of Successes: " + successLogins);
    }

    private void determineLocation ( String ip )
    {
        try
        {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = cityReader.city(ipAddress);
            String cityName = response.getCity().getName();
            if (cityName == null)
            {
                city = "unknown";
            }
            else
            {
                city = cityName;
            }
            String countryName = response.getCountry().getName();
            if (country == null)
            {
                country = "unknown";
            }
            else
            {
                country = countryName;
            }

        }
        catch (java.net.UnknownHostException e)
        {
            System.out.println("Error with finding ip. May be due to a corrupted line.");
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            System.out.println("Error with getting response from database or ip is not in database.");
        }


    }
}