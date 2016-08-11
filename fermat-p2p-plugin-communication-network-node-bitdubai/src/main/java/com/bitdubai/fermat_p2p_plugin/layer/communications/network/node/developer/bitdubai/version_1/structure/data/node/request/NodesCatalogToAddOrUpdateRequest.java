package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.request.NodesCatalogToAddOrUpdateRequest</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogToAddOrUpdateRequest extends PackageContent {

    private final List<NodeCatalog> nodesCatalogList;

    public NodesCatalogToAddOrUpdateRequest(final List<NodeCatalog> nodesCatalogList) {

        this.nodesCatalogList = nodesCatalogList;
    }

    public List<NodeCatalog> getNodesCatalogList() {
        return nodesCatalogList;
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
    public static NodesCatalogToAddOrUpdateRequest parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, NodesCatalogToAddOrUpdateRequest.class);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NodesCatalogToAddOrUpdateRequest{");
        sb.append("nodesCatalogList=").append(nodesCatalogList);
        sb.append('}');
        return sb.toString();
    }
}
