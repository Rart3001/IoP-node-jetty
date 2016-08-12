/*
 * @#ClientSession.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession</code> is
 * represent the session of a client into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@NamedQueries({
        @NamedQuery(name="ClientSession.isClientOnline",query="SELECT c from ClientSession c where c.client.id = :id"),
        @NamedQuery(name="ClientSession.getCheckedInClient", query = "SELECT c from ClientSession c"),
        @NamedQuery(name="ClientSession.isOnline", query="SELECT c FROM ClientSession c WHERE c.client.id = :id AND c.client.status = ProfileStatus.ONLINE")
})
public class ClientSession extends AbstractBaseEntity<String> {

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the id
     */
    @Id
    @NotNull
    private String id;

    /**
     * Represent the client
     */
    @NotNull
    @MapsId
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.DETACH}, targetEntity = Client.class)
    private Client client;

    /**
     * Represent the networkServices
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = NetworkService.class, orphanRemoval = true)
    private Set<NetworkService> networkServices;

    /**
     * Represent the actorCatalogs
     */
    @OneToMany(fetch = FetchType.LAZY, targetEntity = ActorCatalog.class, mappedBy = "session")
    private Set<ActorCatalog> actorCatalogs;

    /**
     * Represent the timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

    /**
     * Constructor
     */
    public ClientSession() {
        super();
        this.id = "";
        this.client = null;
        this.networkServices = new HashSet<>();
        this.actorCatalogs = new HashSet<>();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor with parameter
     */
    public ClientSession(Session session) {
        super();
        this.id = session.getId();
        this.client = null;
        this.networkServices = new HashSet<>();
        this.actorCatalogs = new HashSet<>();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor with parameters
     *
     * @param session
     * @param clientProfile
     */
    public ClientSession(Session session, Client clientProfile) {
        this.id = session.getId();
        this.client = clientProfile;
        this.networkServices = new HashSet<>();
        this.actorCatalogs = new HashSet<>();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * (non-javadoc)
     *
     * @see AbstractBaseEntity@getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the client
     *
     * @return client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Set the networkService
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Get the NetworkServices
     * @return networkServices
     */
    public Set<NetworkService> getNetworkServices() {
        return networkServices;
    }

    /**
     * Set the NetworkServices
     * @param networkServices
     */
    public void setNetworkServices(Set<NetworkService> networkServices) {
        this.networkServices = networkServices;
    }

    /**
     * Get the ActorCatalogs
     * @return actorCatalogs
     */
    public Set<ActorCatalog> getActorCatalogs() {
        return actorCatalogs;
    }

    /**
     * Set the ActorCatalogs
     * @param actorCatalogs
     */
    public void setActorCatalogs(Set<ActorCatalog> actorCatalogs) {
        this.actorCatalogs = actorCatalogs;
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
        if (!(o instanceof ClientSession)) return false;

        ClientSession that = (ClientSession) o;

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
        final StringBuilder sb = new StringBuilder("ClientSession{");
        sb.append("id='").append(id).append('\'');
        sb.append(", client=").append((client != null ? client.getId() : null));
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
