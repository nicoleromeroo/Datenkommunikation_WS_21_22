package hm.edu.dako.auditLogServer;

import hm.edu.dako.chatCommon.ExceptionHandler;
import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

//AuditLogWriter
public class AuditLogMessageLoggingWorkerThread extends Thread {
    private static Logger log = LogManager.getLogger(AuditLogMessageLoggingWorkerThread.class);
    private final AuditLogPDUMessagesInterface<AuditLogPDU> model;
    private int counterForMessage = 0;
    private BufferedWriter writer;

    public AuditLogMessageLoggingWorkerThread(AuditLogPDUMessagesInterface<AuditLogPDU> model) {
        this.model = model;
        setDaemon(true);
    }

    @SuppressWarnings("BusyWait")
    public void run() {
        while (Thread.currentThread().isInterrupted()) {
            try {
                List<AuditLogPDU> newMessages = model.getNewMessages();
                counterForMessage += newMessages.size();
                log.debug("Menge der neuen Nachrichten: " + counterForMessage);
                for (AuditLogPDU auditLogPDU : model.getNewMessages()) {
                    log.debug(auditLogPDU);
                    getWriter()
                            .append(auditLogPDU.getPduType().toString())
                            .append("||")
                            .append(auditLogPDU.getServerThreadName())
                            .append("||")
                            .append(auditLogPDU.getClientThreadName())
                            .append("||")
                            .append(new Date(auditLogPDU.getAuditTime()).toString())
                            .append("||")
                            .append(auditLogPDU.getUserName())
                            .append("||")
                            .append(auditLogPDU.getMessage())
                            .append("||");
                    getWriter().newLine();
                }
                //close the writer
                getWriter().flush();
                Thread.sleep(1200);
            } catch (InterruptedException | IOException e) {
                Thread.currentThread().interrupt();
                ExceptionHandler.logException(e);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private BufferedWriter getWriter() throws IOException {

        if (writer == null) {
            final File logDir = new File(System.getProperty("user.dir") + File.separator + "logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateFormatted = date.format(formatter);
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String formattedDate = format.format(LocalDate.now());

            File logFile = new File(logDir.getAbsolutePath() + File.separator + "audit_log_" + dateFormatted + ".csv");

            writer = new BufferedWriter(new FileWriter(logFile, true));

            createLogHeaderLine();
        }
        return writer;
    }

    private void createLogHeaderLine() throws IOException {
        writer
                .append("PDU Type")
                .append("||")
                .append("Server Thread Name")
                .append("||")
                .append("Client Thread Name")
                .append("||")
                .append("Audit time")
                .append("||")
                .append("Username")
                .append("||")
                .append("Message")
                .append("||");
        writer.newLine();
    }


}
