package de.webspired.Enums;

/**
 * @see #AsServer
 * @see #AsClient
 * @see #AsServerClient
 */
public enum NetworkingOptions {
    /**
     * When creating a World this can be defined => Creating a de.webspired.Server
     */
    AsServer,
    /**
     * When creating a World this can be defined => Creating a de.webspired.Client
     */
    AsClient,
    /**
     * When creating a World this can be defined => Creating a de.webspired.Server and a de.webspired.Client
     */
    AsServerClient
}
