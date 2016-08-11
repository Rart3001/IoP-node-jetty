package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.rest.services;

import com.bitdubai.fermat_api.layer.all_definition.location_system.NetworkNodeCommunicationDeviceLocation;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationSource;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NetworkServiceSessionDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.*;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.rest.NetworkData</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 16/06/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Path("/rest/api/v1/network")
public class NetworkData {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkData.class));

    /*
     * Represent the pluginRoot
     */
    private final NetworkNodePluginRoot pluginRoot;

    /**
     * Constructor
     */
    public NetworkData() {
        pluginRoot = (NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT);
    }


    @GET
    @Path("/catalog")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNodes() {

        try {
            /*
             * Get the node catalog list
             */
            List<NodeCatalog> nodesCatalogs = JPADaoFactory.getNodeCatalogDao().list();
            List<String> nodes = new ArrayList<>();

            if (nodesCatalogs != null) {
                for (NodeCatalog node : nodesCatalogs) {
                    nodes.add(node.getIp());
                }
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("nodes", GsonProvider.getGson().toJson(nodes));

            return Response.status(200).entity(GsonProvider.getGson().toJson(jsonObject)).build();

        } catch (Exception e) {

            LOG.error("Error trying to list nodes.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code", e.hashCode());
            jsonObject.addProperty("message", e.getMessage());
            jsonObject.addProperty("details", GsonProvider.getGson().toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error", GsonProvider.getGson().toJson(jsonObject));

            return Response.status(200).entity(GsonProvider.getGson().toJson(jsonObjectError)).build();

        }


    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServerData() {

        try {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("hash", pluginRoot.getIdentity().getPublicKey());

            if (pluginRoot.getNodeProfile() != null && pluginRoot.getNodeProfile().getLocation() != null) {
                jsonObject.addProperty("location", GsonProvider.getGson().toJson(pluginRoot.getNodeProfile().getLocation()));
            } else {

                Location location = new NetworkNodeCommunicationDeviceLocation(
                        0.0,
                        0.0,
                        0.0,
                        0,
                        0.0,
                        System.currentTimeMillis(),
                        LocationSource.UNKNOWN
                );

                jsonObject.addProperty("location", GsonProvider.getGson().toJson(location));
            }

            jsonObject.addProperty("os", "");
            jsonObject.addProperty("networkServices", GsonProvider.getGson().toJson(getNetworkServicesCount()));

            return Response.status(200).entity(GsonProvider.getGson().toJson(jsonObject)).build();

        } catch (Exception e) {

            LOG.error("Error trying to get server data.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code", e.hashCode());
            jsonObject.addProperty("message", e.getMessage());
            jsonObject.addProperty("details", GsonProvider.getGson().toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error", GsonProvider.getGson().toJson(jsonObject));

            return Response.status(200).entity(GsonProvider.getGson().toJson(jsonObjectError)).build();

        }

    }

    @GET
    @Path("/clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {

        try {

            List<String> listOfClients = new ArrayList<>();

            List<ClientSession> clientSessions = JPADaoFactory.getClientSessionDao().list();

            if (clientSessions != null) {
                for (ClientSession clientCheckIn : clientSessions) {

                    JsonObject jsonObjectClient = new JsonObject();
                    jsonObjectClient.addProperty("hash", clientCheckIn.getClient().getId());
                    jsonObjectClient.addProperty("location", GsonProvider.getGson().toJson(clientCheckIn.getClient().getLocation()));
                    jsonObjectClient.addProperty("networkServices", GsonProvider.getGson().toJson(getListOfNetworkServiceOfClientSpecific(clientCheckIn.getClient().getId())));

                    listOfClients.add(GsonProvider.getGson().toJson(jsonObjectClient));

                }
            }

            return Response.status(200).entity(GsonProvider.getGson().toJson(listOfClients)).build();

        } catch (Exception e) {
            LOG.error("Error trying to list clients.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code", e.hashCode());
            jsonObject.addProperty("message", e.getMessage());
            jsonObject.addProperty("details", GsonProvider.getGson().toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error", GsonProvider.getGson().toJson(jsonObject));

            return Response.status(200).entity(GsonProvider.getGson().toJson(jsonObjectError)).build();
        }
    }

    @GET
    @Path("/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActors() {

        try {

            List<String> actors = new ArrayList<>();

            List<ActorSession> listOfCheckedInActor = JPADaoFactory.getActorSessionDao().list();

            for (ActorSession actorCheckIn : listOfCheckedInActor) {

                ActorCatalog actorCatalog = actorCheckIn.getActor();

                JsonObject jsonObjectActor = new JsonObject();
                jsonObjectActor.addProperty("hash", actorCatalog.getId());
                jsonObjectActor.addProperty("type", actorCatalog.getActorType());
                jsonObjectActor.addProperty("links", GsonProvider.getGson().toJson(new ArrayList<>()));

                jsonObjectActor.addProperty("location", GsonProvider.getGson().toJson(actorCatalog.getLocation()));

                JsonObject jsonObjectActorProfile = new JsonObject();
                jsonObjectActorProfile.addProperty("phrase", "There is not Phrase");
                jsonObjectActorProfile.addProperty("picture", Base64.encodeBase64String(actorCatalog.getPhoto()));
                jsonObjectActorProfile.addProperty("name", actorCatalog.getName());

                jsonObjectActor.addProperty("profile", GsonProvider.getGson().toJson(jsonObjectActorProfile));

                actors.add(GsonProvider.getGson().toJson(jsonObjectActor));
            }

            return Response.status(200).entity(GsonProvider.getGson().toJson(actors)).build();

        } catch (Exception e) {

            LOG.error("Error trying to list actors.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code", e.hashCode());
            jsonObject.addProperty("message", e.getMessage());
            jsonObject.addProperty("details", GsonProvider.getGson().toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error", GsonProvider.getGson().toJson(jsonObject));

            return Response.status(200).entity(GsonProvider.getGson().toJson(jsonObjectError)).build();
        }

    }


    private Map<NetworkServiceType, Long> getNetworkServicesCount() {

        NetworkServiceSessionDao networkServiceSessionDao = JPADaoFactory.getNetworkServiceSessionDao();

        Map<NetworkServiceType, Long> listNetworkServicesCount = new HashMap<>();

        List<NetworkServiceType> listOfNetworkService = getListOfNetworkService();

        if (listOfNetworkService != null) {

            for (NetworkServiceType networkServiceType : listOfNetworkService) {

                try {
                    Map<String, Object> filter = new HashMap<>();
                    filter.put("networkService.networkServiceType", networkServiceType.toString());
                    Long count = (long) networkServiceSessionDao.count(filter);

                    listNetworkServicesCount.put(networkServiceType, count);
                } catch (CantReadRecordDataBaseException e) {

                    LOG.error("Error trying to get network services count.", e);
                }

            }

        }

        return listNetworkServicesCount;
    }

    private List<NetworkServiceType> getListOfNetworkService() {

        Map<NetworkServiceType, NetworkServiceType> listNetworkServices = new HashMap<>();

        try {
            List<NetworkServiceSession> checkedInNetworkServiceList = JPADaoFactory.getNetworkServiceSessionDao().list();

            if (checkedInNetworkServiceList != null) {

                for (NetworkServiceSession checkedInNetworkService : checkedInNetworkServiceList) {
                    if (!listNetworkServices.containsKey(checkedInNetworkService.getNetworkService().getNetworkServiceType()))
                        listNetworkServices.put(checkedInNetworkService.getNetworkService().getNetworkServiceType(), checkedInNetworkService.getNetworkService().getNetworkServiceType());
                }
                return new ArrayList<>(listNetworkServices.values());
            }


        } catch (Exception e) {
            LOG.error("Error trying to list network services of a specific client.", e);
        }

        return new ArrayList<>();

    }

    private List<NetworkServiceType> getListOfNetworkServiceOfClientSpecific(String
                                                                                     publicKeyClient) {

        Map<NetworkServiceType, NetworkServiceType> listNetworkServices = new HashMap<>();

        try {

            Map<String, Object> filtersList = new HashMap<>();

//            filtersList.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME, publicKeyClient);
//            filtersList.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME, ProfileTypes.NETWORK_SERVICE.getCode());
            filtersList.put("networkService.client.id", publicKeyClient);

//            List<CheckedInProfile> checkedInNetworkServiceList = daoFactory.getCheckedInProfilesDao().findAll(filtersList);
            List<NetworkServiceSession> checkedInNetworkServiceList = JPADaoFactory.getNetworkServiceSessionDao().list(filtersList);

            if (checkedInNetworkServiceList != null) {

                for (NetworkServiceSession checkedInNetworkService : checkedInNetworkServiceList)
                    if (!listNetworkServices.containsKey(checkedInNetworkService.getNetworkService().getNetworkServiceType()))
                        listNetworkServices.put(checkedInNetworkService.getNetworkService().getNetworkServiceType(), checkedInNetworkService.getNetworkService().getNetworkServiceType());
                return new ArrayList<>(listNetworkServices.values());
            }

        } catch (Exception e) {
            LOG.error("Error trying to list network services of a specific client.", e);
        }

        return new ArrayList<>();

    }
}
