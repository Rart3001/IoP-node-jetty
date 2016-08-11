/*
 * @#ActorSession.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.sql.Timestamp;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorSession</code>
 * represent the session of the a actor into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@NamedQueries({
    @NamedQuery(name="ActorSession.getAllCheckedInActorsByActorType", query="SELECT a from ActorSession a WHERE a.actor.actorType = :type"),
    @NamedQuery(name="ActorSession.getAllCheckedInActors",            query="SELECT a from ActorSession a"),
    @NamedQuery(name="ActorSession.isOnline"        ,                 query="SELECT a FROM ActorSession a WHERE a.actor.id = :id"),
})
public class ActorSession extends AbstractBaseEntity<Long>{

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
     * Represent the actor
     */
    @NotNull
    @OneToOne @MapsId
    private ActorCatalog actor;

    /**
     * Represent the timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

    /**
     * Constructor with parameters
     *
     * @param session
     * @param actor
     */
    public ActorSession(Session session, ActorCatalog actor) {
        this.id = null;
        this.sessionId = session.getId();
        this.actor = actor;
        actor.setSession(this);
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
     * Get the actor
     * @return ActorCatalog
     */
    public ActorCatalog getActor() {
        return actor;
    }

    /**
     * Set the actor
     * @param actor
     */
    public void setActor(ActorCatalog actor) {
        this.actor = actor;
        if (actor.getSession() != this)
            this.actor.setSession(this);
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
        if (!(o instanceof ActorSession)) return false;

        ActorSession that = (ActorSession) o;

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
        final StringBuilder sb = new StringBuilder("ActorSession{");
        sb.append("id='").append(id).append('\'');
        sb.append(", actor=").append((actor != null ? actor.getId() : null));
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
