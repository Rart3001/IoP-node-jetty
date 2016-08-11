package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.request.ActorCatalogToAddOrUpdateRequest</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCatalogToAddOrUpdateRequest extends PackageContent {

    @Expose(serialize = true, deserialize = true)
    private final List<ActorCatalog>                actorCatalogList;

    @Expose(serialize = true, deserialize = true)
    private final List<ActorPropagationInformation> pendingItemList ;

    public ActorCatalogToAddOrUpdateRequest(final List<ActorCatalog>                actorCatalogList,
                                            final List<ActorPropagationInformation> pendingItemList ) {

        this.actorCatalogList = actorCatalogList;
        this.pendingItemList   = pendingItemList  ;
    }

    public List<ActorCatalog> getActorCatalogList() {
        return actorCatalogList;
    }

    public List<ActorPropagationInformation> getPendingItemList() {
        return pendingItemList;
    }

    /**
     * Generate the json representation
     * @return String
     */
    @Override
    public String toJson() {
        return GsonProvider.getGsonExposeAnnotation().toJson(this, getClass());
    }

    /**
     * Get the object
     *
     * @param content
     * @return PackageContent
     */
    public static ActorCatalogToAddOrUpdateRequest parseContent(String content) {
        return GsonProvider.getGsonExposeAnnotation().fromJson(content, ActorCatalogToAddOrUpdateRequest.class);
    }

    @Override
    public String toString() {
        return "ActorCatalogToAddOrUpdateRequest{" +
                "actorCatalogList=" + actorCatalogList +
                ", pendingItemList=" + pendingItemList +
                '}';
    }
}
