/*
 * @#NetworkServiceCheckInDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.NetworkServiceSessionDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceSession</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkServiceSessionDao extends AbstractBaseDao<NetworkServiceSession> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkServiceSessionDao.class));

    /**
     * Constructor
     */
    public NetworkServiceSessionDao(){
        super(NetworkServiceSession.class);
    }


    /**
     * Check in a Network Service and associate with the session
     *
     * @param session
     * @param networkServiceProfile
     */
    public void checkIn(Session session, NetworkServiceProfile networkServiceProfile) throws CantInsertRecordDataBaseException {

        LOG.info("Executing checkIn(" + session.getId() + ", " + networkServiceProfile.getIdentityPublicKey() + ")");
        LOG.info("type = " + networkServiceProfile.getNetworkServiceType() + ")");


        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

                NetworkServiceSession networkServiceSession;

                /*
                 * Verify is exist the current session for the same client and ns
                 */
                Map<String, Object> filters = new HashMap<>();
                filters.put("networkService.id", networkServiceProfile.getIdentityPublicKey());
                filters.put("sessionId", session.getId());
                List<NetworkServiceSession> list = list(filters);
                if ((list != null) && (!list.isEmpty())){
                    networkServiceSession = list.get(0);
                    networkServiceSession.setNetworkService(new NetworkService(networkServiceProfile));
                    connection.merge(networkServiceSession);
                }else {
                    networkServiceSession = new NetworkServiceSession(session, networkServiceProfile);
                    connection.persist(networkServiceSession);
                }

                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(networkServiceProfile.getIdentityPublicKey(), networkServiceProfile.getNetworkServiceType().toString(), ProfileTypes.NETWORK_SERVICE, RegistrationType.CHECK_IN, RegistrationResult.SUCCESS, "");
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
     * Check out a specific Network Service associate with the session
     *
     * @param session
     * @param networkServiceProfile
     * @throws CantDeleteRecordDataBaseException
     */
    public void checkOut(Session session, NetworkServiceProfile networkServiceProfile) throws CantDeleteRecordDataBaseException {

        LOG.info("Executing checkOut(" + session.getId() + ", " + networkServiceProfile.getIdentityPublicKey() + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            Map<String, Object> filters = new HashMap<>();
            filters.put("sessionId", session.getId());
            filters.put("networkService.id", networkServiceProfile.getIdentityPublicKey());
            List<NetworkServiceSession> list = list(filters);

            if ((list != null) && (!list.isEmpty())) {
                connection.remove(connection.contains(list.get(0)) ? list.get(0) : connection.merge(list.get(0)));
                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(list.get(0).getNetworkService().getId(), list.get(0).getNetworkService().getNetworkServiceType().toString(), ProfileTypes.NETWORK_SERVICE, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "");
                connection.persist(profileRegistrationHistory);
            }

            //Delete client session geolocation
            Query queryNetworkServiceGeolocationDelete = connection.createQuery("DELETE FROM GeoLocation gl WHERE gl.id = :id");
            queryNetworkServiceGeolocationDelete.setParameter("id", networkServiceProfile.getIdentityPublicKey());
            int deletedClientGeoLocation = queryNetworkServiceGeolocationDelete.executeUpdate();

            LOG.info("deleted Network service geolocation = " + deletedClientGeoLocation);

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
}
