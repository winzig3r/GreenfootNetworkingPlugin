package Enums;

/**
 * All the parameters that are used in up and downstream json Messages
 * @see #Action
 * @see #ClientId
 * @see #ActorId
 * @see #WorldId
 * @see #NewActorInformation
 * @see #NewXPosition
 * @see #NewYPosition
 * @see #NewRotation
 * @see #NewImageFilePath
 * @see #NewWorldInformation
 * @see #WorldWidth
 * @see #WorldHeight
 * @see #CellSize
 * @see #Bounded
 * @see #AllCurrentActors
 * @see #OldActorId
 */
public enum Parameters {
    /**
     * Defines the Action a message represents, All actions have to be of type Action
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
     * Used by the clients to inform others about a new image of a given client
     */
    NewImageFilePath,
    /**
     * Used by the Client who created the Server to tell the Server what worlds are there
     * Used by the Server to tell new connecting clients about the worlds they can put their actors in
     * Watch out: This package is only used to inform the server about a new world and can NOT change the parameters of other clients worlds
     */
    NewWorldInformation,
    /**
     * Used in the New World Information Parameter to specify the World Width
     */
    WorldWidth,
    /**
     * Used in the New World Information Parameter to specify the World Height
     */
    WorldHeight,
    /**
     * Used in the New World Information Parameter to specify the Worlds Cell Size
     */
    CellSize,
    /**
     * Used in the New World Information Parameter to specify if the world is Bounded (true) or Unbounded (false)
     */
    Bounded,
    /**
     * Send Downstream with the Handshake to inform the new client about current Actors in the world
     */
    AllCurrentActors,
    /**
     *
     */
    OldActorId,

    RemoveCompletely;
}
