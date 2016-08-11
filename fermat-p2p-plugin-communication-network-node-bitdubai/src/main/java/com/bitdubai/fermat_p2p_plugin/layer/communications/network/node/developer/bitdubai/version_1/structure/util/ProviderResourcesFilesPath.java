/*
 * @#ProviderResourcesFilesPath.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util;

import java.io.File;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ProviderResourcesFilesPath</code> create
 * a path and directory for the resources files
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ProviderResourcesFilesPath {

    /**
     * Represent the FILE_SYSTEM_SEPARATOR
     */
    public  static final String FILE_SYSTEM_SEPARATOR = System.getProperty("file.separator");

    /**
     * Get the path to resources file system folder
     * @return String path to file folder
     **/
    public static String getExternalStorageDirectory() {

        String path = System.getProperty("user.home").concat(FILE_SYSTEM_SEPARATOR).concat("externalStorage").concat(FILE_SYSTEM_SEPARATOR).concat("node").concat(FILE_SYSTEM_SEPARATOR).concat("resources");
        File dir = new File(path);
        return dir.getAbsolutePath().concat(FILE_SYSTEM_SEPARATOR);
    }

    /**
     * Create a new files path in the default path with the name
     * pass as parameter and ended with de FILE_SYSTEM_SEPARATOR
     *
     * @param name
     * @return String
     */
    public static String createNewFilesPath(String name){
        return (getExternalStorageDirectory().concat(name).concat(FILE_SYSTEM_SEPARATOR));
    }

}
