package hm.edu.dako.auditLogServer.AdministratorTool;

import hm.edu.dako.chatClient.ClientModel;
import hm.edu.dako.chatCommon.ExceptionHandler;
import hm.edu.dako.pdu.AuditLogPduType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AdministratorGUI extends Application {
    private static final Logger log = LogManager.getLogger(AdministratorGUI.class);
    private final ClientModel model = new ClientModel();
    @FXML
    private TableColumn<ItemsForTableView, String> timeC;
    private Stage stage;
    //private ClientImpl communicator;
    @FXML
    private TreeView<String> stringTreeView;
    @FXML
    private TextField txtSelectedFile;
    @FXML
    private TextField txtClientsCounter;
    @FXML
    private TextField txtPDUCounter;
    private TableView<ItemsForTableView> tableItemTableView;
    @FXML
    private TableColumn<ItemsForTableView, String> clientC;
    @FXML
    private TableColumn<ItemsForTableView, String> PDULoginCounterC;
    @FXML
    private TableColumn<ItemsForTableView, String> PDULogoutCounterC;
    @FXML
    private TableColumn<ItemsForTableView, String> loginTimeC;
    @FXML
    private TableColumn<ItemsForTableView, String> logoutTimeC;
    @FXML
    private TableColumn<ItemsForTableView, String> PDUMessageCounterC;
    @FXML
    private TableColumn<ItemsForTableView, String> PDUUndefinedCounterC;
    @FXML
    private TableColumn<ItemsForTableView, String> PDUFinishCounterC;
    private String fileSelected;
    private int PDUCounter;
    @FXML
    private TableView<ItemsForTableView> tableView;

    /**
     * userMap speichert für jeden Client das entsprechende TableItem, das
     * mit bestimmten Werten gefüllt ist.
     */
    private HashMap<String, ItemsForTableView> userMap;


    /**
     * Data wird verwendet, um die tableView zu setzen, enthält alle TableItems der userMap.
     */
    private final ObservableList<ItemsForTableView> data = FXCollections.observableArrayList();

    public static void main(String[] args) {
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("AdministratorGUI.xml");
        context.setConfigLocation(file.toURI());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //von ClientFxGUI
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdministratorGUI.xml"));
            Parent root = loader.load();
            primaryStage.setTitle("Administrator Tool");
            primaryStage.setScene(new Scene(root, 280, 320));
            root.setStyle("-fx-background-color: cornsilk");
            stage = primaryStage;
            primaryStage.show();
        } catch (IOException e) {
            setErrorMessage();
            e.printStackTrace();
            log.error(e);
        }
    }

    @FXML
    public void initAd() {
        TreeItem<String> rootItem = new TreeItem<>("logs", new ImageView(
                new Image(getClass().getResourceAsStream("images/folder.png"))));
        rootItem.setExpanded(true);

        File dir = new File(System.getProperty("user.dir") + "/logs/");
        File[] files = dir.listFiles((dire, name) -> name.toLowerCase().endsWith(".csv"));
        if (files != null) {
            for (File file : files) {
                TreeItem<String> item = new TreeItem<> (file.getName(),new ImageView(
                        new Image(getClass().getResourceAsStream("images/file.png"))));
                rootItem.getChildren().add(item);
            }
        }

        stringTreeView.setRoot(rootItem);
        clientC.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        PDULoginCounterC.setCellValueFactory(new PropertyValueFactory<>("PDULoginCounter"));
        PDULogoutCounterC.setCellValueFactory(new PropertyValueFactory<>("PDULogoutCounter"));
        loginTimeC.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        logoutTimeC.setCellValueFactory(new PropertyValueFactory<>("logoutTime"));
        PDUMessageCounterC.setCellValueFactory(new PropertyValueFactory<>("PDUMessageCounter"));
        PDUUndefinedCounterC.setCellValueFactory(new PropertyValueFactory<>("PDUUndefinedCounter"));
        PDUFinishCounterC.setCellValueFactory(new PropertyValueFactory<>("PDUFinishCounter"));
    }

    /**
     * Diese Methode wird aufgerufen, wenn eines der TreeView-Elemente ausgewählt ist.
     * Ein Thread für die Ladeanimation und einer für die Protokollanalyse.
     */
    @FXML
    public void event() {
        TreeItem<String> selectedItem = stringTreeView.getSelectionModel().getSelectedItem();

        LoadingAnimation ps = new LoadingAnimation();
        if (selectedItem.getValue().contains(".csv")) {

            // Thread für Progress Indicator
            Runnable progressTask = () -> Platform.runLater(LoadingAnimation::startProgress);
            new Thread(progressTask).start();

            // Thread für Analyse
            Runnable r = new Runnable() {
                int returnValue = 2;

                public void run() {
                    Platform.runLater(() -> {
                        ps.stopProgress();
                        if (returnValue == 200) {
                            txtSelectedFile.setText(fileSelected);
                            txtClientsCounter.setText(String.valueOf(userMap.size()));
                            // -1 because of csv header
                            txtPDUCounter.setText(String.valueOf(PDUCounter-1));
                            tableView.setItems(data);

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Es ist ein Fehler aufgetreten!");
                            alert.setHeaderText("Fehlerbehandlung (Fehlercode = 95)");
                            alert.setContentText("Bei der Analyse des Log-Files ist ein Fehler aufgetreten.");
                            alert.showAndWait();
                        }
                    });
                }
            };

            Thread cdThread = new Thread(r);
            cdThread.setName("Analyse");
            cdThread.start();
        }

    }

    /**
     * analyse each row of the csv file and handle the information, depends on the PDU type
     *
     * @param selectedItem selected Item from TreeView
     * @return int 200 for success, 400 else
     */
    private int analyse(TreeItem<String> selectedItem) {
        String path = "/" + selectedItem.getParent().getValue() + "/" + selectedItem.getValue();
        fileSelected = path;

        String line;
        String cvsSplitBy = ";";
        userMap = new HashMap<>();
        data.clear();
        PDUCounter = 0;

        try {
            FileReader fr = new FileReader(System.getProperty("user.dir") + path);
            try (BufferedReader br = new BufferedReader(fr)) {
                while ((line = br.readLine()) != null) {

                    String[] pdu = line.split(cvsSplitBy);

                    if (pdu[0].equals(AuditLogPduType.LOGIN_REQUEST.getDescription())) {
                        if (!userMap.containsKey(pdu[3])) {
                            ItemsForTableView ti = new ItemsForTableView(
                                    pdu[3],
                                    0,
                                    1,
                                    0,
                                    0,
                                    0,
                                    pdu[4],
                                    "",
                                    ""
                            );
                            userMap.put(pdu[3], ti);
                        } else {
                            ItemsForTableView ti = userMap.get(pdu[3]);
                            ti.setLoginTime(pdu[4]);
                            ti.setPDULoginCounter(ti.getPDULoginCounter() + 1);
                            userMap.put(pdu[3], ti);
                        }
                    } else if (pdu[0].equals(AuditLogPduType.LOGOUT_REQUEST.getDescription())) {
                        ItemsForTableView ti = userMap.get(pdu[3]);
                        ti.setLogoutTime(pdu[4]);
                        ti.setPDULogoutCounter(ti.getPDULogoutCounter() + 1);
                        userMap.put(pdu[3], ti);
                    } else if (pdu[0].equals(AuditLogPduType.CHAT_MESSAGE_REQUEST.getDescription())) {
                        ItemsForTableView ti = userMap.get(pdu[3]);
                        ti.setPDUMessageCounter(ti.getPDUMessageCounter() + 1);
                        userMap.put(pdu[3], ti);
                    } else if (pdu[0].equals(AuditLogPduType.FINISH_AUDIT_REQUEST.getDescription())) {
                        ItemsForTableView ti = userMap.get(pdu[3]);
                        ti.setPDUFinishCounter(ti.getPDUFinishCounter() + 1);
                        userMap.put(pdu[3], ti);
                    } else if (pdu[0].equals(AuditLogPduType.UNDEFINED.getDescription())) {
                        ItemsForTableView ti = userMap.get(pdu[3]);
                        ti.setPDUUndefinedCounter(ti.getPDUUndefinedCounter() + 1);
                        userMap.put(pdu[3], ti);
                    }
                    PDUCounter++;
                }

            }
            return 200;
        } catch (IOException e ) {
            setErrorMessage();
            ExceptionHandler.logException(e);
        }
        return 400;
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
        super.stop();
    }

    public void setErrorMessage() {
        log.debug("errorMessage: ");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Es ist ein Fehler im AdministratorGUI aufgetreten");
            // Der Verbindungsversuch ist aufgrund eines temporären Fehlers fehlgeschlagen. Wiederholen Sie den Vorgang.
            // https://support.microsoft.com/de-de/topic/liste-der-m%C3%B6glicherweise-angezeigten-fehlercodes-beim-aufbauen-einer-df%C3%BC-verbindung-oder-vpn-verbindung-in-windows-vista-8650caae-20e4-7720-f7c5-cca924488e91
            alert.setHeaderText("Fehlerbehandlung (Fehlercode = 774");
            alert.setContentText("Der Verbindungsversuch ist aufgrund eines temporären Fehlers fehlgeschlagen. Wiederholen Sie den Vorgang.");
            alert.showAndWait();
        });
    }


    public ClientModel getModel() {
        return model;
    }

}
