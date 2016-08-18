/*
 * @#ClientDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ClientDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientDao extends AbstractBaseDao<Client>{

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ClientDao.class));

    /**
     * Constructor
     */
    public ClientDao(){
        super(Client.class);
    }

    public void deleteAllClientGeolocation() throws CantDeleteRecordDataBaseException {

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            List<Client> clientList = list();

            for (Client clientPk: clientList) {
                Query deleteQuery = connection.createQuery("DELETE FROM GeoLocation gl WHERE gl.id = :id");
                deleteQuery.setParameter("id", clientPk.getId());
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
     * @param clientId
     * @return String
     * @throws CantReadRecordDataBaseException
     */
    public String getSessionId(String clientId) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getSessionId(" + clientId + ")");
        EntityManager connection = getConnection();

        try {

            TypedQuery<String> query = connection.createQuery("SELECT c.session.id FROM Client c WHERE c.id = :id ORDER BY c.session.timestamp DESC", String.class);
            query.setParameter("id", clientId);
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
