package networking;

public final class Protocol {
    public final static int PORT = 8000;
    public final static String HOST = "10.201.194.154";

    /**
     * PROTOCOL
     * Special strings, which server can send us.
     * They are not shown in a console.
     */
    static final String ACCEPT_CONNECTION = "AC:+";
    static final String DECLINE_CONNECTION = "EX:0";
    static final String START_OF_CONNECTED_USERS_STREAM = "ST:R";
    static final String END_OF_STREAM = "EN:0";
    static final String UPDATE_USERS = "UP:A";
    static final String IMAGE_STRING = "IM:G#";
}
