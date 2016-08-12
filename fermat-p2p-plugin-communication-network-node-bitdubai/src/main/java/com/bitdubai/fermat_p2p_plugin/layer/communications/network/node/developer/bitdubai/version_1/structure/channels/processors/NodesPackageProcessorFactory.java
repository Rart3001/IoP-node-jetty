package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.*;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.*;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.*;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.NodesPackageProcessorFactory</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 05/08/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesPackageProcessorFactory {

    public static PackageProcessor getNodeServerPackageProcessorsByPackageType(final PackageType packageType){

         switch (packageType) {
             case ADD_NODE_TO_CATALOG_REQUEST:
                 return new AddNodeToCatalogRequestProcessor();

             case ACTOR_CATALOG_TO_PROPAGATE_REQUEST:
                 return new ActorCatalogToPropagateRequestProcessor();

             case ACTOR_CATALOG_TO_ADD_OR_UPDATE_REQUEST:
                 return new ActorCatalogToAddOrUpdateRequestProcessor();

             case GET_ACTOR_CATALOG_REQUEST:
                 return new GetActorCatalogRequestProcessor();

             case GET_NODE_CATALOG_REQUEST:
                 return new GetNodeCatalogRequestProcessor();

             case NODES_CATALOG_TO_ADD_OR_UPDATE_REQUEST:
                 return new NodesCatalogToAddOrUpdateRequestProcessor();

             case NODES_CATALOG_TO_PROPAGATE_REQUEST:
                 return new NodesCatalogToPropagateRequestProcessor();

             case UPDATE_NODE_IN_CATALOG_REQUEST:
                 return new UpdateNodeInCatalogRequestProcessor();

         }

         return null;
    }

    public static PackageProcessor getNodeClientPackageProcessorsByPackageType(final PackageType packageType){

        switch (packageType) {
            case ADD_NODE_TO_CATALOG_RESPONSE:
                return new AddNodeToCatalogResponseProcessor();

            case ACTOR_CATALOG_TO_PROPAGATE_RESPONSE:
                return new ActorCatalogToPropagateResponseProcessor();

            case GET_ACTOR_CATALOG_RESPONSE:
                return new GetActorCatalogResponseProcessor();

            case GET_NODE_CATALOG_RESPONSE:
                return new GetNodeCatalogResponseProcessor();

            case NODES_CATALOG_TO_PROPAGATE_RESPONSE:
                return new NodesCatalogToPropagateResponseProcessor();

            case UPDATE_NODE_IN_CATALOG_RESPONSE:
                return new UpdateNodeInCatalogResponseProcessor();

        }

        return null;
    }

    public static PackageProcessor getClientPackageProcessorsByPackageType(final PackageType packageType) {




        switch (packageType) {
            case ACTOR_CALL_REQUEST:
                return new ActorCallRequestProcessor();

            case ACTOR_LIST_REQUEST:
                return new ActorListRequestProcessor();

            case CHECK_IN_ACTOR_REQUEST:
                return new CheckInActorRequestProcessor();

            case CHECK_IN_CLIENT_REQUEST:
                return new CheckInClientRequestProcessor();

            case CHECK_IN_NETWORK_SERVICE_REQUEST:
                return new CheckInNetworkServiceRequestProcessor();

            case CHECK_OUT_ACTOR_REQUEST:
                return new CheckOutActorRequestProcessor();

            case CHECK_OUT_CLIENT_REQUEST:
                return new CheckOutClientRequestProcessor();

            case CHECK_OUT_NETWORK_SERVICE_REQUEST:
                return new CheckOutNetworkServiceRequestProcessor();

            case MESSAGE_TRANSMIT:
                return new MessageTransmitProcessor();

            case MESSAGE_TRANSMIT_SYNC_ACK_RESPONSE:
                return new MessageTransmitSyncProcessor();

            case NEAR_NODE_LIST_REQUEST:
                return new NearNodeListRequestProcessor();

            case UPDATE_ACTOR_PROFILE_REQUEST:
                return new UpdateActorProfileIntoCatalogProcessor();

            case UPDATE_PROFILE_GEOLOCATION_REQUEST:
                return new UpdateProfileLocationIntoCatalogProcessor();

        }

        return null;

    }

}
