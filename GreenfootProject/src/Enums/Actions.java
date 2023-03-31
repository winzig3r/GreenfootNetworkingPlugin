package Enums;

public enum Actions {
    /**
     * Sent Downstream to inform a client that a TCP connection has been established successfully, Triggers an Upstream Handshake message over UDP
     * Sent Upstream to identify the UDP connection of a client to a server
     */
    HANDSHAKE,
    /**
     * Sent Upstream to inform the server about a position about of a certain actor
     * Informed Downstream to update the position on all other clients except the client who sent the message in the first place
     */
    UPDATE_POSITION,
    /**
     *
     */
    UPDATE_ROTATION,
    /**
     *
     */
    UPDATE_IMAGE,
    /**
     *
     */
    ADD_ACTOR,
    /**
     *
     */
    CREATE_ACTOR,
    /**
     *
     */
    REMOVE_ACTOR,
    /**
     *
     */
    ADD_WORLD,
    /**
     *
     */
    UNKNOWN,
    UPDATE_ACTOR_ID
}
