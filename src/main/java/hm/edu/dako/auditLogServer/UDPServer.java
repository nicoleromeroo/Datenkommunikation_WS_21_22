package hm.edu.dako.auditLogServer;

import hm.edu.dako.connection.ServerSocketInterface;

import hm.edu.dako.connection.udp.UdpServerSocket;

import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UDPServer extends AuditLogAbstractServer {

    public UDPServer() {
        super();
    }

    public UDPServer(int serverPort) {
        super(serverPort);
    }

    public UDPServer(int serverPort, AuditLogPDUMessagesInterface<AuditLogPDU> model) {
        super(serverPort, model);
    }

    public static void main(String[] args) {
        AuditLogServerInterface theServer = new UDPServer();
        if (args.length != 0) {
            String arg1 = args[0];
            theServer = new UDPServer(Integer.parseInt(arg1));
        }
        else {
            theServer = new UDPServer();
        }

        theServer.start();
    }

    @Override
    void checkAndConfigure() {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("log4j2.auditLogUdpServer.xml");
        context.setConfigLocation(file.toURI());
        /*ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("log4j2.auditLogUdpServer.xml");
        PropertyConfigurator.configure(String.valueOf(url));*/
        //ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //URL url = loader.getResource("log4j2.auditLogTcpServer.xml");
        //FileWatchdog.checkAndConfigure(url);
    }

    @Override
    ServerSocketInterface getServerSocket() throws IOException {
        if (serverSocket == null) {
            serverSocket = new UdpServerSocket(

                    serverPort,
                    AuditLogAbstractServer.DEFAULT_RECEIVEBUFFER_SIZE,
                    AuditLogAbstractServer.DEFAULT_SENDBUFFER_SIZE
            );
        }
        log.info("Server wurde auf " + serverPort + " Port initialisert");
        return serverSocket;
    }
 
}
