package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.NearNodeListMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.NearNodeListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.NearNodeListRequestProcessor</code>
 * process all packages received the type <code>MessageType.NEAR_NODE_LIST_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 26/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NearNodeListRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NearNodeListRequestProcessor.class));

    /**
     * Constructor
     */
    public NearNodeListRequestProcessor() {
        super(PackageType.NEAR_NODE_LIST_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+ packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        try {

            NearNodeListMsgRequest messageContent = NearNodeListMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Get the client location
             */
            Location clientLocation = messageContent.getClientLocation();

            /*
             * Get the node catalog list
             */
            List<NodeCatalog> nodesCatalogs = JPADaoFactory.getNodeCatalogDao().findAllNearTo(clientLocation, 0, 50);

            /*
             * Filter and order
             */
            List<NodeProfile> nodesCatalogsFiltered = new ArrayList<>(nodesCatalogs.size());

            for (NodeCatalog nodeCatalog : nodesCatalogs)
                    nodesCatalogsFiltered.add(nodeCatalog.getNodeProfile());

            /*
             * If all ok, respond whit success message
             */
            NearNodeListMsgRespond respondNearNodeListMsg = new NearNodeListMsgRespond(NearNodeListMsgRespond.STATUS.SUCCESS, NearNodeListMsgRespond.STATUS.SUCCESS.toString(), nodesCatalogsFiltered);
            channel.sendPackage(session, respondNearNodeListMsg.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.NEAR_NODE_LIST_RESPONSE, destinationIdentityPublicKey);

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * If all ok, respond whit success message
                 */
                NearNodeListMsgRespond respondNearNodeListMsg = new NearNodeListMsgRespond(NearNodeListMsgRespond.STATUS.FAIL, exception.getLocalizedMessage(), null);
                channel.sendPackage(session, respondNearNodeListMsg.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.NEAR_NODE_LIST_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }

    }
}
