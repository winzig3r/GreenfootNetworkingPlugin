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
    ClientId,
    /**
     * Used by the clients to tell the other clients what actor should be updated if an update package like "sendPositionUpdate" is sent
     */
    ActorId,
    /**
     * Used to identify information about the world, that has to be somehow modified (e.g.: Adding / Removing an actor)
     */
    WorldId,
    /**
     * Used by the server to tell the clients the information about the newly added actor
     */
    NewActorInformation,
    /**
     * Used in the updated Position Packet sent by the client to determine the new x position of a given actor
     */
    NewXPosition,
    /**
     * Used in the updated Position Packet sent by the client to determine the new y position of a given actor
     */
    NewYPosition,
    /**
     * Used in the updated Rotation Packet sent by the client to determine the new rotation of a given actor
     */
    NewRotation,
    /**
     * Used gy the clients to inform others about a new image of a given client
     */
    NewImageFilePath,
    NewWorldInformation,
    WorldWidth,
    WorldHeight,
    CellSize,
    Bounded

}
