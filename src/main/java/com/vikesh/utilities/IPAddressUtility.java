package com.vikesh.utilities;

public class IPAddressUtility {

    private static final String INVALID_IP = "INVALID_IP";
    private static final String PUBLIC_IP = "PUBLIC_NETWORK";
    private static final String PRIVATE_IP = "PRIVATE_NETWORK";

    public static String getIPAddressV4NetworkType(String ipAddress) {
        if (!isValidIPV4(ipAddress)) {
            return INVALID_IP;
        }
        if (ipAddress.startsWith("1.")) {
            return PRIVATE_IP;
        }
        return PUBLIC_IP;
    }

    public static boolean isValidIPV4(final String ipAddress) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }
        return ipAddress.matches(PATTERN);
    }
}
