package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckOutProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckOutProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInNetworkServiceRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_NETWORK_SERVICE_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckOutNetworkServiceRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckOutNetworkServiceRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckOutNetworkServiceRequestProcessor() {
        super(PackageType.CHECK_OUT_NETWORK_SERVICE_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        CheckOutProfileMsgRequest messageContent = CheckOutProfileMsgRequest.parseContent(packageReceived.getContent());
        String profileIdentity = messageContent.getIdentityPublicKey();

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Create a Network Service object
             */
            NetworkService networkService = new NetworkService(profileIdentity);

            /*
             * Checked Out Profile from data base
             */
            JPADaoFactory.getNetworkServiceDao().delete(networkService);

            /*
             * If all ok, respond whit success message
             */
            CheckOutProfileMsjRespond checkOutProfileMsjRespond = new CheckOutProfileMsjRespond(CheckOutProfileMsjRespond.STATUS.SUCCESS, CheckOutProfileMsjRespond.STATUS.SUCCESS.toString(), profileIdentity);
            channel.sendPackage(session, checkOutProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_OUT_NETWORK_SERVICE_RESPONSE, destinationIdentityPublicKey);

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckOutProfileMsjRespond checkOutProfileMsjRespond = new CheckOutProfileMsjRespond(CheckOutProfileMsjRespond.STATUS.FAIL, exception.getLocalizedMessage(), profileIdentity);
                channel.sendPackage(session, checkOutProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_OUT_NETWORK_SERVICE_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }

    }

}
