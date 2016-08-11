package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.*;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.*;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.*;

import java.util.ArrayList;
import java.util.List;

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

    public static List<PackageProcessor> getNodeServerPackageProcessorsByPackageType(final PackageType packageType){

        List<PackageProcessor> packageProcessors = new ArrayList<>();

         switch (packageType) {
             case ADD_NODE_TO_CATALOG_REQUEST:
                 packageProcessors.add(new AddNodeToCatalogRequestProcessor());
                 break;
             case ACTOR_CATALOG_TO_PROPAGATE_REQUEST:
                 packageProcessors.add(new ActorCatalogToPropagateRequestProcessor());
                 break;
             case ACTOR_CATALOG_TO_ADD_OR_UPDATE_REQUEST:
                 packageProcessors.add(new ActorCatalogToAddOrUpdateRequestProcessor());
                 break;
             case GET_ACTOR_CATALOG_REQUEST:
                 packageProcessors.add(new GetActorCatalogRequestProcessor());
                 break;
             case GET_NODE_CATALOG_REQUEST:
                 packageProcessors.add(new GetNodeCatalogRequestProcessor());
                 break;
             case NODES_CATALOG_TO_ADD_OR_UPDATE_REQUEST:
                 packageProcessors.add(new NodesCatalogToAddOrUpdateRequestProcessor());
                 break;
             case NODES_CATALOG_TO_PROPAGATE_REQUEST:
                 packageProcessors.add(new NodesCatalogToPropagateRequestProcessor());
                 break;
             case UPDATE_NODE_IN_CATALOG_REQUEST:
                 packageProcessors.add(new UpdateNodeInCatalogRequestProcessor());
                 break;
         }

        return packageProcessors;
    }

    public static List<PackageProcessor> getNodeClientPackageProcessorsByPackageType(final PackageType packageType){

        List<PackageProcessor> packageProcessors = new ArrayList<>();

        switch (packageType) {
            case ADD_NODE_TO_CATALOG_RESPONSE:
                packageProcessors.add(new AddNodeToCatalogResponseProcessor());
                break;
            case ACTOR_CATALOG_TO_PROPAGATE_RESPONSE:
                packageProcessors.add(new ActorCatalogToPropagateResponseProcessor());
                break;
            case GET_ACTOR_CATALOG_RESPONSE:
                packageProcessors.add(new GetActorCatalogResponseProcessor());
                break;
            case GET_NODE_CATALOG_RESPONSE:
                packageProcessors.add(new GetNodeCatalogResponseProcessor());
                break;
            case NODES_CATALOG_TO_PROPAGATE_RESPONSE:
                packageProcessors.add(new NodesCatalogToPropagateResponseProcessor());
                break;
            case UPDATE_NODE_IN_CATALOG_RESPONSE:
                packageProcessors.add(new UpdateNodeInCatalogResponseProcessor());
                break;
        }

        return packageProcessors;
    }

    public static List<PackageProcessor> getClientPackageProcessorsByPackageType(final PackageType packageType) {

        List<PackageProcessor> packageProcessors = new ArrayList<>();

        switch (packageType) {
            case ACTOR_CALL_REQUEST:
                packageProcessors.add(new ActorCallRequestProcessor());
                break;
            case ACTOR_LIST_REQUEST:
                packageProcessors.add(new ActorListRequestProcessor());
                break;
            case ACTOR_TRACE_DISCOVERY_QUERY_REQUEST:
                packageProcessors.add(new ActorTraceDiscoveryQueryRequestProcessor());
                break;
            case CHECK_IN_ACTOR_REQUEST:
                packageProcessors.add(new CheckInActorRequestProcessor());
                break;
            case CHECK_IN_CLIENT_REQUEST:
                packageProcessors.add(new CheckInClientRequestProcessor());
                break;
            case CHECK_IN_NETWORK_SERVICE_REQUEST:
                packageProcessors.add(new CheckInNetworkServiceRequestProcessor());
                break;
            case CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST:
                packageProcessors.add(new CheckInProfileDiscoveryQueryRequestProcessor());
                break;
            case CHECK_OUT_ACTOR_REQUEST:
                packageProcessors.add(new CheckOutActorRequestProcessor());
                break;
            case CHECK_OUT_CLIENT_REQUEST:
                packageProcessors.add(new CheckOutClientRequestProcessor());
                break;
            case CHECK_OUT_NETWORK_SERVICE_REQUEST:
                packageProcessors.add(new CheckOutNetworkServiceRequestProcessor());
                break;
            case MESSAGE_TRANSMIT:
                packageProcessors.add(new MessageTransmitProcessor());
                break;
            case MESSAGE_TRANSMIT_SYNC_ACK_RESPONSE:
                packageProcessors.add(new MessageTransmitSyncProcessor());
                break;
            case NEAR_NODE_LIST_REQUEST:
                packageProcessors.add(new NearNodeListRequestProcessor());
                break;
            case UPDATE_ACTOR_PROFILE_REQUEST:
                packageProcessors.add(new UpdateActorProfileIntoCatalogProcessor());
                break;
            case UPDATE_PROFILE_GEOLOCATION_REQUEST:
                packageProcessors.add(new UpdateProfileLocationIntoCatalogProcessor());
                break;
        }

        return packageProcessors;
    }

}
