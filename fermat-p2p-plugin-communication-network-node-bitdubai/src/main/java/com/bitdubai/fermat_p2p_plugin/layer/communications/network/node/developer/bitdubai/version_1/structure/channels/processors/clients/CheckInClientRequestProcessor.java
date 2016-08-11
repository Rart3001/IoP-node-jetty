package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ClientProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInClientRequestProcessor</code>
 * process all packages received the type <code>PackageType.CHECK_IN_CLIENT_REQUEST</code><p/>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInClientRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInClientRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInClientRequestProcessor() {
        super(PackageType.CHECK_IN_CLIENT_REQUEST);
    }

    /**
     * (non-javadoc)
     *
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        ClientProfile clientProfile;

        try {

            CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Obtain the profile of the client
             */
            clientProfile = (ClientProfile) messageContent.getProfileToRegister();

            /*
             * Delete all previous or old session
             */
            //JPADaoFactory.getClientSessionDao().deleteAll(clientProfile);

            /*
             * Save the client
             */
            Client client = new Client(clientProfile);
            JPADaoFactory.getClientDao().save(client);

            /*
             * Checked In Profile into data base
             */
            JPADaoFactory.getClientSessionDao().checkIn(session, clientProfile);

            /*
             * If all ok, respond whit success message
             */
            CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.SUCCESS, CheckInProfileMsjRespond.STATUS.SUCCESS.toString(), clientProfile.getIdentityPublicKey());
            channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_CLIENT_RESPONSE, destinationIdentityPublicKey);

        } catch (Exception exception) {

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(
                        CheckInProfileMsjRespond.STATUS.FAIL,
                        exception.getLocalizedMessage(),
                        null
                );

                channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_CLIENT_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

}
