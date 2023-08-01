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
    public Response addFruitJson(String json) {
        boolean added = fruitService.addJsonToMongoDB(json, "fruits");

        if (added) {
            return Response.ok("Fruit added successfully", MediaType.TEXT_PLAIN).build();
        } else {
            return Response.serverError().entity("Error adding the fruit").build();
        }
    }

    @GET
    @Path("/queryByKeyValue")
    public Response queryFruit(@QueryParam("json") String jsonString) {
        List<Document> result = fruitService.queryByKeyValue(jsonString, "fruits");
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

}
