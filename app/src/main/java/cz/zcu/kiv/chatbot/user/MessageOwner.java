package cz.zcu.kiv.chatbot.user;

public enum MessageOwner {

    INIT_MESSAGE("1"),
    CLIENT("100"),
    ASSISTANT("2");

    private String userID;

    MessageOwner(String userID) {
        this.userID = userID;
    }

    public static MessageOwner resolve(String userID) {
        for(MessageOwner identity: values()) {
            if (identity.userID.equalsIgnoreCase(userID)) {
                return identity;
            }
        }

        return null;
    }

    public String getUserID() {
        return userID;
    }

}
