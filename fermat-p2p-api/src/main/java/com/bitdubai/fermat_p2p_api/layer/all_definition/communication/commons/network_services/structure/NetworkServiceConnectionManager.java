package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractNetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.daos.IncomingMessagesDao;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.daos.OutgoingMessagesDao;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.Profile;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantEstablishConnectionException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.structure.NetworkServiceConnectionManager</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 13/05/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 * //TODO: León esto no tiene nada de manager..., revisá lo que haces.
 */
public final class NetworkServiceConnectionManager {

    private AbstractNetworkService networkServiceRoot;

    private IncomingMessagesDao incomingMessagesDao;
    private OutgoingMessagesDao outgoingMessagesDao;

    private Map<String, Profile> poolConnectionsWaitingForResponse;

    public NetworkServiceConnectionManager(final AbstractNetworkService networkServiceRoot) {

        this.networkServiceRoot = networkServiceRoot;

        this.incomingMessagesDao = new IncomingMessagesDao(networkServiceRoot.getDataBase());
        this.outgoingMessagesDao = new OutgoingMessagesDao(networkServiceRoot.getDataBase());

        this.poolConnectionsWaitingForResponse = new ConcurrentHashMap<>();
    }

    public void connectTo(NetworkServiceProfile remoteNetworkService) {

        if (!poolConnectionsWaitingForResponse.containsKey(remoteNetworkService.getIdentityPublicKey())) {

            // TODO CALL NETWORK SERVICE
        }


    }

    public void connectTo(ActorProfile remoteActor) throws CantEstablishConnectionException {

        if (!poolConnectionsWaitingForResponse.containsKey(remoteActor.getIdentityPublicKey())) {

            try {

                networkServiceRoot.getConnection().callActor(networkServiceRoot.getProfile(), remoteActor);

                poolConnectionsWaitingForResponse.put(remoteActor.getIdentityPublicKey(), remoteActor);

            } catch (Exception e) {
                networkServiceRoot.reportError(
                        UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        e
                );
            }
        }
    }

    /**
     * Notify to the agent that remove a specific connection
     *
     * @param identityPublicKey
     */
    public void removeConnectionWaitingForResponse(String identityPublicKey){
        this.poolConnectionsWaitingForResponse.remove(identityPublicKey);
    }

    /**
     * Notify to the agent that remove all connection
     */
    public void removeAllConnectionWaitingForResponse(){
        this.poolConnectionsWaitingForResponse.clear();
    }

    /**
     * Get the OutgoingMessageDao
     * @return OutgoingMessageDao
     */
    public OutgoingMessagesDao getOutgoingMessagesDao() {
        return outgoingMessagesDao;
    }

    /**
     * Get the IncomingMessageDao
     * @return IncomingMessageDao
     */
    public IncomingMessagesDao getIncomingMessagesDao() {
        return incomingMessagesDao;
    }

}