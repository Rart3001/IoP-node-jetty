package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/08/16.
 */
public class GeoLocationDao extends AbstractBaseDao<GeoLocation> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ClientDao.class));

    /**
     * Constructor
     */
    public GeoLocationDao() {
        super(GeoLocation.class);
    }
}
