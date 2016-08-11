package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodePropagationInformation;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.request.NodesCatalogToPropagateRequest</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogToPropagateRequest extends PackageContent {

    private final List<NodePropagationInformation> nodePropagationInformationList;

    public NodesCatalogToPropagateRequest(final List<NodePropagationInformation> nodePropagationInformationList) {

        this.nodePropagationInformationList = nodePropagationInformationList;
    }

    public List<NodePropagationInformation> getNodePropagationInformationList() {
        return nodePropagationInformationList;
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
    public static NodesCatalogToPropagateRequest parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, NodesCatalogToPropagateRequest.class);
    }

    @Override
    public String toString() {
        return "NodesCatalogToPropagateRequest{" +
                "nodePropagationInformationList=" + nodePropagationInformationList +
                '}';
    }
}
