package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.request.ActorCatalogToPropagateRequest</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCatalogToPropagateRequest extends PackageContent {

    private final List<ActorPropagationInformation> actorPropagationInformationList;

    public ActorCatalogToPropagateRequest(final List<ActorPropagationInformation> actorPropagationInformationList) {

        this.actorPropagationInformationList = actorPropagationInformationList;
    }

    public List<ActorPropagationInformation> getActorPropagationInformationList() {
        return actorPropagationInformationList;
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
    public static ActorCatalogToPropagateRequest parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, ActorCatalogToPropagateRequest.class);
    }

    @Override
    public String toString() {
        return "ActorCatalogToPropagateRequest{" +
                "actorPropagationInformationList=" + actorPropagationInformationList +
                '}';
    }
}
