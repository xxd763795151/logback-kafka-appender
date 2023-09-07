package com.github.danielwegener.logback.kafka.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author: xuxd
 * @date: 2023/7/12 16:44
 **/
public class HostConverter extends ClassicConverter {

    private String host;

    public HostConverter() {
        String value = System.getProperty("service.host");
        if (value != null && !value.trim().isEmpty()) {
            this.host = value;
        } else {
            this.host = getHost();
        }

    }

    @Override
    public String convert(ILoggingEvent event) {
        return host;
    }

    private String getHost() {
        try {
            InetAddress candidateAddress = null;

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr.getHostAddress();
                        }

                        if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }

                    }
                }
            }

            return candidateAddress == null ? InetAddress.getLocalHost().getHostAddress() : candidateAddress.getHostAddress();
        } catch (Exception e) {
            System.err.println(e);
        }
        return "";
    }
}
