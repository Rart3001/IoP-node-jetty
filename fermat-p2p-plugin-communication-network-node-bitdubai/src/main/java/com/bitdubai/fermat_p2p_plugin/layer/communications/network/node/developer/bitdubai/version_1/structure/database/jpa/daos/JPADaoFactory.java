/*
 * @#JPADaoFactory.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;


/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory</code>
 * represent the Data Access Object factory
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public final class JPADaoFactory {

    /**
     * Get the value of actorCatalogDao
     *
     * @return actorCatalogDao
     */
    static public ActorCatalogDao getActorCatalogDao() {
        return new ActorCatalogDao();
    }

    /**
     * Get the value of actorSessionDao
     *
     * @return actorSessionDao
     */
    static public ActorSessionDao getActorSessionDao() {
        return new ActorSessionDao();
    }

    /**
     * Get the value of clientSessionDao
     *
     * @return clientSessionDao
     */
    static public ClientSessionDao getClientSessionDao() {
        return new ClientSessionDao();
    }

    /**
     * Get the value of clientDao
     *
     * @return clientDao
     */
    static public ClientDao getClientDao() {
        return new ClientDao();
    }

    /**
     * Get the value of methodCallsHistoryDao
     *
     * @return methodCallsHistoryDao
     */
    static public MethodCallsHistoryDao getMethodCallsHistoryDao() {
        return new MethodCallsHistoryDao();
    }

    /**
     * Get the value of networkServiceSessionDao
     *
     * @return networkServiceSessionDao
     */
    static public NetworkServiceSessionDao getNetworkServiceSessionDao() {
        return new NetworkServiceSessionDao();
    }

    /**
     * Get the value of networkServiceDao
     *
     * @return networkServiceDao
     */
    static public NetworkServiceDao getNetworkServiceDao() {
        return new NetworkServiceDao();
    }

    /**
     * Get the value of nodeCatalogDao
     *
     * @return nodeCatalogDao
     */
    static public NodeCatalogDao getNodeCatalogDao() {
        return new NodeCatalogDao();
    }

    static public GeoLocationDao getGeoLocationDao() {
        return new GeoLocationDao();
    }

}
