package com.sprint.findex.sb02findexteam4.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {

    private IpUtils(){}

    public static String getIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

}
