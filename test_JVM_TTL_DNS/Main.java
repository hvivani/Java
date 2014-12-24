//This will test the value for the JVM TTL for DNS lookups.
//http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/java-dg-jvm-ttl.html


package com.amazonaws.vivanih.hadoop.cascading;
import java.lang.String;
import java.io.*;
import sun.net.InetAddressCachePolicy;

public class Main {

        public static void main(String[] args) {
        String p = "networkaddress.cache.ttl";

        // property at startup.
        System.out.println(String.format("initial: %s", System.getProperty(p)));

        System.setProperty("networkaddress.cache.ttl","59");

        // if networkaddress.cache.ttl is not set in java.security,
        // a default value of 30 is returned.
        System.out.println(String.format("get: %d",InetAddressCachePolicy.get()));

        // this will throw claiming we cannot make the setting more lax.
        InetAddressCachePolicy.setIfNotSet(15);
        System.out.println(String.format("get: %d",InetAddressCachePolicy.get()));

        //try to set with another method
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");
        System.out.println(String.format("get: %d",InetAddressCachePolicy.get()));
        }

}
