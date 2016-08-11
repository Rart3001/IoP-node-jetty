package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.agents;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStopAgentException;
import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.AgentStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkClientConnection;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractNetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.MessagesStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.agents.NetworkServicePendingMessagesSupervisorAgent</code> is
 * responsible to validate is exist pending message to process (incoming or outgoing)
 * <p/>
 * Created by Leon Acosta - (rart3001@gmail.com) on 16/05/2016.
 * Based on Roberto Requena network service Template.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NetworkServicePendingMessagesSupervisorAgent extends FermatAgent {

    private AbstractNetworkService networkServiceRoot;
    private ScheduledExecutorService scheduledThreadPool;
    private List<ScheduledFuture> scheduledFutures;

    /**
     * Constructor with parameter
     *
     * @param networkServiceRoot
     */
    public NetworkServicePendingMessagesSupervisorAgent(final AbstractNetworkService networkServiceRoot){

        super();
        this.networkServiceRoot = networkServiceRoot;
        this.status             = AgentStatus.CREATED;
    }

    /**
     * Method that process the pending incoming messages
     */
    private void processPendingIncomingMessage() {

        try {

            /*
             * Read all pending message from database
             */
            Map<String, Object> filters = new HashMap<>();
            filters.put(NetworkServiceDatabaseConstants.INCOMING_MESSAGES_STATUS_COLUMN_NAME, MessagesStatus.NEW_RECEIVED.getCode());
            List<NetworkServiceMessage> messages = networkServiceRoot.getNetworkServiceConnectionManager().getIncomingMessagesDao().findAll(filters);

            if(messages != null) {

                /*
                 * For all destination in the message request a new connection
                 */
                for (NetworkServiceMessage fermatMessage : messages) {

                    networkServiceRoot.onNewMessageReceived(fermatMessage);

                    networkServiceRoot.getNetworkServiceConnectionManager().getIncomingMessagesDao().markAsRead(fermatMessage);

                }
            }

        }catch (Exception e){
            System.out.println("NetworkServicePendingMessagesSupervisorAgent ("+networkServiceRoot.getProfile().getNetworkServiceType()+") - processPendingIncomingMessage detect a error: "+e.getMessage());
            networkServiceRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
    }

    /**
     * Method that process the pending outgoing messages, in this method
     * validate is pending message to send, and request new connection for
     * the remote agent send the message
     */
    private void processPendingOutgoingMessage(Integer countFail, Integer countFailMax) {

        try {

            /*
             * Read all pending message from database
             */
            Map<String, Boolean> receivers = networkServiceRoot.getNetworkServiceConnectionManager().getOutgoingMessagesDao().findByFailCount(countFail, countFailMax);

            NetworkClientConnection networkClientConnection = networkServiceRoot.getConnection();

            /*
             * For all destination in the message request a new connection
             */
            for (Map.Entry<String, Boolean> receiver : receivers.entrySet()) {

                if (networkClientConnection.isConnected()) {

                    if (receiver.getValue()) {
                        ActorProfile remoteParticipant = new ActorProfile();
                        remoteParticipant.setIdentityPublicKey(receiver.getKey());

                        networkServiceRoot.getNetworkServiceConnectionManager().connectTo(remoteParticipant);
                    } else {

                        NetworkServiceProfile remoteParticipant = new NetworkServiceProfile();
                        remoteParticipant.setIdentityPublicKey(receiver.getKey());

                        networkServiceRoot.getNetworkServiceConnectionManager().connectTo(remoteParticipant);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("NetworkServicePendingMessagesSupervisorAgent ("+networkServiceRoot.getProfile().getNetworkServiceType()+") - processPendingOutgoingMessage detect a error: "+e.getMessage());
            networkServiceRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }

    }

    @Override
    public synchronized void start() throws CantStartAgentException {

        try {

            if(this.isStarted())
                return;

            if(this.isPaused()) {
                this.resume();
                return;
            }

            this.scheduledThreadPool   = Executors.newScheduledThreadPool(5);
            this.scheduledFutures      = new ArrayList<>();

            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingIncomingMessageProcessorTask(),         10,    30, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(),         10,    30, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(1, 4),     10,    60, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(5, 9),     10,   600, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(10, null), 10, 3600, TimeUnit.SECONDS));

            this.status = AgentStatus.STARTED;

        } catch (CantStartAgentException exception) {

            throw exception;
        } catch (Exception exception) {

            throw new CantStartAgentException(exception, null, "You should inspect the cause.");
        }
    }

    public synchronized void resume() throws CantStartAgentException {

        try {

            if(this.isStarted())
                return;

            if(this.isStopped())
                throw new CantStartAgentException("The agent is stopped, can't resume it, you should start it.");

            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingIncomingMessageProcessorTask(),         10,    30, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(), 10, 30, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(1, 4), 10, 60, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(5, 9),     10,   600, TimeUnit.SECONDS));
            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PendingOutgoingMessageProcessorTask(10, null), 10,  3600, TimeUnit.SECONDS));

            this.status = AgentStatus.STARTED;

        } catch (CantStartAgentException exception) {

            throw exception;
        } catch (Exception exception) {

            throw new CantStartAgentException(exception, null, "You should inspect the cause.");
        }
    }

    public synchronized void pause() throws CantStopAgentException {

        try {

            if (isPaused())
                return;

            if(!this.isStarted())
                throw new CantStopAgentException("The agent is not running and it cannot be paused. Current status: "+this.getStatus());

            for (ScheduledFuture future: scheduledFutures) {
                future.cancel(Boolean.TRUE);
                scheduledFutures.remove(future);
            }

            this.status = AgentStatus.PAUSED;

        } catch (CantStopAgentException exception) {

            throw exception;
        } catch (Exception exception) {

            throw new CantStopAgentException(exception, null, "You should inspect the cause.");
        }
    }

    public synchronized void stop() throws CantStopAgentException {

        try {

            if (isStopped())
                return;

            if(!this.isStarted() && !this.isPaused())
                throw new CantStartAgentException("The agent is not running, it cannot be stopped.");

            for (ScheduledFuture future: scheduledFutures) {
                future.cancel(Boolean.TRUE);
                scheduledFutures.remove(future);
            }

            scheduledThreadPool.shutdown();
            scheduledFutures    = null;
            scheduledThreadPool = null;
            this.status = AgentStatus.STOPPED;

        } catch (Exception exception) {

            throw new CantStopAgentException(exception, null, "You should inspect the cause.");
        }
    }

    private class PendingIncomingMessageProcessorTask implements Runnable {

        /**
         * (non-javadoc)
         * @see Runnable#run()
         */
        @Override
        public void run() {
            processPendingIncomingMessage();
        }
    }

    private class PendingOutgoingMessageProcessorTask implements Runnable {

        /**
         * Represent the count fail
         */
        private Integer countFailMin;

        /**
         * Represent the count fail
         */
        private Integer countFailMax;

        /**
         * Constructor without parameters
         */
        public PendingOutgoingMessageProcessorTask(){

        }

        /**
         * Constructor with parameters
         *
         * @param countFailMin
         * @param countFailMax
         */
        public PendingOutgoingMessageProcessorTask(final Integer countFailMin,
                                                   final Integer countFailMax){

            this.countFailMin = countFailMin;
            this.countFailMax = countFailMax;
        }

        /**
         * (non-javadoc)
         * @see Runnable#run()
         */
        @Override
        public void run() {
            processPendingOutgoingMessage(countFailMin, countFailMax);
        }
    }

}
