/*
 * @#NetworkServiceDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.JPANamedQuery;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.NetworkServiceDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkServiceDao extends AbstractBaseDao<NetworkService> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkServiceDao.class));

    /**
     * Constructor
     */
    public NetworkServiceDao(){
        super(NetworkService.class);
    }

    public void deleteAllNetworkServiceGeolocation() throws CantDeleteRecordDataBaseException {

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            List<NetworkService> networkServiceList = list();

            for (NetworkService networkServicePk: networkServiceList) {
                Query deleteQuery = connection.createQuery("DELETE FROM GeoLocation gl WHERE gl.id = :id");
                deleteQuery.setParameter("id", networkServicePk.getId());
                deleteQuery.executeUpdate();
            }

            transaction.commit();
            connection.flush();

        }catch (Exception e){
            LOG.error(e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantDeleteRecordDataBaseException(e, "Network Node", "");
        }finally {
            connection.close();
        }
    }

    /**
     * Get the session id
     * @param networkServiceId
     * @return String
     * @throws CantReadRecordDataBaseException
     */
    public String getSessionId(String networkServiceId) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getSessionId(" + networkServiceId + ")");
        EntityManager connection = getConnection();

        try {

            TypedQuery<String> query = connection.createQuery("SELECT ns.session.id FROM NetworkService ns WHERE ns.id = :id ORDER BY ns.session.timestamp DESC", String.class);
            query.setParameter("id", networkServiceId);
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

    /**
     * Count all network service online
     * @return Long
     * @throws CantReadRecordDataBaseException
     */
    public Long countOnLine() throws CantReadRecordDataBaseException {

        EntityManager connection = getConnection();
        try {

           TypedQuery<Long> query = connection.createNamedQuery("NetworkService.countOnline", Long.class);
           return query.getSingleResult();

        }catch (Exception e){
            LOG.error(e);
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        }finally {
            connection.close();
        }
    }

    /**
     * Count all network service online by type
     *
     * @return Long
     * @throws CantReadRecordDataBaseException
     */
    public List<Object[]> countOnLineByType() throws CantReadRecordDataBaseException {

        EntityManager connection = getConnection();
        try {

            TypedQuery<Object[]> query = connection.createNamedQuery("NetworkService.countOnlineByType", Object[].class);
            return query.getResultList();

        }catch (Exception e){
            LOG.error(e);
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        }finally {
            connection.close();
        }
    }

    /**
     * Get all network service type for a online client
     *
     * @return List<NetworkServiceType>
     * @throws CantReadRecordDataBaseException
     */
    public List<NetworkServiceType> listTypeOnLineByClient(String clientId) throws CantReadRecordDataBaseException {

        EntityManager connection = getConnection();
        try {

            TypedQuery<NetworkServiceType> query = connection.createNamedQuery("NetworkService.listTypeOnLineByClient", NetworkServiceType.class);
            return query.getResultList();

        }catch (Exception e){
            LOG.error(e);
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        }finally {
            connection.close();
        }
    }

    /**
     * Get all Network Service online
     *
     * @return Long
     * @throws CantReadRecordDataBaseException
     */
    public List<NetworkService> listOnline() throws CantReadRecordDataBaseException {

        EntityManager connection = getConnection();
        try {

            TypedQuery<NetworkService> query = connection.createNamedQuery("NetworkService.getAllCheckedIn", NetworkService.class);
            return query.getResultList();

        }catch (Exception e){
            LOG.error(e);
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        }finally {
            connection.close();
        }
    }

}
