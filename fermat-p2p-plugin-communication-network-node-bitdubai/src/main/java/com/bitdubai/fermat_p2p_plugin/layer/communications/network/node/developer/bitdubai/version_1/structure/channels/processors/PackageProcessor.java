package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor</code>
 * is the base class for all message processor class <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public abstract class PackageProcessor {

    /**
     * Represent the packageType
     */
    private final PackageType packageType;

    /**
     * Constructor with parameter
     * @param packageType
     */
    public PackageProcessor(PackageType packageType) {
        this.packageType = packageType;
    }

    /**
     * Gets the value of packageType and returns
     *
     * @return packageType
     */
    public PackageType getPackageType() {
        return packageType;
    }

    /**
     * Save the method call history
     *
     * @param parameters
     * @param profileIdentityPublicKey
     */
    protected void methodCallsHistory(String parameters, String profileIdentityPublicKey) {
/*
        MethodCallsHistory methodCallsHistory = new MethodCallsHistory();
        methodCallsHistory.setMethodName(getPackageType().toString());
        methodCallsHistory.setParameters(parameters);
        methodCallsHistory.setProfileIdentityPublicKey(profileIdentityPublicKey);

        getDaoFactory().getMethodCallsHistoryDao().create(methodCallsHistory);*/
    }

    /**
     * Method that call to process the message
     *
     * @param session that send the package
     * @param packageReceived to process
     */
    public abstract void processingPackage(final Session session, final Package packageReceived, final FermatWebSocketChannelEndpoint channel);
}
