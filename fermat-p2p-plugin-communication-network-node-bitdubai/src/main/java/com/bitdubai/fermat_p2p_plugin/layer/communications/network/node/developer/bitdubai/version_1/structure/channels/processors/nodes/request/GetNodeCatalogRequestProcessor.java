package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.GetNodeCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.GetNodeCatalogResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NodeCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.GetNodeCatalogRequestProcessor</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class GetNodeCatalogRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(GetNodeCatalogRequestProcessor.class));

    public GetNodeCatalogRequestProcessor() {
        super(PackageType.GET_NODE_CATALOG_REQUEST);
    }

    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        GetNodeCatalogRequest messageContent = GetNodeCatalogRequest.parseContent(packageReceived.getContent());

        LOG.info(messageContent.toString());

        GetNodeCatalogResponse getNodeCatalogResponse;
        List<NodeCatalog> nodesCatalogList = null;

        try {

            NodeCatalogDao nodeCatalogDao = JPADaoFactory.getNodeCatalogDao();

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            if (messageContent.getOffset() >= 0 && messageContent.getMax() > 0){

                nodesCatalogList = nodeCatalogDao.list(messageContent.getOffset(), messageContent.getMax());

                long count = nodeCatalogDao.count();

                /*
                 * If all ok, respond whit success message
                 */
                getNodeCatalogResponse = new GetNodeCatalogResponse(GetNodeCatalogResponse.STATUS.SUCCESS, GetNodeCatalogResponse.STATUS.SUCCESS.toString(), nodesCatalogList, count);

            } else {

                getNodeCatalogResponse = new GetNodeCatalogResponse(GetNodeCatalogResponse.STATUS.FAIL, "Invalid parameters: max="+messageContent.getMax()+ " | offset="+messageContent.getOffset(), nodesCatalogList, 0L);
            }

            Package packageRespond = Package.createInstance(getNodeCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.GET_NODE_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

            /*
             * Send the respond
             */
            session.getAsyncRemote().sendObject(packageRespond);

        } catch (Exception exception){

            try {

                LOG.info(FermatException.wrapException(exception).toString());

                /*
                 * Respond whit fail message
                 */
                getNodeCatalogResponse = new GetNodeCatalogResponse(GetNodeCatalogResponse.STATUS.FAIL, exception.getLocalizedMessage(), nodesCatalogList, 0L);
                Package packageRespond = Package.createInstance(getNodeCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.GET_NODE_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.info(FermatException.wrapException(e).toString());
            }
        }
    }

}
