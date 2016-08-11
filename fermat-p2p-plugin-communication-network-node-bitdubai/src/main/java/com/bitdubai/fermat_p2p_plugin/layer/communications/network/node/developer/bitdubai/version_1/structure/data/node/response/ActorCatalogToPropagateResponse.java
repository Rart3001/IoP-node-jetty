package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.request.ActorCatalogToPropagateResponse</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCatalogToPropagateResponse extends MsgRespond {

    private final List<ActorPropagationInformation> actorPropagationInformationList;
    private final Integer                           lateNotificationCounter        ;

    public ActorCatalogToPropagateResponse(final List<ActorPropagationInformation> actorPropagationInformationList,
                                           final Integer                           lateNotificationCounter        ,
                                           final STATUS                            status                         ,
                                           final String                            details                        ) {
        super(status, details);
        this.actorPropagationInformationList = actorPropagationInformationList;
        this.lateNotificationCounter         = lateNotificationCounter        ;
    }

    public List<ActorPropagationInformation> getActorPropagationInformationList() {
        return actorPropagationInformationList;
    }

    public Integer getLateNotificationCounter() {
        return lateNotificationCounter;
    }

    /**
     * Generate the json representation
     * @return String
     */
    @Override
    public String toJson() {
        return GsonProvider.getGson().toJson(this, getClass());
    }

    /**
     * Get the object
     *
     * @param content
     * @return PackageContent
     */
    public static ActorCatalogToPropagateResponse parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, ActorCatalogToPropagateResponse.class);
    }

    @Override
    public String toString() {
        return "ActorCatalogToPropagateResponse{" +
                "actorPropagationInformationList=" + actorPropagationInformationList +
                ", lateNotificationCounter=" + lateNotificationCounter +
                '}';
    }
}
