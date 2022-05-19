
package jb.umbrella;

import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.webclient.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;

@ApplicationScoped
public class AddressService {

    private WebClient webClient;

    public AddressService() {
        this.webClient = WebClient.builder().addReader(JsonpSupport.reader()).addWriter(JsonpSupport.writer())
                .addMediaSupport(JsonpSupport.create()).build();
    }

    /**
     * Gets sanitized address from dataforsnyningen.dk's datavask
     *
     * @param address_query     String format of address to query for address data
     * @return String of sanitized address
     */
    public String getSanitizedAddress(String address_query) throws Exception {
        try {
            // Query address string from api.dataforsyningen.dk
            String sanitizedURI = "https://api.dataforsyningen.dk/datavask/adresser?betegnelse="+URLEncoder.encode(address_query, StandardCharsets.UTF_8);

            JsonObject all_sanitized_data = webClient.get().uri(sanitizedURI).request(JsonObject.class).get();

            JsonObject child_address_object = all_sanitized_data.getJsonArray("resultater").getJsonObject(0).getJsonObject("adresse");

            String street = child_address_object.getString("vejnavn");
            String street_no = child_address_object.getString("husnr");
            String post_no = child_address_object.getString("postnr");
            String city = child_address_object.getString("postnrnavn");

            return street + " " + street_no + ", " + post_no + " " + city;
                
        } catch (Exception e){
            throw new Exception("Service call failed: No sanitizable address could be found, with exception: " + e);
        }
    }
    
    /**
     * Gets address data from dataforsnyningen.dk
     *
     * @param address_query     String format of address to query for address data
     * @return JsonArray of the address data that closes match the given address_query string
     */
    public JsonArray getAddressData(String address_query) throws Exception {
        try {
            // Query address string from api.dataforsyningen.dk
            String sanitizedURI = "https://api.dataforsyningen.dk/adresser?q="+URLEncoder.encode(address_query, StandardCharsets.UTF_8);

            return webClient.get().uri(sanitizedURI).request(JsonArray.class).get();
                
        } catch (Exception e){
            throw new Exception("Service call failed: No address could be found, with exception: " + e);
        }
    }

     /**
     * Gets weather data from met.no
     *
     * @param lat     Latitude where weather data is queried
     * @param lon     Lontitude where weather data is queried
     * @return JsonObject of weather data in the area provided
     */
    public JsonObject getWeatherData(String lat, String lon) throws Exception {
        try {
            String sanitizedURI = "https://api.met.no/weatherapi/locationforecast/2.0/complete?lat="+
                URLEncoder.encode(lat, StandardCharsets.UTF_8)+"&lon="+
                URLEncoder.encode(lon, StandardCharsets.UTF_8);

            return webClient.get().uri(sanitizedURI).request(JsonObject.class).get();
        } catch (Exception e){
            throw new Exception("Service call failed: No weather data could be found, with exception: " + e);
        }
    }

}
