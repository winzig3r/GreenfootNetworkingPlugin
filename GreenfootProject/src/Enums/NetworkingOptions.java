package Enums;

/**
 * @see #AsServer
 * @see #AsClient
 * @see #AsServerClient
 */
public enum NetworkingOptions {
    /**
     * When creating a World this can be defined => Creating a Server
     */
    AsServer,
    /**
     * When creating a World this can be defined => Creating a Client
     */
    AsClient,
    /**
     * When creating a World this can be defined => Creating a Server and a Client
     */
    AsServerClient
}
