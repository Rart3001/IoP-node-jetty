package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * The enum <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author lnacosta
 * @version 1.0
 * @since Java JDK 1.7
 */
public enum ActorCatalogUpdateTypes implements FermatEnum {

    /**
     * To make the code more readable, please keep the elements in the Enum sorted alphabetically.
     */
    ADD        ("AD"),
    UPDATE     ("UP"),
    GEO        ("GE"),
    LAST_CONN  ("LC"),

    ;

    private final String code;

    ActorCatalogUpdateTypes(final String code) {

        this.code = code;
    }

    public static ActorCatalogUpdateTypes getByCode(final String code) throws InvalidParameterException {

        for (ActorCatalogUpdateTypes type : ActorCatalogUpdateTypes.values()) {
            if(type.getCode().equals(code))
                return type;
        }

        throw new InvalidParameterException(
                "Code Received: " + code,
                "The received code is not valid for the ActorCatalogUpdateTypes enum."
        );
    }

    @Override
    public String getCode() {

        return this.code;
    }
}