package com.parcel.license;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class LicenseChecker {

    private static final String LICENSE_FILE = "license.dat";

    public static boolean checkLicense() {
        File file = new File(LICENSE_FILE);
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String hash = reader.readLine();

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();

                byte[] mac = netInterface.getHardwareAddress();
                if(mac != null) {
                    String macStr = byteArrayToMacString(mac);
                    if(BCrypt.checkpw(macStr, hash)) {
                        return true;
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not found license file!");
        } catch (IOException e) {
            System.out.println("Error during reading file.");
        }

        return false;
    }

    private static String byteArrayToMacString(byte[] macAddr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macAddr.length; i++) {
            if(i > 0) {
                sb.append(":");
            }

            byte b = macAddr[i];
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
