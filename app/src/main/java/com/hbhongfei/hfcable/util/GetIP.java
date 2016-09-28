package com.hbhongfei.hfcable.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取用户手机端的ip
 * Created by 苑雪元 on 2016/9/27.
 */
public class GetIP {

    public static String GetIp() {
        try {

            for (Enumeration<NetworkInterface> en = NetworkInterface

                    .getNetworkInterfaces(); en.hasMoreElements(); ) {

                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr

                        .hasMoreElements(); ) {

                    InetAddress inetAddress = ipAddr.nextElement();
                    // ipv4地址
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }

                }

            }

        } catch (Exception ex) {

        }

        return "";

    }
}
