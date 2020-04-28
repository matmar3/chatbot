package cz.zcu.kiv.chatbot.message;

/**
 * Defines possible types of responses that assistant can generate.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-27-04
 */
public enum ResponseType {

    /**
     * Standard plaintext message.
     */
    TEXT,

    /**
     * List of options.
     */
    OPTION,

    /**
     * Unrecognized response type.
     */
    NONE;

    /**
     * Resolves given type and returns its equivalent as {@link ResponseType}. If type is not
     * recognized, method returns {@code NONE} type.
     *
     * @param type - possible response type
     * @return - resolved response type
     */
    public static ResponseType resolve(String type) {
        for(ResponseType t: values()) {
            if (type.equalsIgnoreCase(t.toString())) {
                return t;
            }
        }

        return ResponseType.NONE;
    }

}
