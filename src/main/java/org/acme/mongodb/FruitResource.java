package org.acme.mongodb;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/fruits")
public class FruitResource {

    @Inject FruitService fruitService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addFruitJson(String json) {
        fruitService.addJsonToMongoDB(json, "fruits");
    }
}
