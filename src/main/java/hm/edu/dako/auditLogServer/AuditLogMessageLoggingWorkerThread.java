package hm.edu.dako.auditLogServer;

//AuditLogWriter

import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;

public class AuditLogMessageLoggingWorkerThread extends Thread {
    private static Logger log = LogManager.getLogger(AuditLogMessageLoggingWorkerThread.class);
    private final AuditLogPDUMessagesInterface<AuditLogPDU> model;
    private int counterForMessage = 0;
    private BufferedWriter writer;

    public AuditLogMessageLoggingWorkerThread(AuditLogPDUMessagesInterface<AuditLogPDU> model) {
        this.model = model;
        setDaemon(true);
    }


}
