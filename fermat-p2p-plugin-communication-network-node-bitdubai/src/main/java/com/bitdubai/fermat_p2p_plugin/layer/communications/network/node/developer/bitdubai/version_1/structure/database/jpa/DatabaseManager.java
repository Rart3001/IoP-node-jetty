/*
 * @#DatabaseManager.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.*;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ProviderResourcesFilesPath;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager</code> are the
 * database manager class
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class DatabaseManager {

    /**
     * Represent the LOG
     */
    private static final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(DatabaseManager.class));

    /**
     * Represent the value of DIR_NAME
     */
    public static final String DIR_NAME = "database";

    /**
     * Represent the value of DIR_NAME
     */
    public static final String DATA_BASE_NAME = "network_node.odb";

    /**
     * Represent the entityManagerFactory instance
     */
    private static final EntityManagerFactory entityManagerFactory;

    static {

        String path = ProviderResourcesFilesPath.createNewFilesPath(DIR_NAME);
        System.setProperty("objectdb.home", path);
        System.setProperty("objectdb.temp.avoid-page-recycle", "true");


        /*
         * Configure the database properties
         * TODO: GET THIS VALUES FROM CONFIGURATION FILE
         */
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.user", "admin");
        properties.put("javax.persistence.jdbc.password", "admin");
        properties.put("javax.jdo.option.MinPool", "50");
        properties.put("javax.jdo.option.MaxPool", "200");
        properties.put("javax.persistence.sharedCache.mode", "DISABLE_SELECTIVE");

        LOG.info("Open a database connection (create a new database if it doesn't exist yet)");
        entityManagerFactory = Persistence.createEntityManagerFactory(path.concat(DATA_BASE_NAME), properties);

        /*
         * Load the tables
         */
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getMetamodel().entity(ActorCatalog.class);
        entityManager.getMetamodel().entity(Client.class);
        entityManager.getMetamodel().entity(ClientSession.class);
        entityManager.getMetamodel().entity(GeoLocation.class);
        entityManager.getMetamodel().entity(MethodCallsHistory.class);
        entityManager.getMetamodel().entity(NetworkService.class);
        entityManager.getMetamodel().entity(NodeCatalog.class);
        entityManager.getMetamodel().entity(ProfileRegistrationHistory.class);
        entityManager.close();

    }


    /**
     * Get a new instance to a Entity manager that
     * represent a connection with the data base.
     *
     * @return EntityManager
     */
    public static EntityManager getConnection() {

        if (entityManagerFactory != null){
            return entityManagerFactory.createEntityManager();
        }else {
            throw new RuntimeException("Cant get Connection, entityManagerFactory = "+ null);
        }

    }

    /**
     * Close de data base
     */
    public static void closeDataBase(){

        if (entityManagerFactory != null && entityManagerFactory.isOpen()){
            entityManagerFactory.close();
        }
    }

    /**
     * Get the EntityManagerFactory value
     *
     * @return EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
