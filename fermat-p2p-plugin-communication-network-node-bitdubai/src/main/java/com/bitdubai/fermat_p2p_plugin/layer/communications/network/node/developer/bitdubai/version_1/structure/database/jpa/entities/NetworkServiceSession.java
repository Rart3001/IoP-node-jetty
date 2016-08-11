/*
 * @#NetworkServiceSession.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.sql.Timestamp;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceSession</code> is
 * represent the session of a network service into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@NamedQueries({
    @NamedQuery(name="NetworkServiceSession.isOnline"        , query="SELECT n from NetworkServiceSession n where n.networkService.id = :id"),
})
public class NetworkServiceSession extends AbstractBaseEntity<Long>{

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the id
     */
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * Represent the sessionId
     */
    private String sessionId;

    /**
     * Represent the networkServiceProfile
     */
    @NotNull
    @MapsId
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.DETACH}, orphanRemoval=true, targetEntity = NetworkService.class)
    private NetworkService networkService;

    /**
     * Represent the timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

    /**
     * Constructor
     */
    public NetworkServiceSession() {
        super();
        this.id = null;
        this.sessionId = "";
        this.networkService = null;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor  with parameter
     */
    public NetworkServiceSession(Session session) {
        super();
        this.id = null;
        this.sessionId = session.getId();
        this.networkService = null;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor with parameters
     *
     * @param session
     * @param networkServiceProfile
     */
    public NetworkServiceSession(Session session, NetworkServiceProfile networkServiceProfile) {
        this.id = null;
        this.sessionId = session.getId();
        this.networkService = new NetworkService(networkServiceProfile);
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the value of sessionId
     *
     * @return sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Set the value of sessionId
     *
     * @param sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Get the value of networkService
     *
     * @return networkService
     */
    public NetworkService getNetworkService() {
        return networkService;
    }

    /**
     * Set the value of networkService
     *
     * @param networkService
     */
    public void setNetworkService(NetworkService networkService) {
        this.networkService = networkService;
    }

    /**
     * Get the timestamp
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp
     * @param timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkServiceSession)) return false;

        NetworkServiceSession that = (NetworkServiceSession) o;

        return getId().equals(that.getId());

    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * (non-javadoc)
     *
     * @see AbstractBaseEntity@toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NetworkServiceSession{");
        sb.append("id='").append(id).append('\'');
        sb.append(", networkService=").append((networkService != null ? networkService.getId() : null));
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
