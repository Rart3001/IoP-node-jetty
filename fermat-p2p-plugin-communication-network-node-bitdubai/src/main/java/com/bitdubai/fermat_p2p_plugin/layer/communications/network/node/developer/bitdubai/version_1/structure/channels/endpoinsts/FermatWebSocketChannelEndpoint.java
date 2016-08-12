package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.exception.PackageTypeNotSupportedException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public abstract class FermatWebSocketChannelEndpoint {

    /**
     * Represent the MAX_MESSAGE_SIZE
     */
    protected static final int MAX_MESSAGE_SIZE = 3000000;

    /**
     * Represent the MAX_IDLE_TIMEOUT
     */
    protected static final int MAX_IDLE_TIMEOUT = 60000;

    /**
     * Represent the channelIdentity
     */
    private ECCKeyPair channelIdentity;

    /**
     * Constructor
     */
    public FermatWebSocketChannelEndpoint(){
        super();
        this.channelIdentity = ((NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT)).getIdentity(); //new ECCKeyPair(); //
    }

    /**
     * Gets the value of channelIdentity and returns
     *
     * @return channelIdentity
     */
    public ECCKeyPair getChannelIdentity() {
        return channelIdentity;
    }

    /**
     * Sets the channelIdentity
     *
     * @param channelIdentity to set
     */
    protected void setChannelIdentity(ECCKeyPair channelIdentity) {
        this.channelIdentity = channelIdentity;
    }

    /**
     * Method that process a new message received
     *
     * @param packageReceived
     * @param session
     */
    protected void processMessage(Package packageReceived, Session session) throws PackageTypeNotSupportedException {

        PackageProcessor packageProcessor = getPackageProcessors(packageReceived.getPackageType());
        /*
         * Validate if can process the message
         */
        if (packageProcessor != null){

            /*
             * Process the message
             */
            packageProcessor.processingPackage(session, packageReceived, this);

        } else {

            throw new PackageTypeNotSupportedException("The package type: "+packageReceived.getPackageType()+" is not supported");
        }
    }

    public synchronized final void sendPackage(final Session            session           ,
                                               final String             packageContent    ,
                                               final NetworkServiceType networkServiceType,
                                               final PackageType        packageType       ,
                                               final String             identityPublicKey ) throws EncodeException, IOException {

        if (session.isOpen()) {

            Package packageRespond = Package.createInstance(
                    packageContent                      ,
                    networkServiceType                  ,
                    packageType                         ,
                    getChannelIdentity().getPrivateKey(),
                    identityPublicKey
            );

            session.getBasicRemote().sendObject(packageRespond);
        } else {
            throw new IOException("connection is not opened.");
        }
    }

    /**
     * Gets the value of packageProcessors and returns
     *
     * @return packageProcessors
     */
    protected abstract PackageProcessor getPackageProcessors(PackageType packageType);

}
