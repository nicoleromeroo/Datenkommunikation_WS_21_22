package hm.edu.dako.auditLogServer;

import hm.edu.dako.connection.ServerSocketInterface;
import hm.edu.dako.connection.tcp.TcpServerSocket;
import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.log4j.PropertyConfigurator;
import org.scf4j.props.PropertyConfigurator;

import java.io.IOException;

public class TCPServer extends AuditLogAbstractServer {

    public TCPServer() {
        super();
    }

    public TCPServer(int serverPort) {
        super(serverPort);
    }

    public TCPServer(int serverPort, AuditLogPDUMessagesInterface<AuditLogPDU> model) {
       super(serverPort, model);
    }

    public static void main(String[] args) {
        AuditLogServerInterface theServer;
        if (args.length != 0) {
            String arg1 = args[0];
            theServer = new TCPServer(Integer.parseInt(arg1));
        }
        else {
            theServer = new TCPServer();
        }
        theServer.start();
    }

    @Override
    void initLog4J() {
        PropertyConfigurator.configureAndWatch("log4j.auditLogServer_tcp.properties");
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
    //brauchen das nicht
    @Override
    public void stop() throws Exception {

    }
}

