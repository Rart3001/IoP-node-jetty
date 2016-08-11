package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileDiscoveryQueryMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.Profile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.DistanceCalculator;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;
import java.util.*;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInProfileDiscoveryQueryRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 27/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInProfileDiscoveryQueryRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInProfileDiscoveryQueryRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInProfileDiscoveryQueryRequestProcessor() {
        super(PackageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: " + packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        CheckInProfileDiscoveryQueryMsgRequest messageContent = CheckInProfileDiscoveryQueryMsgRequest.parseContent(packageReceived.getContent());
        List<Profile> profileList = null;
        DiscoveryQueryParameters discoveryQueryParameters = messageContent.getDiscoveryQueryParameters();

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Validate if a network service search
             */
            if (discoveryQueryParameters.getNetworkServiceType() != null && discoveryQueryParameters.getNetworkServiceType() !=  NetworkServiceType.UNDEFINED){

                /*
                 * Find in the data base
                 */
                profileList = filterNetworkServices(discoveryQueryParameters);

            }else{

                /*
                 * Find in the data base
                 */
                profileList = filterActors(discoveryQueryParameters);
            }

            if(profileList != null && profileList.size() == 0)
                throw new Exception("Not Found row in the Table");

            /*
             * Apply geolocation
             */
            if(discoveryQueryParameters.getLocation() != null)
                profileList = applyGeoLocationFilter(discoveryQueryParameters.getLocation(), profileList, discoveryQueryParameters.getDistance());

            /*
             * Apply pagination
             */
            if ((discoveryQueryParameters.getMax() != 0) && (discoveryQueryParameters.getOffset() != 0)){

                /*
                 * Apply pagination
                 */
                if (profileList.size() > discoveryQueryParameters.getMax() &&
                        profileList.size() > discoveryQueryParameters.getOffset()){
                    profileList =  profileList.subList(discoveryQueryParameters.getOffset(), discoveryQueryParameters.getMax());
                }else if (profileList.size() > 100) {
                    profileList = profileList.subList(discoveryQueryParameters.getOffset(), 100);
                }

            }else if (profileList.size() > 100) {
                profileList = profileList.subList(0, 100);
            }

            /*
             * If all ok, respond whit success message
             */
            CheckInProfileListMsgRespond checkInProfileListMsgRespond = new CheckInProfileListMsgRespond(CheckInProfileListMsgRespond.STATUS.SUCCESS, CheckInProfileListMsgRespond.STATUS.SUCCESS.toString(), profileList, discoveryQueryParameters);
            channel.sendPackage(session, checkInProfileListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_RESPONSE, destinationIdentityPublicKey);

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckInProfileListMsgRespond checkInProfileListMsgRespond = new CheckInProfileListMsgRespond(CheckInProfileListMsgRespond.STATUS.FAIL, exception.getLocalizedMessage(), profileList, discoveryQueryParameters);
                channel.sendPackage(session, checkInProfileListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }

        }

    }

    /**
     * Filter all network service from data base that mach
     * with the parameters
     *
     * @param discoveryQueryParameters
     * @return List<Profile>
     */
    private List<Profile> filterNetworkServices(DiscoveryQueryParameters discoveryQueryParameters) throws CantReadRecordDataBaseException {

        List<Profile> profileList = new ArrayList<>();

        Map<String, Object> filters = constructFiltersNetworkServiceTable(discoveryQueryParameters);
        List<NetworkServiceSession> networkServices = JPADaoFactory.getNetworkServiceSessionDao().list(filters);

        for (NetworkServiceSession checkedInNetworkService : networkServices)
            profileList.add(checkedInNetworkService.getNetworkService().getNetworkServiceProfile());

        return profileList;
    }

    /**
     * Filter all network service from data base that mach
     * with the parameters
     *
     * @param discoveryQueryParameters
     * @return List<Profile>
     */
    private List<Profile> filterActors(DiscoveryQueryParameters discoveryQueryParameters) throws CantReadRecordDataBaseException {

        List<Profile> profileList = new ArrayList<>();

        Map<String, Object> filters = buildActorFilterGroupFromDiscoveryQueryParameters(discoveryQueryParameters);
        List<ActorSession> actores = JPADaoFactory.getActorSessionDao().list(filters);

        if(actores != null)
            for (ActorSession checkedInActor : actores)
                profileList.add(checkedInActor.getActor().getActorProfile());

        return profileList;
    }

    /**
     * Construct database filter from discovery query parameters.
     *
     * @param params parameters to filter the actors catalog table.
     *
     * @return a list with the database table filters.
     */
    private Map<String, Object> buildActorFilterGroupFromDiscoveryQueryParameters(final DiscoveryQueryParameters params){

        final Map<String, Object> filters = new HashMap<>();

        if (params.getIdentityPublicKey() != null)
            filters.put("id", params.getIdentityPublicKey());

        if (params.getName() != null)
            filters.put("name", params.getName());

        if (params.getAlias() != null)
            filters.put("alias",  params.getAlias());

        if (params.getActorType() != null)
            filters.put("actorType", params.getActorType());

        if (params.getExtraData() != null)
            filters.put("extraData", params.getExtraData());

        if (params.getLastConnectionTime() != null)
            filters.put("lastConnection", params.getLastConnectionTime().toString());

        if (params.getLastConnectionTime() != null)
            filters.put("location", params.getLocation());

        if (params.getLastConnectionTime() != null)
            filters.put("distance", params.getDistance());

        return filters;
    }


    /**
     * Construct data base filter from discovery query parameters
     *
     * @param discoveryQueryParameters
     * @return Map<String, Object> filters
     */
    private Map<String, Object> constructFiltersNetworkServiceTable(DiscoveryQueryParameters discoveryQueryParameters){

        Map<String, Object> filters = new HashMap<>();

        if (discoveryQueryParameters.getIdentityPublicKey() != null)
            filters.put("networkService.id", discoveryQueryParameters.getIdentityPublicKey());

        if (discoveryQueryParameters.getNetworkServiceType() != null)
            filters.put("networkService.networkServiceType", discoveryQueryParameters.getNetworkServiceType().getCode());

        return filters;
    }


    /**
     * Method that apply geo location filter to the list
     *
     * @param filterLocation
     * @param profileList
     * @param distance
     * @return List<Profile>
     */
    private List<Profile> applyGeoLocationFilter(Location filterLocation, List<Profile> profileList, Double distance) {

        /*
         * Hold the data ordered by distance
         */
        Map<Double, Profile> orderedByDistance = new TreeMap<>();

        /*
         * For each node
         */
        for (final Profile profile: profileList) {

            /*
             * If component have a geo location
             */
            if (profile.getLocation() != null){

                /*
                 * Calculate the distance between the two points
                 */
                Double componentDistance = DistanceCalculator.distance(filterLocation, profile.getLocation(), DistanceCalculator.KILOMETERS);

                if (distance != null){

                    if (componentDistance <= distance){

                        /*
                         * Add to the list
                         */
                        orderedByDistance.put(componentDistance, profile);
                    }

                }else {

                    /*
                     * Add to the list
                     */
                    orderedByDistance.put(componentDistance, profile);

                }

            }

        }

        return new ArrayList<>(orderedByDistance.values());
    }

}
