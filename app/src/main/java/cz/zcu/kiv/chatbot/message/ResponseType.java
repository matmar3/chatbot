package cz.zcu.kiv.chatbot.message;

public enum ResponseType {

    TEXT,
    OPTION,
    NONE;

    public static ResponseType resolve(String type) {
        for(ResponseType t: values()) {
            if (type.equalsIgnoreCase(t.toString())) {
                return t;
            }
        }

        return ResponseType.NONE;
    }

}
