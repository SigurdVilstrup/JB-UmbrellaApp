
package jb.umbrella;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.spi.CDI;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import io.helidon.microprofile.server.Server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MainTest {

    private static Server server;
    private static String serverUrl;

    @BeforeAll
    public static void startTheServer() throws Exception {
        server = Server.create().start();
        serverUrl = "http://localhost:" + server.port();
    }


    @Test
    void testHelloWorld() {
        Client client = ClientBuilder.newClient();

        JsonObject jsonObject = client.target(serverUrl).path("api/fact").request().get(JsonObject.class);
        Assertions.assertNotNull(jsonObject.getString("fact"));

        Response r = client.target(serverUrl).path("metrics").request().get();
        Assertions.assertEquals(200, r.getStatus(), "GET metrics status code");

        r = client.target(serverUrl).path("health").request().get();
        Assertions.assertEquals(200, r.getStatus(), "GET health status code");
    }

    @Test
    void testAddressService() {
        Client client = ClientBuilder.newClient();

        JsonObject jsonObject = client.target(serverUrl).path("api/address/Vestergde%208%20Silkebog").request().get(JsonObject.class);
        Assertions.assertNotNull(jsonObject.getString("closest_address"));
        Assertions.assertNotNull(jsonObject.getString("probability_of_precipitation"));

        Response r = client.target(serverUrl).path("metrics").request().get();
        Assertions.assertEquals(200, r.getStatus(), "GET metrics status code");

        r = client.target(serverUrl).path("health").request().get();
        Assertions.assertEquals(200, r.getStatus(), "GET health status code");
    }

    @AfterAll
    static void destroyClass() {
        CDI<Object> current = CDI.current();
        ((SeContainer) current).close();
    }
}
