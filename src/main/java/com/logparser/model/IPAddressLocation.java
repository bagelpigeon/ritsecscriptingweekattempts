package com.logparser.model;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import java.net.InetAddress;

public class IPAddressLocation
{
    private int failedLogins = 0;
    private int successLogins = 0;
    private String location;
    private String ip;
    private String city;
    private String country;
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
            if (cityName == null)
            {
                city = "unknown";
            }
            else
            {
                city = cityName;
            }
            String countryName = response.getCountry().getName();
            if (countryName == null)
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
            System.out.println("Error with finding ip. May be due to an incorrectly formatted line.");
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            System.out.println("Error with getting response from database or ip is not in database.");
        }
    }

    public void printSummary ( )
    {
        System.out.println("    IPAddress: " + ip + " | " + "Location: " + city + ", "
            + country + " | Failed Logins:" + failedLogins + " | Succeeded Logins:" + successLogins);
    }

}
