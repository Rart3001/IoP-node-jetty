package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.GetActorsCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.GetActorsCatalogResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ActorCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.GetActorCatalogRequestProcessor</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class GetActorCatalogRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(GetActorCatalogRequestProcessor.class));

    public GetActorCatalogRequestProcessor() {
        super(PackageType.GET_ACTOR_CATALOG_REQUEST);
    }

    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        GetActorsCatalogRequest messageContent = GetActorsCatalogRequest.parseContent(packageReceived.getContent());

        GetActorsCatalogResponse getActorsCatalogResponse;
        List<ActorCatalog> catalogList = null;

        try {

            ActorCatalogDao actorCatalogDao = JPADaoFactory.getActorCatalogDao();

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            catalogList = loadData(messageContent.getOffset(), messageContent.getMax(), actorCatalogDao);

            long count = actorCatalogDao.count();

            /*
             * If all ok, respond whit success message
             */
            getActorsCatalogResponse = new GetActorsCatalogResponse(GetActorsCatalogResponse.STATUS.SUCCESS, GetActorsCatalogResponse.STATUS.SUCCESS.toString(), catalogList, count);
            Package packageRespond = Package.createInstance(getActorsCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.GET_ACTOR_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

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
                getActorsCatalogResponse = new GetActorsCatalogResponse(GetActorsCatalogResponse.STATUS.FAIL, exception.getLocalizedMessage(), catalogList, 0L);
                Package packageRespond = Package.createInstance(getActorsCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.GET_ACTOR_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.info(FermatException.wrapException(e).toString());
            }
        }
    }

    /**
     * Load the data from database
     *
     * @param offset
     * @param max
     * @return List<ActorsCatalog>
     */
    public List<ActorCatalog> loadData(Integer offset, Integer max, ActorCatalogDao actorCatalogDao) throws Exception {

        List<ActorCatalog> catalogList;

        if (offset >= 0 && max > 0){

            catalogList = actorCatalogDao.list(offset, max);

        } else {

            catalogList = actorCatalogDao.list();
        }

        return catalogList;
    }
}
