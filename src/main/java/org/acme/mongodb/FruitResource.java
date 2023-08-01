package org.acme.mongodb;

import java.util.List;

import org.bson.Document;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fruits")
public class FruitResource {

    @Inject
    FruitService fruitService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addFruitJson(String json) {
        fruitService.addJsonToMongoDB(json, "fruits");
    }

    @GET
    @Path("/queryByKeyValue")
    public Response queryFruit(@QueryParam("json") String jsonString) {
        List<Document> result = fruitService.queryByKeyValue(jsonString, "fruits");
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

}
