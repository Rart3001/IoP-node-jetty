/*
 * @#ActorCheckInDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ActorSessionDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorSession</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ActorSessionDao extends AbstractBaseDao<ActorSession> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorSessionDao.class));

    /**
     * Constructor
     */
    public ActorSessionDao(){
        super(ActorSession.class);
    }

    /**
     * Check in a actor and associate with the session
     *
     * @param session
     * @param actorCatalog
     */
    public void checkIn(Session session, ActorCatalog actorCatalog) throws CantInsertRecordDataBaseException {

        LOG.debug("Executing checkIn(" + session.getId() + ", " + actorCatalog.getId() + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            ActorSession actorSession;
            Map<String, Object> filters = new HashMap<>();
            filters.put("sessionId", session.getId());
            filters.put("actor.id", actorCatalog.getId());
            List<ActorSession> list = list(filters);

            if ((list != null) && (!list.isEmpty())){
                actorSession = list.get(0);
                actorSession.setActor(actorCatalog);
                connection.merge(actorSession);
            }else {
                actorSession = new ActorSession(session, actorCatalog);
                connection.persist(actorSession);
            }

            ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(actorCatalog.getId(), actorCatalog.getActorType(), ProfileTypes.ACTOR, RegistrationType.CHECK_IN, RegistrationResult.SUCCESS, "");
            connection.persist(profileRegistrationHistory);

            transaction.commit();

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
     * Check out a specific actor associate with the session
     *
     * @param session
     * @param actorProfile
     * @throws CantDeleteRecordDataBaseException
     */
    public void checkOut(Session session, ActorProfile actorProfile) throws CantDeleteRecordDataBaseException {

        LOG.debug("Executing checkOut(" + session.getId() + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

                /*
                 * Find previous or old session for the same client and actor, if
                 * exist delete
                 */
                Map<String, Object> filters = new HashMap<>();
                filters.put("actor.id", actorProfile.getIdentityPublicKey());
                List<ActorSession> oldSession = list(filters);
                LOG.info("oldSession = " + (oldSession != null ? oldSession.size() : 0));

                assert oldSession != null;
                for (ActorSession a : oldSession) {
                    connection.remove(a);
                }

                /*
                 * Verify is exist the current session for the same client and actor
                 */
                filters.put("sessionId", session.getId());
                List<ActorSession> list = list(filters);
                if ((list != null) && (!list.isEmpty())) {
                    connection.remove(connection.contains(list.get(0)) ? list.get(0) : connection.merge(list.get(0)));
                    ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(list.get(0).getActor().getId(), list.get(0).getActor().getActorType(), ProfileTypes.ACTOR, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "");
                    connection.persist(profileRegistrationHistory);
                }

            //Delete actor geolocation
            Query queryActorGeolocationDelete = connection.createQuery("DELETE FROM GeoLocation gl WHERE gl.id = :id");
            queryActorGeolocationDelete.setParameter("id", actorProfile.getIdentityPublicKey());
            int deletedActorSessionGeoLocation = queryActorGeolocationDelete.executeUpdate();

            LOG.info("deleted actor geolocation = " + deletedActorSessionGeoLocation);

            transaction.commit();

        } catch (Exception e) {
            LOG.error(e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantDeleteRecordDataBaseException(CantDeleteRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    /**
     * Get the session id for a actor
     *
     * @param actorID
     * @return string
     * @throws CantReadRecordDataBaseException
     */
    public String getSessionId(String actorID) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getSessionId(" + actorID + ")");
        EntityManager connection = getConnection();

        try {

            TypedQuery<String> query = connection.createQuery("SELECT s.sessionId FROM ActorSession s WHERE s.actor.id = :id ORDER BY timestamp DESC", String.class);
            query.setParameter("id", actorID);
            query.setMaxResults(1);

            List<String> ids = query.getResultList();
            return (ids != null && !ids.isEmpty() ? ids.get(0) : null);

        } catch (Exception e) {
            LOG.error(e);
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

}
