package com.datasource.retrofitsource

import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class TLSSocketFactory constructor(val delegate:SSLSocketFactory) : SSLSocketFactory() {

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.getDefaultCipherSuites()
    }

    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose))
    }

    override fun createSocket(host: String?, port: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort))
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort))
    }

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket != null && socket is SSLSocket) {
            (socket.setEnabledProtocols(arrayOf<String>("TLSv1.2")))
        }
        return socket
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.getSupportedCipherSuites()
    }
}