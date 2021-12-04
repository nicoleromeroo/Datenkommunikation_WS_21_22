package hm.edu.dako.auditLogServer;

import hm.edu.dako.connection.ServerSocketInterface;
import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;

import static javafx.application.Application.launch;

/**
 *  ChatServerGUI
 */


public class AuditLogAbstractServer implements AuditLogServerInterface{

    /**
     * Default AuditLog Server Port.
     */
    // Standardvalue for AuditLogServer
    static final String DEFAULT_AUDITLOGSERVER_PORT = "40001";

    /**
     * Default Send Buffer Size fuer Server Port in Bytes.
     */
    // Standard-und Maximal-Puffergroessen in Byte
    static final String DEFAULT_SENDBUFFER_SIZE = "300000";
    /**
     * Default Receive Buffer Size fuer Server Port in Bytes.
     */
    static final String DEFAULT_RECEIVEBUFFER_SIZE = "300000";

    private static final Logger log = LogManager.getLogger(AuditLogAbstractServer.class);

    static ServerSocketInterface serverSocket;

    final int serverPort;

    private ConnectionWorkerThreadImpl connectionWorkerThread;
    private AuditLogPDUMessagesInterface<AuditLogPDU> model;

    public AuditLogAbstractServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public AuditLogAbstractServer(int serverPort, AuditLogPDUMessagesInterface<AuditLogPDU> model) {
        this.serverPort = serverPort;
        this.model = model;
    }

    public AuditLogPDUMessagesInterface<AuditLogPDU> getModel() {
        return model;
    }

    public static void main(String[] args) {

        // Log4j2-Logging aus Datei konfigurieren
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("log4j2.chatServer.xml");
        context.setConfigLocation(file.toURI());

        launch(args);
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() throws Exception {

    }
}
