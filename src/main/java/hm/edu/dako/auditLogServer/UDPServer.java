package hm.edu.dako.auditLogServer;

import hm.edu.dako.connection.ServerSocketInterface;
import hm.edu.dako.connection.tcp.TcpServerSocket;
import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class UDPServer extends AuditLogAbstractServer{

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
        AuditLogServerInterface theServer;
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
    void initLog4J() {
        PropertyConfigurator.configureAndWatch("log4j.auditLogServer_udp.properties");
    }

    @Override
    ServerSocketInterface getServerSocket() throws IOException {
        if (serverSocket == null) {
            serverSocket = new TcpServerSocket(
                    serverPort,
                    AuditLogAbstractServer.DEFAULT_RECEIVEBUFFER_SIZE,
                    AuditLogAbstractServer.DEFAULT_SENDBUFFER_SIZE
            );
        }
        log.info("Server wurde auf " + serverPort + " Port initialisert");
        return serverSocket;
    }
    // nicht zu gebrauchen
    @Override
    public void stop() throws Exception {

    }
}
