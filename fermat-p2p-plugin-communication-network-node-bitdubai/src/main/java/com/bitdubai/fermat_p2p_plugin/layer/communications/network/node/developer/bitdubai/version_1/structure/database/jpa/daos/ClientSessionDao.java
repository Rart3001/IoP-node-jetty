/*
 * @#ClientCheckInDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ClientProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ClientSessionDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientSessionDao extends AbstractBaseDao<ClientSession>{

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ClientSessionDao.class));

    /**
     * Constructor
     */
    public ClientSessionDao(){
        super(ClientSession.class);
    }

    /**
     * Check in a client and associate with the session
     *
     * @param session
     * @param clientProfile
     */
    public void checkIn(Session session, ClientProfile clientProfile) throws CantInsertRecordDataBaseException {

        LOG.info("Executing checkIn(" + session.getId() + ", " + clientProfile.getIdentityPublicKey() + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

                ClientSession clientSession = new ClientSession(session, new Client(clientProfile));

                /*
                 * Verify is exist the current session for the same client
                 */
                if (exist(connection, session.getId())){
                    connection.merge(clientSession);
                }else {
                    connection.persist(clientSession);
                }

                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(clientProfile.getIdentityPublicKey(), clientProfile.getDeviceType(), ProfileTypes.CLIENT, RegistrationType.CHECK_IN, RegistrationResult.SUCCESS, "");
                connection.persist(profileRegistrationHistory);

            transaction.commit();
            connection.flush();

        }catch (Exception e){
            LOG.error(e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantInsertRecordDataBaseException(CantInsertRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Check out a client associate with the session, and check out all network services and
     * actors associate with this session too
     *
     * @param sessionId
     */
    public void checkOut(String sessionId){

        LOG.debug("Executing checkOut("+sessionId+")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            ClientSession clientSession = connection.find(ClientSession.class, sessionId);

            if (clientSession != null){

                transaction.begin();

                LOG.info("Client = "+(clientSession.getClient() != null ? clientSession.getClient().getId() : 0));
                LOG.info("NetworkServices = "+(clientSession.getNetworkServices() != null ? clientSession.getNetworkServices().size() : 0));
                LOG.info("ActorCatalogs = "+(clientSession.getActorCatalogs() != null ? clientSession.getActorCatalogs().size() : 0));

                for (ActorCatalog actorCatalog: clientSession.getActorCatalogs()) {
                    actorCatalog.setSession(null);
                    connection.merge(actorCatalog);
                }

                connection.remove(connection.contains(clientSession) ? clientSession : connection.merge(clientSession));

                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(clientSession.getClient().getId(), clientSession.getClient().getDeviceType(), ProfileTypes.CLIENT, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "Delete all network service and actor session associate with this client");
                connection.persist(profileRegistrationHistory);

                transaction.commit();
                connection.flush();
            }

        }catch (Exception e){
            LOG.error(e);
        }finally {
            connection.close();
        }

    }


    /**
     * Find a entity by his id
     *
     * @param id
     * @return Entity
     */
    public ClientSession findByClientId(String id) throws CantReadRecordDataBaseException {

        LOG.debug("Executing findById("+id+")");

        if (id == null){
            throw new IllegalArgumentException("The id can't be null");
        }

        EntityManager connection = getConnection();
        ClientSession entity = null;

        try {

            TypedQuery<ClientSession> query = connection.createQuery("SELECT s FROM ClientSession s WHERE s.client.id = :id ORDER BY s.timestamp DESC", ClientSession.class);
            query.setParameter("id", id);
            query.setMaxResults(1);
            entity = query.getSingleResult();
            entity.getActorCatalogs();
            entity.getNetworkServices();

        } catch (Exception e){
            LOG.error(e);
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

        return entity;

    }
}
