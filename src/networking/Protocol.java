package networking;

public final class Protocol {
    public final static int PORT = 8000;
    public final static String HOST = "10.201.194.154";

    /**
     * PROTOCOL
     * Special strings, which server can send us.
     * They are not shown in a console.
     */
    static final String SERVER_ACCEPT_CONNECTION = "AC:+";
    static final String SERVER_DECLINE_CONNECTION = "EX:0";
    static final String SERVER_ACKNOWLEDGE_ONLINE = "\u00061";
    static final String SERVER_USERS_ONLINE_STREAM = "\u00021";
    static final String SERVER_MISSED_MESSAGES_STREAM = "\u00022";
    static final String SERVER_IMAGE_STREAM = "\u0002#";
    static final String SERVER_END_OF_STREAM = "\u0004";
    static final String REQUEST_LOGIN = "\u0002*";
    static final String GET_USERS_ONLINE = "ON:L";
    static final String GET_MISSED_MESSAGES = "MS:0";
}
