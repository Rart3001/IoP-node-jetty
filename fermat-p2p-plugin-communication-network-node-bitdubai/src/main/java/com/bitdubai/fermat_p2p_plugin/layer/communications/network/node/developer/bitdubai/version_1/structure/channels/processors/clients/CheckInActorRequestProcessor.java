package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.actors.ActorsCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ActorCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ThumbnailUtil;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInActorRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_ACTOR_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInActorRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInActorRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInActorRequestProcessor() {
        super(PackageType.CHECK_IN_ACTOR_REQUEST);
    }


    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: " + packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

        ActorProfile actorProfile = (ActorProfile) messageContent.getProfileToRegister();

        try {

            ActorCatalogDao actorCatalogDao = JPADaoFactory.getActorCatalogDao();

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Load the client session
             */
            ClientSession clientSession = JPADaoFactory.getClientSessionDao().findById(session.getId());

            ActorCatalog actorCatalog;
            if (actorCatalogDao.exist(actorProfile.getIdentityPublicKey())) {
                actorCatalog = checkUpdates(actorProfile, actorCatalogDao, clientSession);
            } else {
                actorCatalog = create(actorProfile, actorCatalogDao, clientSession);
            }

            /*
             * If all ok, respond whit success message
             */
            CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.SUCCESS, CheckInProfileMsjRespond.STATUS.SUCCESS.toString(), actorProfile.getIdentityPublicKey());
            channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_ACTOR_RESPONSE, destinationIdentityPublicKey);

            LOG.info("Registered new Actor = "+actorProfile.getName());
            LOG.info("Registered new Actor PK = "+actorProfile.getIdentityPublicKey());

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.FAIL, exception.getLocalizedMessage(), actorProfile.getIdentityPublicKey());
                channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_ACTOR_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    private ActorCatalog checkUpdates(ActorProfile actorProfile, ActorCatalogDao actorCatalogDao, ClientSession clientSession) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException {

        ActorCatalog actorsCatalogToUpdate = actorCatalogDao.findById(actorProfile.getIdentityPublicKey());
        actorsCatalogToUpdate.setSession(clientSession);

        boolean hasChanges = false;

        if (!actorProfile.getName().equals(actorsCatalogToUpdate.getName())) {
            actorsCatalogToUpdate.setName(actorProfile.getName());
            hasChanges = true;
        }

        if (!actorProfile.getAlias().equals(actorsCatalogToUpdate.getAlias())) {
            actorsCatalogToUpdate.setAlias(actorProfile.getAlias());
            hasChanges = true;
        }

        if (!Arrays.equals(actorProfile.getPhoto(), actorsCatalogToUpdate.getPhoto())) {
            actorsCatalogToUpdate.setPhoto(actorProfile.getPhoto());
            hasChanges = true;
        }

        if (!getNetworkNodePluginRoot().getNodeProfile().getIdentityPublicKey().equals(actorsCatalogToUpdate.getHomeNode().getId())) {
            actorsCatalogToUpdate.setHomeNode(new NodeCatalog(getNetworkNodePluginRoot().getNodeProfile().getIdentityPublicKey()));
            hasChanges = true;
        }

        if (!actorProfile.getLocation().equals(actorsCatalogToUpdate.getLocation())) {
            actorsCatalogToUpdate.setLocation(actorProfile.getLocation().getLatitude(), actorProfile.getLocation().getLongitude());
            hasChanges = true;
        }

        LOG.info("hasChanges = "+hasChanges);

        if (hasChanges){

            Timestamp currentMillis = new Timestamp(System.currentTimeMillis());
            LOG.info("Updating profile");

            actorsCatalogToUpdate.setLastConnection(currentMillis);
            actorsCatalogToUpdate.setLastUpdateTime(currentMillis);
            actorsCatalogToUpdate.setLastUpdateType(ActorCatalogUpdateTypes.UPDATE);
            actorsCatalogToUpdate.setVersion(actorsCatalogToUpdate.getVersion() + 1);
            actorsCatalogToUpdate.setTriedToPropagateTimes(0);
            actorsCatalogToUpdate.setPendingPropagations(ActorsCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

            actorCatalogDao.update(actorsCatalogToUpdate);

        }else {
            actorCatalogDao.update(actorsCatalogToUpdate);
        }

        return actorsCatalogToUpdate;
    }

    private ActorCatalog create(ActorProfile actorProfile, ActorCatalogDao actorCatalogDao, ClientSession clientSession) throws IOException, CantInsertRecordDataBaseException, CantReadRecordDataBaseException {

        /*
         * Generate a thumbnail for the image
         */
        byte[] thumbnail = null;
        if (actorProfile.getPhoto() != null && actorProfile.getPhoto().length > 0) {
            thumbnail = ThumbnailUtil.generateThumbnail(actorProfile.getPhoto());
        }

        /*
         * Create the actor catalog
         */
        ActorCatalog actorCatalog = new ActorCatalog(actorProfile, thumbnail, JPADaoFactory.getNodeCatalogDao().findById(getNetworkNodePluginRoot().getNodeProfile().getIdentityPublicKey()), "");
        actorCatalog.setSession(clientSession);

        Timestamp currentMillis = new Timestamp(System.currentTimeMillis());

        actorCatalog.setLastConnection(currentMillis);
        actorCatalog.setLastUpdateTime(currentMillis);
        actorCatalog.setLastUpdateType(ActorCatalogUpdateTypes.ADD);
        actorCatalog.setVersion(0);
        actorCatalog.setTriedToPropagateTimes(0);
        actorCatalog.setPendingPropagations(ActorsCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

        /*
         * Save into data base
         */
        actorCatalogDao.persist(actorCatalog);

        return actorCatalog;
    }

    private NetworkNodePluginRoot pluginRoot;

    private NetworkNodePluginRoot getNetworkNodePluginRoot() {

        if (pluginRoot == null)
            pluginRoot = (NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT);

        return pluginRoot;
    }

}
