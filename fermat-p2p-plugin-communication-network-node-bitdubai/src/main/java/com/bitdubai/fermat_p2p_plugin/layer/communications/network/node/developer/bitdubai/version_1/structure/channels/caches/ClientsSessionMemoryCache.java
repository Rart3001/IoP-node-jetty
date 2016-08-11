package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches.ClientsSessionMemoryCache</code>
 * is responsible the manage the cache of the client session connected with the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.servers.FermatWebSocketClientChannelServerEndpoint</code><p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 27/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientsSessionMemoryCache {

    /**
     * Represent the singleton instance
     */
    private static ClientsSessionMemoryCache instance;

    /**
     * Holds all client sessions
     */
    private final Map<String , Session> clientSessionsById;

    /**
     * Constructor
     */
    private ClientsSessionMemoryCache() {
        clientSessionsById = new ConcurrentHashMap<>();
    }

    /**
     * Return the singleton instance
     *
     * @return ClientsSessionMemoryCache
     */
    public static ClientsSessionMemoryCache getInstance(){

        /*
         * If no exist create a new one
         */
        if (instance == null){
            instance = new ClientsSessionMemoryCache();
        }

        return instance;
    }

    /**
     * Get the session client
     *
     * @param sessionId the client identity
     * @return the session of the client
     */
    public Session get(String sessionId){

        /*
         * Return the session of this client
         */
        if (sessionId != null){
            return getInstance().clientSessionsById.get(sessionId);
        }else {
            return null;
        }

    }

    /**
     * Add a new session to the memory cache
     *
     * @param session the client session
     */
    public void add(final Session session){

        /*
         * Add to the cache
         */
        getInstance().clientSessionsById.put(session.getId(), session);
    }

    /**
     * Remove the session client
     *
     * @param session the session of the connection
     * @return the id of the session
     */
    public String remove(Session session){

        /*
         * remove the session of this client
         */

        getInstance().clientSessionsById.remove(session.getId());

        return session.getId();
    }

    /**
     * Verify is exist a session for a session id
     *
     * @param sessionId the session id
     * @return (TRUE or FALSE)
     */
    public boolean exist(String sessionId){

        return getInstance().clientSessionsById.containsKey(sessionId);
    }
}
