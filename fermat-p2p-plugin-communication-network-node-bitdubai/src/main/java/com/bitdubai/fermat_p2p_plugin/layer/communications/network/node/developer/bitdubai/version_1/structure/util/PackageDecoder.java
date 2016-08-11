package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.PackageDecoder</code>
 * decode the json string to a package object
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 30/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class PackageDecoder implements Decoder.Text<Package>{

    /**
     * (non-javadoc)
     * @see Decoder.Text#decode(String)
     */
    @Override
    public Package decode(String s) throws DecodeException {
        return GsonProvider.getGson().fromJson(s, Package.class);
    }

    /**
     * (non-javadoc)
     * @see Decoder.Text#willDecode(String)
     */
    @Override
    public boolean willDecode(String s) {
        try{

            GsonProvider.getJsonParser().parse(s);
            return true;

        }catch (Exception ex){
            return false;
        }
    }

    /**
     * (non-javadoc)
     * @see Decoder.Text#init(EndpointConfig)
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * (non-javadoc)
     * @see Decoder.Text#destroy()
     */
    @Override
    public void destroy() {

    }
}
