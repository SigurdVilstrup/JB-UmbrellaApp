
package jb.umbrella;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Collections;
import java.util.List;

@Path("/api")
@RequestScoped
public class AddressResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    @Inject()
    AddressService addressService;

    @GET
    @Path("/address/{address_query}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getUmbrellaData(@PathParam("address_query") String address_query) throws Exception {
        String sanitized_address_query = addressService.getSanitizedAddress(address_query);
        JsonArray addressData = addressService.getAddressData(sanitized_address_query);
        String address = getClosestAddress(addressData);
        String lat_lon = getLatLon(addressData);
        JsonObject WeatherData = addressService.getWeatherData(lat_lon.split("_")[0], lat_lon.split("_")[1]);
        String precipitation_chance = getPrecipitationChance6h(WeatherData);

        return createAddressResponse(address, precipitation_chance);
    }

    /**
     * Gets the closes address from the list of addresses in a JsonArray
     *
     * @param address_data     JsonArray of address data
     * @return the closest address as a String
     */
    private String getClosestAddress(JsonArray address_data){
        // Return the closes address aka. the first in the array of address objects
        String closest_address = address_data.getJsonObject(0).getString("adressebetegnelse");
            return closest_address;
    }

    /**
     * Gets latitude and longitude from given JsonAray of address_data
     *
     * @param address_data     JsonArray of address data
     * @return latitude and longtitude as String in String format >>latitude + "_" + longitude<<
     */
    private String getLatLon(JsonArray address_data){
        // Getting coordinates / lat / lon from address data query
        JsonObject closestAddressData = address_data.getJsonObject(0);
        JsonObject childJsonObject = closestAddressData.getJsonObject("adgangsadresse").getJsonObject("adgangspunkt");
        
        List<JsonString> latlon_object = childJsonObject.getJsonArray("koordinater").getValuesAs(JsonString.class);
        String latlon = latlon_object.get(1)+"_"+latlon_object.get(0);

        return latlon;
    }

    /**
     * gets change of precipitation within the next 6 hours from the weather data
     *
     * @param weather_data     JsonArray of weather data
     * @return String of chance of precipitation within the next 6 hours
     */
    private String getPrecipitationChance6h(JsonObject weather_data){
        JsonObject childJsonObject = weather_data.getJsonObject("properties").getJsonArray("timeseries").getJsonObject(0).getJsonObject("data").getJsonObject("next_6_hours").getJsonObject("details");
        return childJsonObject.getJsonNumber("probability_of_precipitation").toString();
    }

    /**
     * Creates Json address response
     *
     * @param closest_address           String of the closes address
     * @param precipitation_chance     string of the chance of precipitation within the next 6 hours
     * @return JsonObject for response
     */
    private JsonObject createAddressResponse(String closest_address, String precipitation_chance) {
        return JSON.createObjectBuilder()
            .add("closest_address", closest_address)
            .add("probability_of_precipitation", precipitation_chance)
            .build();
    }
}
