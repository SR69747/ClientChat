package networking;

public final class Protocol {
    public final static int PORT = 8000;
    public final static String HOST = "10.201.194.154";

    /**
     * PROTOCOL
     * Special strings, which server can send us or we can send to server.
     * They are not shown in a console.
     */
    static final String SERVER_ACCEPT_CONNECTION = "AC:+";
    static final String SERVER_DECLINE_CONNECTION = "EX:0";
    static final String SERVER_ACKNOWLEDGE_DATABASE_UPDATE = "DA:B";
    static final String SERVER_STREAM_USERS_ONLINE = "US:O";
    static final String SERVER_STREAM_MISSED_MESSAGES = "MI:M";
    static final String SERVER_END_OF_STREAM = "\u0004";
    static final String SERVER_IMAGE_STRING = "IM:\u0003";

    static final String SEND_MESSAGE_TO = "T@>\u0003";
    static final String REQUEST_LOGIN = "\u0002\u0003";
    static final String REQUEST_USERS_ONLINE = "ON:L";
    static final String REQUEST_MISSED_MESSAGES = "MS:0";
    //static final String REQUEST_REGISTRATION = "\u0001\u0003";

}
