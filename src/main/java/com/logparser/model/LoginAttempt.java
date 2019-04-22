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
    private ArrayList<IPAddressLocation> ipList = new ArrayList<IPAddressLocation>();
    private DatabaseReader cityReader;
    private boolean validUser;
    private int failedLogins = 0;
    private int successLogins = 0;

    //might needs a special case for root due to people attempting to pair it with
    // random passwords
    //so far only accounts for failed attempts
    public LoginAttempt ( String userName, boolean success, DatabaseReader city )
    {
        this.userName = userName;
        //needs to be turned into list later on
        cityReader = city;
        this.validUser = success;
    }

    public void addNewIP ( String ip, int numOfTries, boolean success )
    {
        IPAddressLocation ipObj = new IPAddressLocation(ip, cityReader);
        if (!this.ipList.contains(ipObj))
        {
            this.ipList.add(ipObj);
        }
        if (success)
        {
            addToSuccessLogins();
        }
        else
        {
            addToFailedLogins(numOfTries, ip);
        }

    }

    public void addToFailedLogins ( int numOfTries, String ip )
    {
        for ( int i = 0; i < ipList.size(); i++)
        {
            if (ipList.get(i).getIp().equals(ip))
            {
                ipList.get(i).addToFailedLogins(numOfTries);
            }
        }
        failedLogins += numOfTries;
    }

    public void addToSuccessLogins ( )
    {
        /*
        for ( int i = 0; i < ipList.size(); i++)
        {
            if (ipList.get(i).getIp().equals(ip))
            {
                ipList.get(i).addToSuccessLogins();
            }
        }*/
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

    public ArrayList<IPAddressLocation> getIps( )
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

    public int getTotalAttempts( )
    {
        return failedLogins + successLogins;
    }

    public void printSummary ( )
    {
        System.out.println("User: " + userName + " | Num of Failures:" +
                failedLogins + " | Num of Successes: " + successLogins);
        //print of list of ips that accessed this username with their respective
        //info
        for ( int i = 0; i < ipList.size(); i++)
        {
            ipList.get(i).printSummary();
        }
    }


}