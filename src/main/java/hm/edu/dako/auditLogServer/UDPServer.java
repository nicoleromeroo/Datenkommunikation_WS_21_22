package hm.edu.dako.auditLogServer;

import hm.edu.dako.connection.ServerSocketInterface;
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
        return null;
    }
    // nicht zu gebrauchen
    @Override
    public void stop() throws Exception {

    }
}
