
package jb.umbrella;

import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.webclient.WebClient;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;

@ApplicationScoped
public class FactService {

    private WebClient webClient;

    public FactService() {
        this.webClient = WebClient.builder().addReader(JsonpSupport.reader()).addWriter(JsonpSupport.writer())
                .addMediaSupport(JsonpSupport.create()).build();
    }

    public String getRandomFact() throws Exception {
        try {
            return webClient.get().uri("https://api.chucknorris.io/jokes/random").request(JsonObject.class)
                    .get().getString("value");
        } catch (Exception e){
            throw new Exception("Service call failed: No facts could be found");
        }
    }
}
