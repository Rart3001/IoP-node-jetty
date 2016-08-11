package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.UpdateActorProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.UpdateProfileMsjRespond;
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
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.AddActorIntoCatalogProcessor</code>
 * process all packages received the type <code>MessageType.UPDATE_ACTOR_PROFILE_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 20/06/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class UpdateActorProfileIntoCatalogProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(UpdateActorProfileIntoCatalogProcessor.class));

    /**
     * Represent the nodeIdentity
     */
    private final String nodeIdentity = ((NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT)).getIdentity().getPublicKey();

    /**
     * Constructor
     */
    public UpdateActorProfileIntoCatalogProcessor() {
        super(PackageType.UPDATE_ACTOR_PROFILE_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        UpdateActorProfileMsgRequest messageContent = UpdateActorProfileMsgRequest.parseContent(packageReceived.getContent());
        ActorProfile actorProfile = (ActorProfile) messageContent.getProfileToUpdate();
        UpdateProfileMsjRespond updateProfileMsjRespond = null;

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            ActorCatalogDao actorCatalogDao = JPADaoFactory.getActorCatalogDao();

            /*
             * Validate if exist
             */
            if (actorCatalogDao.exist(actorProfile.getIdentityPublicKey())){

                LOG.info("Existing profile");

                ActorCatalog actorsCatalogToUpdate = actorCatalogDao.findById(actorProfile.getIdentityPublicKey());

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

                if (!nodeIdentity.equals(actorsCatalogToUpdate.getHomeNode().getId())) {
                    actorsCatalogToUpdate.setHomeNode(new NodeCatalog(nodeIdentity));
                    hasChanges = true;
                }

                if (actorsCatalogToUpdate.getLocation()!=null && actorProfile.getLocation()!=null) {
                    if (!actorProfile.getLocation().equals(actorsCatalogToUpdate.getLocation())) {
                        actorsCatalogToUpdate.setLocation(actorProfile.getLocation().getLatitude(), actorProfile.getLocation().getLongitude());
                        hasChanges = true;
                    }
                }

                if (!actorProfile.getExtraData().equals(actorsCatalogToUpdate.getExtraData())) {
                    actorsCatalogToUpdate.setExtraData(actorProfile.getExtraData());
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

                    /*
                     * If all ok, respond whit success message
                     */
                    updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.SUCCESS, MsgRespond.STATUS.SUCCESS.toString(), actorProfile.getIdentityPublicKey());
                }

            } else {

                updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.FAIL, "The actor profile no exist", actorProfile.getIdentityPublicKey());

            }

            /*
             * Send the respond
             */
            channel.sendPackage(session, updateProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_ACTOR_PROFILE_RESPONSE, destinationIdentityPublicKey);

            LOG.info("Process finish");

        } catch (Exception exception){

            try {

                LOG.error(exception);
                updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.FAIL, exception.getCause().getMessage(), actorProfile.getIdentityPublicKey());
                channel.sendPackage(session, updateProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_ACTOR_PROFILE_RESPONSE, destinationIdentityPublicKey);

            }catch (Exception e) {
               LOG.error(e);
            }
        }

    }
}
