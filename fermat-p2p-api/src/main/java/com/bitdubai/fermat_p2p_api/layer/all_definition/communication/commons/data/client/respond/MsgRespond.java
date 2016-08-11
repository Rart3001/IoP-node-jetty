package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.google.gson.annotations.Expose;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 26/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class MsgRespond extends PackageContent {

    /**
     * Represent the status of the check in process
     */
    public enum STATUS{
        SUCCESS,
        FAIL,
        EXCEPTION
    }

    /**
     * Represent the status
     */
    @Expose(serialize = true, deserialize = true)
    private STATUS status;

    /**
     * Represent the details
     */
    @Expose(serialize = true, deserialize = true)
    private String details;

    /**
     * Constructor with parameters
     *
     * @param status
     * @param details
     */
    public MsgRespond(STATUS status, String details){
        this.status = status;
        this.details = details;
    }

    /**
     * Gets the value of status and returns
     *
     * @return status
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * Get the Details value
     *
     * @return details
     */
    public String getDetails() {
        return details;
    }

    
}
