package com.vikesh.main;

import com.vikesh.utilities.IPAddressUtility;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public void validateIPAddressInFile(String fileLocation) throws Exception {
        Map<String, Map<String, String>> mismatchedIPs = new HashMap<>();
        List<String[]> outputs = new ArrayList<>();
        String[] outputHeaders = {"IP_ADDRESS", "ACTUAL_NETWORK_TYPE"};
        outputs.add(outputHeaders);
        InputStream inputStream = getClass().getResourceAsStream(fileLocation);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            final String headerRecord = br.readLine();
            final String[] headers = headerRecord.split("\\,", -1);
            String dataRecord = null;
            while ((dataRecord = br.readLine()) != null) {
                Map<String, Object> dataMap = new HashMap<>();
                final Object[] values = dataRecord.split("\\,", -1);
                for (int index = 0; index < headers.length; index++) {
                    try {
                        dataMap.put(headers[index], values[index]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        dataMap.put(headers[index], null);
                    }
                }
                String ipAddress = String.valueOf(dataMap.get("IP_ADDRESS"));
                String networkType = String.valueOf(dataMap.get("NETWORK_TYPE"));
                String calculatedNetworkType = IPAddressUtility.getIPAddressV4NetworkType(ipAddress);
                boolean matched = calculatedNetworkType.equalsIgnoreCase(networkType);
                String[] rowData = {ipAddress, networkType, calculatedNetworkType, String.valueOf(matched).toUpperCase()};
                outputs.add(rowData);
                if (!matched) {
                    Map<String, String> mismatchedIP = new HashMap<>();
                    mismatchedIP.put("Expected", calculatedNetworkType);
                    mismatchedIP.put("Actual", networkType);
                    mismatchedIPs.put(ipAddress, mismatchedIP);
                }
            }
        }
        if (!mismatchedIPs.isEmpty()) {
            System.out.println("Few IPs are not matching, IPs: " + mismatchedIPs);
        }
        writeOutputToFile(outputs);
    }

    private void writeOutputToFile(List<String[]> outputs) {
        if (outputs.isEmpty()) {
            System.out.println("Empty data found, so ignoring file writing");
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter("./src/test/output/ip-address-output.csv"))) {
            writer.writeAll(outputs);
        } catch (Exception e) {
            System.out.println("Caught exception while saving file and exception is: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.validateIPAddressInFile("/files/ip-address.csv");
    }
}
