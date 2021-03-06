package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.conf;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.conf.ClientChannelConfigurator</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientChannelConfigurator extends ServerEndpointConfig.Configurator {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ClientChannelConfigurator.class.getName()));

    /**
     * (non-javadoc)
     *
     * @see ServerEndpointConfig.Configurator#modifyHandshake(ServerEndpointConfig, HandshakeRequest, HandshakeResponse)
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig serverEndpointConfig, HandshakeRequest handshakeRequest, HandshakeResponse handshakeResponse) {

       /* for (String key : handshakeRequest.getHeaders().keySet()) {
            LOG.info(key + " : "+handshakeRequest.getHeaders().get(key));
        }*/

        /*
         * Validate if the client public key identity come in the header
         */
        if (handshakeRequest.getHeaders().containsKey(HeadersAttName.CPKI_ATT_HEADER_NAME)){

            /*
             * Get the client public key identity
             */
            String cpki = handshakeRequest.getHeaders().get(HeadersAttName.CPKI_ATT_HEADER_NAME).get(0);

            /*
             * Pass the identity create to the FermatWebSocketClientChannelServerEndpoint
             */
            serverEndpointConfig.getUserProperties().put(HeadersAttName.CPKI_ATT_HEADER_NAME, cpki);

            /*
             * Create a node identity for this session
             */
            ECCKeyPair nodeIdentityForSession;
            List<String>  value = new ArrayList<>();
            /*
             * Create the node public key identity header attribute value
             * to share with the client
             */
            if(!serverEndpointConfig.getUserProperties().containsKey(HeadersAttName.REMOTE_NPKI_ATT_HEADER_NAME)){
                nodeIdentityForSession = new ECCKeyPair();
                value.add(nodeIdentityForSession.getPublicKey());
            }else{
                nodeIdentityForSession = (ECCKeyPair) serverEndpointConfig.getUserProperties().get(HeadersAttName.REMOTE_NPKI_ATT_HEADER_NAME);
                value.add(nodeIdentityForSession.getPublicKey());
            }

            /*
             * Set the new header attribute
             */
             handshakeResponse.getHeaders().put(HeadersAttName.REMOTE_NPKI_ATT_HEADER_NAME, value);

            /*
             * Pass the identity create to the FermatWebSocketClientChannelServerEndpoint
             */
             serverEndpointConfig.getUserProperties().put(HeadersAttName.REMOTE_NPKI_ATT_HEADER_NAME, nodeIdentityForSession);

        }

    }

    /**
     * (non-javadoc)
     *
     * @see ServerEndpointConfig.Configurator#checkOrigin(String)
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {

        LOG.info("originHeaderValue = "+originHeaderValue);

        return super.checkOrigin(originHeaderValue);
    }

}
