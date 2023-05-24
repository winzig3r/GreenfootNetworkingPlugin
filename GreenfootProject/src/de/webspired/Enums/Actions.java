package de.webspired.Enums;

import de.webspired.GreenfootNetworking.NetworkedActor;
import greenfoot.Actor;

/**
 * @see #HANDSHAKE
 * @see #UPDATE_POSITION
 * @see #UPDATE_ROTATION
 * @see #UPDATE_IMAGE
 * @see #CREATE_ACTOR
 * @see #ADD_WORLD
 * @see #REQUEST_OTHER_ACTORS
 */
public enum Actions {
    /**
     * Parameters used Upstream:
     * {@link Parameters#ClientId} int <br>
     * Parameters used Downstream:
     * {@link Parameters#ClientId}, int, The client sending the message <br>
     * {@link Parameters#AllCurrentActors}, String, specifies all the actors currently spawned regardless of the world <br>  <br>
     * Sent Downstream to inform a client that a TCP connection has been established successfully, Triggers an Upstream Handshake message over UDP
     * Sent Upstream to identify the UDP connection of a client to a server
     */
    HANDSHAKE,
    /**
     * Parameters used Upstream & Downstream:
     * {@link Parameters#ClientId}, int, defines the client sending the message <br>
     * {@link Parameters#ActorId}, int, defines the actor id receiving position update <br>
     * {@link Parameters#NewXPosition}, int, defines the New XPosition of the actor <br>
     * {@link Parameters#NewYPosition}, int, defines the New YPosition of the actor <br>
     * Sent Upstream to inform the server about a position about of a certain actor
     * Informed Downstream to update the position on all other clients except the client who sent the message in the first place
     */
    UPDATE_POSITION,
    /**
     * Parameters used Upstream & Downstream:
     * {@link Parameters#ClientId}, int, defines the client sending the message <br>
     * {@link Parameters#ActorId}, int, defines the actor receiving the rotation update <br>
     * {@link Parameters#NewRotation}, int, defines the new Rotation of the client <br>
     * Sent Upstream to inform the server about a new rotation of a certain actor
     */
    UPDATE_ROTATION,
    /**
     * Parameters used Upstream & Downstream: <br>
     * {@link Parameters#ClientId}, int, defines the client sending the message <br>
     * {@link Parameters#ActorId}, int, defines the actor receiving the image update <br>
     * {@link Parameters#NewImageFilePath}, String <br>
     * Sent if an image got updated
     */
    UPDATE_IMAGE,
    /**
     * Parameters used Upstream & Downstream: <br>
     * {@link Parameters#NewActorInformation}, JObject, defines the information of the newly created actor {@link NetworkedActor#toJsonString()} <br>
     * {@link Parameters#ActorId}, int, defines the id of the actor being created <br>
     * {@link Parameters#WorldId}, int, defines the world an actor is in (-1 if in no world) <br>
     * {@link Parameters#NewImageFilePath}, String, The image path (if already set) <br>
     * {@link Parameters#NewXPosition}, int, The XPosition of the actor in the world if worldId > -1 <br>
     * {@link Parameters#NewYPosition}, int, The YPosition of the actor in the world if worldId > -1 <br>
     * {@link Parameters#ClientId}, int, defines the client sending the message <br>
     * Sent if an actor is created on a client, triggers a {@link Actions#CREATED_ACTOR} packet to be sent
     */
    CREATE_ACTOR,
    /**
     * Parameters used Downstream: <br>
     * {@link Parameters#ActorId}, int, defines the information of the newly created actor {@link NetworkedActor#toJsonString()} <br>
     * {@link Parameters#OldActorId}, int, defines the client sending the message <br>
     * Sent if the server created the client successfully after a {@link Actions#CREATE_ACTOR} packet.
     * Used to make the actor addable. If an actor is addable it can be added to world. {@link de.webspired.Client.ActorHandler} is used for that.
     */
    CREATED_ACTOR,
    /**
     * Parameters used Upstream & Downstream: <br>
     * {@link Parameters#ClientId}, int, The client adding the actor to the world <br>
     * {@link Parameters#ActorId}, int, The id of the actor that is added to the world <br>
     * {@link Parameters#WorldId}, int, The id of the world the actor is going to be added to <br>
     * {@link Parameters#NewXPosition}, int, The x position of the newly added actor (needed to call {@link greenfoot.World#addObject(Actor, int, int)}) <br>
     * {@link Parameters#NewYPosition}, int, The y position of the newly added actor (needed to call {@link greenfoot.World#addObject(Actor, int, int)}) <br>
     * This package is sent by a client to the server and from the server to all the clients. It informs them about an actor being added to the world
     */
    ADD_ACTOR_TO_WORLD,
    /**
     * Parameters used Upstream & Downstream: <br>
     * {@link Parameters#ClientId}, int, The client removing the actor from the world <br>
     * {@link Parameters#ActorId}, int, The id of the actor that is removed from the world <br>
     * {@link Parameters#WorldId}, int, The id of the world the actor is going to be removed from <br>
     * This package is sent by a client to the server and from the server to all the clients. It informs them about an actor being removed from the world
     */
    REMOVE_ACTOR_FROM_WORLD,
    /**
     * Parameters used Upstream: <br>
     * {@link Parameters#ClientId}, int, The id of the Client hosting the world => The first time this world is added, just to inform the server <br>
     * {@link Parameters#NewWorldInformation}, JObject, The information of the world specified by {@link de.webspired.GreenfootNetworking.NetworkedWorld#toJsonString()} <br>
     * Only used to inform the server, not sent to other clients as they have this world already by just starting the project (project is the same across all clients originally)
     */
    ADD_WORLD,
    /**
     * Parameters used Upstream: <br>
     * {@link Parameters#ActorId}, int, The id of the Client requesting all the current actors
     * Parameters used Downstream: <br>
     * {@link Parameters#AllCurrentActors}, String, specifies all the actors currently spawned regardless of the world <br>
     */
    REQUEST_OTHER_ACTORS,
    /**
     *
     */
    RESET

}
