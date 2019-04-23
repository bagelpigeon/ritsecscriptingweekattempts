package com.logparser.model;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import java.net.InetAddress;

/**
 * This class summarizes a attempts to authenticate for a particular IP
 * This class is attached to a single LoginAttempt (username)
 * Author: github.com/bagelpigeon
 **/

public class IPAddressLocation
{
    private int failedLogins = 0;
    private int successLogins = 0;
    private String location;
    private String ip;
    private String city = "unknown";
    private String country = "unknown";
    private DatabaseReader cityReader;

    public IPAddressLocation (String ipAddress, DatabaseReader cityReader)
    {
        this.ip = ipAddress;
        this.cityReader = cityReader;
        determineLocation();
    }


    @Override
    public boolean equals ( Object otherIPObj )
    {
        if (otherIPObj == this )
        {
            return true;
        }
        if (!(otherIPObj instanceof IPAddressLocation))
        {
            return false;
        }
        IPAddressLocation otherIP = (IPAddressLocation) otherIPObj;

        return this.ip.equals(otherIP.ip);
    }
    public void addToFailedLogins ( int numOfTries )
    {
        failedLogins += numOfTries;
    }

    public void addToSuccessLogins ( )
    {
        successLogins += 1;
    }

    public int getFailedLogins ()
    {
        return failedLogins;
    }

    public int getSuccessLogins ()
    {
        return successLogins;
    }

    public String getIp ()
    {
        return ip;
    }

    /**
     * Returns hashcode
     * @return
     */
    @Override
    public int hashCode ()
    {
        return this.ip.hashCode();
    }

    private void determineLocation ( )
    {
        try
        {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = cityReader.city(ipAddress);
            String cityName = response.getCity().getName();
            if (cityName != null)
            {
                city = cityName;
            }
            String countryName = response.getCountry().getName();
            if (countryName != null)
            {
                country = countryName;
            }
        }
        catch (java.net.UnknownHostException e)
        {
            System.out.println("Error with finding ip: " + ip + ". May be due to an incorrectly formatted line.");
        }
        catch (Exception e)
        {
            //Commented out, since some ips are not in the database, an expected error, but including this
            // exception will spam the log
            //e.printStackTrace();
            //System.out.println("Ip: " + ip + " is not in database.");
        }
    }

    public void printSummary ( )
    {
        System.out.println("    IPAddress: " + ip + " | " + "Location: " + city + ", "
            + country + " | Failed Logins:" + failedLogins + " | Succeeded Logins:" + successLogins);
    }

}
