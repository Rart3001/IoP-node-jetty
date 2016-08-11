package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.nodes;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.exception.PackageTypeNotSupportedException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.conf.ClientNodeChannelConfigurator;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.NodesPackageProcessorFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.PackageDecoder;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.PackageEncoder;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.FermatWebSocketClientNodeChannelServerEndpoint.FermatWebSocketClientNodeChannelServerEndpoint</code>
 * is the client to communicate nodes by the node client channel<p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@ClientEndpoint(
        configurator = ClientNodeChannelConfigurator.class ,
        encoders = {PackageEncoder.class},
        decoders = {PackageDecoder.class}
)
public class FermatWebSocketClientNodeChannelServerEndpoint extends FermatWebSocketChannelEndpoint {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(FermatWebSocketClientNodeChannelServerEndpoint.class));

    /**
     * Represent the clientConnection
     */
    private Session clientConnection;

    /**
     * Constructor
     */
    public FermatWebSocketClientNodeChannelServerEndpoint(){
        super();
    }

    public FermatWebSocketClientNodeChannelServerEndpoint(NodeCatalog remoteNodeCatalogProfile){

        try {

            URI endpointURI = new URI("ws://"+remoteNodeCatalogProfile.getIp()+":"+remoteNodeCatalogProfile.getDefaultPort()+"/fermat/ws/node-channel");

            LOG.info("Trying to connect to "+endpointURI.toString());
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            clientConnection = webSocketContainer.connectToServer(this, endpointURI);

        } catch (Exception e) {
            LOG.info(FermatException.wrapException(e).toString());
            throw new RuntimeException(e);
        }

    }

    /**
     * Constructor with parameters
     *
     * @param ip
     * @param port
     */
    public FermatWebSocketClientNodeChannelServerEndpoint(String ip, Integer port){

       try {

            URI endpointURI = new URI("ws://"+ip+":"+port+"/fermat/ws/node-channel");
            LOG.info("Trying to connect to "+endpointURI.toString());

           //ClientManager webSocketContainer = ClientManager.createClient();


            /*
             * Create a ReconnectHandler
             * it intents only three times
             */
          /* ClientManager.ReconnectHandler reconnectHandler = new ClientManager.ReconnectHandler() {
               int i = 0;

               @Override
               public boolean onDisconnect(CloseReason closeReason) {
                   i++;

                   if(i < 4)
                       return Boolean.TRUE;
                   else
                       return Boolean.FALSE;

               }

               @Override
               public boolean onConnectFailure(Exception exception) {
                   i++;

                   if(i < 4) {

                       try {

                           //System.out.println("# NetworkClientCommunicationConnection - Reconnect Failure Message: "+exception.getMessage()+" Cause: "+exception.getCause());
                           // To avoid potential DDoS when you don't limit number of reconnects, wait to the next try.
                           Thread.sleep(5000);

                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }

                       System.out.println("###############################################################################");
                       System.out.println("# Connect Failure -> Reconnecting... #");
                       System.out.println("###############################################################################");

                       return Boolean.TRUE;
                   }else{
                       return Boolean.FALSE;
                   }

               }

           };*/


           /*
            * Register the ReconnectHandler
            */
           //webSocketContainer.getProperties().put(ClientProperties.RECONNECT_HANDLER, reconnectHandler);
           WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
           clientConnection = webSocketContainer.connectToServer(this, endpointURI);

        } catch (Exception e) {
           LOG.info(FermatException.wrapException(e).toString());
           throw new RuntimeException(e);
        }

    }

    /**
     * (non-javadoc)
     *
     * @see FermatWebSocketChannelEndpoint#getPackageProcessors(PackageType)
     */
    @Override
    protected List<PackageProcessor> getPackageProcessors(PackageType packageType){
        return NodesPackageProcessorFactory.getNodeClientPackageProcessorsByPackageType(packageType);
    }

    /**
     *  Method called to handle a new connection
     *
     * @param session connected
     * @throws IOException
     */
    @OnOpen
    public void onConnect(final Session session) {

        LOG.info(" --------------------------------------------------------------------- ");
        LOG.info(" Starting method onConnect");
        LOG.info(" id = "+session.getId());
        LOG.info(" url = " + session.getRequestURI());

    }

    /**
     * Method called to handle a new packet received
     *
     * @param packageReceived new
     * @param session sender
     */
    @OnMessage
    public void newPackageReceived(Package packageReceived, Session session) {

        LOG.info("New message Received");
        LOG.info("Session: " + session.getId() + " packageReceived = " + packageReceived.getPackageType() + "");

        try {

            /*
             * Process the new package received
             */
            processMessage(packageReceived, session);

        }catch (PackageTypeNotSupportedException p){
            p.printStackTrace();
            LOG.warn(p.getMessage());
        } catch (Exception exception) {

            LOG.info("*********** UNHANDLED EXCEPTION TRYING TO PROCESS A PACKAGE ********** \n"+FermatException.wrapException(exception).toString());
        }

    }

    /**
     * Method called to handle a connection close
     *
     * @param closeReason message
     * @param session closed
     */
    @OnClose
    public void onClose(CloseReason closeReason, Session session) {

        LOG.info("Closed session : " + session.getId() + " Code: (" + closeReason.getCloseCode() + ") - reason: " + closeReason.getReasonPhrase());

    }

    /**
     * Method  called to handle a error
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable){

        LOG.error("Unhandled exception catch");
        LOG.info(FermatException.wrapException(new Exception(throwable)).toString());
        try {

            if (session.isOpen()) {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
                LOG.error("The session was closed correctly");

            }else {
                LOG.error("The session already close, no try to close");
            }
        } catch (Exception e) {
            //I'll try to print the stacktrace to determinate this exception
            System.out.println("ON CLOSE EXCEPTION: ");
            LOG.info(FermatException.wrapException(e).toString());
        }
    }

    /**
     * Return if the client connection are connected
     *
     * @return boolean
     */
    public boolean isConnected(){

        try {

            if (clientConnection != null){
                return clientConnection.isOpen();
            }

        }catch (Exception e){
            return Boolean.FALSE;
        }

        return Boolean.FALSE;
    }

    /**
     * Send message
     * @param message
     */
    public boolean sendMessage(String message, PackageType packageType) {

        if (isConnected()){

            LOG.info("Sending message "+message);

            String channelIdentityPrivateKey = getChannelIdentity().getPrivateKey();
            String destinationIdentityPublicKey = (String) clientConnection.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
            Package packageRequest = Package.createInstance(message, NetworkServiceType.UNDEFINED, packageType, channelIdentityPrivateKey, destinationIdentityPublicKey);
            this.clientConnection.getAsyncRemote().sendObject(packageRequest);

            return true;

        } else {

            LOG.warn("Can't send message, no connected ");

            return false;
        }

    }

}
