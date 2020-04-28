package cz.zcu.kiv.chatbot.message;

/**
 * List of possible owners of the message.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-27-04
 */
public enum MessageOwner {

    /**
     * Initial message that is not visible.
     */
    INIT_MESSAGE("1"),

    /**
     * Owner of messages sent from client's application.
     */
    CLIENT("100"),

    /**
     * Owner of messages received from assistant.
     */
    ASSISTANT("2");

    /**
     * Owner identification in string form.
     */
    private String userID;

    MessageOwner(String userID) {
        this.userID = userID;
    }

    /**
     * Resolves given userID and returns its equivalent as {@link MessageOwner}. If owner is not
     * recognized, method returns {@code null} type.
     *
     * @param userID - owner's userID
     * @return - resolved owner's type
     */
    public static MessageOwner resolve(String userID) {
        for(MessageOwner identity: values()) {
            if (identity.userID.equalsIgnoreCase(userID)) {
                return identity;
            }
        }

        return null;
    }

    /**
     * Returns owner's userID.
     * @return - owner's userID
     */
    public String getUserID() {
        return userID;
    }

}
