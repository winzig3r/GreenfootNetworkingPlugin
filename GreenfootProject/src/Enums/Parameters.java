package Enums;

/**
 * All the parameters that are used in up and downstream json Messages
 * @see #Action
 * @see #ClientId
 */
public enum Parameters {
    /**
     * Defines the Action a message represents, All actions have to be of type Actions
     */
    Action,
    /**
     * Always an Integer, used up- and downstream, represents the id of the client sending the message or receiving the message (e.g. Handshake Packet)
     */
    ClientId
}
