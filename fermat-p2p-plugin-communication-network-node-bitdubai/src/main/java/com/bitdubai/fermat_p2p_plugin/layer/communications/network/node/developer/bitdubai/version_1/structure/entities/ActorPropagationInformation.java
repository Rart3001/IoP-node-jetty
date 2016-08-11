package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorPropagationInformation {

    private final String                  identityPublicKey;

    private final Integer                 version;

    private final ActorCatalogUpdateTypes lastUpdateType;

    public ActorPropagationInformation(final String                   identityPublicKey,
                                       final Integer                  version          ,
                                       final ActorCatalogUpdateTypes  lastUpdateType   ) {

        this.identityPublicKey     = identityPublicKey    ;
        this.version               = version              ;
        this.lastUpdateType        = lastUpdateType       ;
    }

    public String getId() {
        return identityPublicKey;
    }

    public Integer getVersion() {
        return version;
    }

    public ActorCatalogUpdateTypes getLastUpdateType() {
        return lastUpdateType;
    }
    
    @Override
    public String toString() {
        return "ActorPropagationInformation{" +
                "identityPublicKey='" + identityPublicKey + '\'' +
                ", version=" + version +
                ", lastUpdateType='" + lastUpdateType + '\'' +
                '}';
    }
}