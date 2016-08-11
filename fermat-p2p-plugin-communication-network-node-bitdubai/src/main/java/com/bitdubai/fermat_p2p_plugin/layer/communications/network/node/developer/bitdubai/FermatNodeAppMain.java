package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai;


import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_osa_addon.layer.linux.device_location.developer.bitdubai.version_1.DeviceLocationSystemAddonRoot;
import com.bitdubai.fermat_osa_addon.layer.linux.file_system.developer.bitdubai.version_1.PluginFileSystemLinuxAddonRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;

/**
 * The Class <code>com.bitdubai.linux.core.app.FermatNodeAppMain</code> initialize
 * all fermat system
 * <p/>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 30/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class FermatNodeAppMain {

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {

        try {

            System.out.println("***********************************************************************");
            System.out.println("* FERMAT - Network Node - Version 1.0 (2016)                            *");
            System.out.println("* www.fermat.org                                                      *");
            System.out.println("***********************************************************************");
            System.out.println("");
            System.out.println("- Starting process ...");

            DeviceLocationSystemAddonRoot deviceLocationSystemAddonRoot = new DeviceLocationSystemAddonRoot();
            deviceLocationSystemAddonRoot.start();

            PluginFileSystemLinuxAddonRoot pluginFileSystemLinuxAddonRoot = new PluginFileSystemLinuxAddonRoot();
            pluginFileSystemLinuxAddonRoot.start();

            NetworkNodePluginRoot networkNodePluginRoot = new NetworkNodePluginRoot();


            networkNodePluginRoot.setLocationManager((LocationManager) deviceLocationSystemAddonRoot.getManager());
            networkNodePluginRoot.setPluginFileSystem((PluginFileSystem) pluginFileSystemLinuxAddonRoot.getManager());

            networkNodePluginRoot.start();

            System.out.println("FERMAT - Network Node - started satisfactory...");

        } catch (Exception e) {

            System.out.println("***********************************************************************");
            System.out.println("* FERMAT - ERROR                                                      *");
            System.out.println("***********************************************************************");
            e.printStackTrace();
            System.exit(1);
        }

    }
}
