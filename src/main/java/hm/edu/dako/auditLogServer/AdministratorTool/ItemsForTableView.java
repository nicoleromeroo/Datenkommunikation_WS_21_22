package hm.edu.dako.auditLogServer.AdministratorTool;

public class ItemsForTableView {
    private String clientName;
    private int PDULoginCounter;
    private int PDULogoutCounter;
    private String loginTime;
    private String logoutTime;
    private int PDUMessageCounter;
    private int PDUUndefinedCounter;
    private int PDUFinishCounter;


    /**
     *
     * @param clientName Client-Name
     * @param PDULoginCounter Zaehler fuer PDUs Typ "Login"
     * @param PDULogoutCounter Zaehler fuer PDUs Typ "Logout"
     * @param loginTime login Zeit von Client
     * @param logoutTime logout Zeit von Client
     * @param PDUMessageCounter Zaehler fuer PDUs Typ "Chat"
     * @param PDUUndefinedCounter Zaehler fuer PDUs Typ "Undefined"
     * @param PDUFinishCounter Zaehler fuer PDUs Typ "Finish"
     */
    public ItemsForTableView(String clientName,
                     int PDUMessageCounter,
                     int PDULoginCounter,
                     int PDULogoutCounter,
                     int PDUUndefinedCounter,
                     int PDUFinishCounter,
                     String loginTime,
                     String logoutTime,
                     String estimatedTime) {
        this.clientName = clientName;
        this.PDUMessageCounter = PDUMessageCounter;
        this.PDULoginCounter = PDULoginCounter;
        this.PDULogoutCounter = PDULogoutCounter;
        this.PDUUndefinedCounter = PDUUndefinedCounter;
        this.PDUFinishCounter = PDUFinishCounter;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    /**
     * returns the name of client
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * set the name of the client
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * returns the number of sent PDUs of type "Chat"
     * @return int
     */
    public int getPDUMessageCounter() {
        return PDUMessageCounter;
    }

    /**
     * set number of sent PDUs of tpe "Chat"
     */
    public void setPDUMessageCounter(int PDUMessageCounter) {
        this.PDUMessageCounter = PDUMessageCounter;
    }

    /**
     * returns the number of sent PDUs of type "Login"
     * @return int
     */
    public int getPDULoginCounter() {
        return PDULoginCounter;
    }

    /**
     * set number of sent PDUs of tpe "Login"
     */
    public void setPDULoginCounter(int PDULoginCounter) {
        this.PDULoginCounter = PDULoginCounter;
    }

    /**
     * returns the number of sent PDUs of type "Logout"
     * @return int
     */
    public int getPDULogoutCounter() {
        return PDULogoutCounter;
    }

    /**
     * set number of sent PDUs of tpe "Logout"
     */
    public void setPDULogoutCounter(int PDULogoutCounter) {
        this.PDULogoutCounter = PDULogoutCounter;
    }

    /**
     * returns the number of sent PDUs of type "Undefined"
     * @return int
     */
    public int getPDUUndefinedCounter() {
        return PDUUndefinedCounter;
    }

    /**
     * set number of sent PDUs of tpe "Undefined"
     */
    public void setPDUUndefinedCounter(int PDUUndefinedCounter) {
        this.PDUUndefinedCounter = PDUUndefinedCounter;
    }

    /**
     * returns the number of sent PDUs of type "Finish"
     * @return int
     */
    public int getPDUFinishCounter() {
        return PDUFinishCounter;
    }

    /**
     * set number of sent PDUs of tpe "Finish"
     */
    public void setPDUFinishCounter(int PDUFinishCounter) {
        this.PDUFinishCounter = PDUFinishCounter;
    }

    /**
     * returns the login time of client
     * @return String
     */
    public String getLoginTime() {
        return loginTime;
    }

    /**
     * set login time of client
     */
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * returns the logout time of client
     * @return String
     */
    public String getLogoutTime() {
        return logoutTime;
    }

    /**
     * set the logout Time of client
     */
    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

}
