/*
 * @#ProfileRegistrationHistory.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory</code>
 * represent the Profile Registration History
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@Cacheable(false)
public class ProfileRegistrationHistory extends AbstractBaseEntity<Long> {

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
     * Represent the identityPublicKey
     */
    @NotNull
    private String identityPublicKey;

    /**
     * Represent the deviceType
     */
    @NotNull
    private String deviceType;

    /**
     * Represent the profileType
     */
    @NotNull
    private ProfileTypes profileType;

    /**
     * Represent the checkedTimestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp checkedTimestamp;

    /**
     * Represent the type
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private RegistrationType type;

    /**
     * Represent the result
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private RegistrationResult result;

    /**
     * Represent the detail
     */
    @NotNull
    private String detail;

    /**
     * Constructor
     */
    public ProfileRegistrationHistory() {
        super();
        this.identityPublicKey = "";
        this.deviceType = "";
        this.profileType = null;
        this.checkedTimestamp = new Timestamp(System.currentTimeMillis());
        this.type = null;
        this.result = null;
        this.detail = "";
    }

    /**
     * Constructor with parameters
     * @param identityPublicKey
     * @param deviceType
     * @param profileType
     * @param type
     * @param result
     * @param detail
     */
    public ProfileRegistrationHistory(String identityPublicKey, String deviceType, ProfileTypes profileType, RegistrationType type, RegistrationResult result, String detail) {
        super();
        this.identityPublicKey = identityPublicKey;
        this.deviceType = deviceType;
        this.profileType = profileType;
        this.checkedTimestamp = new Timestamp(System.currentTimeMillis());
        this.type = type;
        this.result = result;
        this.detail = detail;
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
     * Get the IdentityPublicKey
     * @return identityPublicKey
     */
    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    /**
     * Set the IdentityPublicKey
     * @param identityPublicKey
     */
    public void setIdentityPublicKey(String identityPublicKey) {
        this.identityPublicKey = identityPublicKey;
    }

    /**
     * Get the DeviceType
     * @return deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Set the DeviceType
     * @param deviceType
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * Get the ProfileType
     * @return profileType
     */
    public ProfileTypes getProfileType() {
        return profileType;
    }

    /**
     * Set the ProfileType
     * @param profileType
     */
    public void setProfileType(ProfileTypes profileType) {
        this.profileType = profileType;
    }

    /**
     * Get the CheckedTimestamp
     * @return checkedTimestamp
     */
    public Timestamp getCheckedTimestamp() {
        return checkedTimestamp;
    }

    /**
     * Set the CheckedTimestamp
     * @param checkedTimestamp
     */
    public void setCheckedTimestamp(Timestamp checkedTimestamp) {
        this.checkedTimestamp = checkedTimestamp;
    }

    /**
     * Get the Type
     * @return type
     */
    public RegistrationType getType() {
        return type;
    }

    /**
     * Set the Type
     * @param type
     */
    public void setType(RegistrationType type) {
        this.type = type;
    }

    /**
     * Get the Result
     * @return result
     */
    public RegistrationResult getResult() {
        return result;
    }

    /**
     * Set the Result
     * @param result
     */
    public void setResult(RegistrationResult result) {
        this.result = result;
    }

    /**
     * Get the Detail
     * @return String
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Set the Detail
     * @param detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileRegistrationHistory)) return false;

        ProfileRegistrationHistory that = (ProfileRegistrationHistory) o;

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
        final StringBuilder sb = new StringBuilder("ProfileRegistrationHistory{");
        sb.append("id=").append(id);
        sb.append(", identityPublicKey='").append(identityPublicKey).append('\'');
        sb.append(", deviceType='").append(deviceType).append('\'');
        sb.append(", profileType=").append(profileType);
        sb.append(", checkedTimestamp=").append(checkedTimestamp);
        sb.append(", type=").append(type);
        sb.append(", result=").append(result);
        sb.append(", detail='").append(detail).append('\'');
        sb.append('}');
        return sb.toString();
    }
}