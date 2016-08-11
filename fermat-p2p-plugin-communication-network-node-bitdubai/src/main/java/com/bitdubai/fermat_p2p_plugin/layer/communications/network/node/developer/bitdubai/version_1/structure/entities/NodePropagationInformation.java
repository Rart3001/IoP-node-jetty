package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodePropagationInformation</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodePropagationInformation {

    private final           String  identityPublicKey;

    private final           Integer version;

    public NodePropagationInformation(final String  identityPublicKey,
                                      final Integer version          ) {

        this.identityPublicKey     = identityPublicKey    ;
        this.version               = version              ;
    }

    public String getId() {
        return identityPublicKey;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "NodePropagationInformation{" +
                "identityPublicKey='" + identityPublicKey + '\'' +
                ", version=" + version +
                '}';
    }
}