/*
 * @#MethodCallsHistory.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.MethodCallsHistory</code>
 * represent the history of the calls
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@Cacheable(false)
public class MethodCallsHistory extends AbstractBaseEntity<Long> {

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
     * Represent the createTimestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	private Timestamp createTimestamp;

    /**
     * Represent the methodName
     */
    @NotNull
	private String methodName;

    /**
     * Represent the parameters
     */
	private String parameters;

    /**
     * Represent the profileIdentityPublicKey
     */
    @NotNull
	private String profileIdentityPublicKey;

    /**
     * Constructor
     */
    public MethodCallsHistory() {
        super();
        this.methodName = "";
        this.parameters = "";
        this.profileIdentityPublicKey = "";
        this.createTimestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor with parameters
     *
     * @param methodName
     * @param parameters
     * @param profileIdentityPublicKey
     */
    public MethodCallsHistory(String methodName, String parameters, String profileIdentityPublicKey) {
        super();
        this.methodName = methodName;
        this.parameters = parameters;
        this.profileIdentityPublicKey = profileIdentityPublicKey;
        this.createTimestamp = new Timestamp(System.currentTimeMillis());
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
     * Get the CreateTimestamp
     * @return createTimestamp
     */
    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    /**
     * Set the CreateTimestamp
     * @param createTimestamp
     */
    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    /**
     * Get the MethodName
     * @return methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Set the MethodName
     * @param methodName
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Get the Parameters
     * @return String
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * Set the Parameters
     * @param parameters
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * Get the ProfileIdentityPublicKey
     * @return String
     */
    public String getProfileIdentityPublicKey() {
        return profileIdentityPublicKey;
    }

    /**
     * Set the ProfileIdentityPublicKey
     * @param profileIdentityPublicKey
     */
    public void setProfileIdentityPublicKey(String profileIdentityPublicKey) {
        this.profileIdentityPublicKey = profileIdentityPublicKey;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodCallsHistory)) return false;

        MethodCallsHistory that = (MethodCallsHistory) o;

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
        final StringBuilder sb = new StringBuilder("MethodCallsHistory{");
        sb.append("id=").append(id);
        sb.append(", createTimestamp=").append(createTimestamp);
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", parameters='").append(parameters).append('\'');
        sb.append(", profileIdentityPublicKey='").append(profileIdentityPublicKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}