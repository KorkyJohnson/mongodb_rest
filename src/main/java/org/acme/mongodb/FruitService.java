package org.acme.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import io.vertx.core.json.JsonObject;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class FruitService {

    @Inject
    MongoClient mongoClient;

    // Create
    public void addJsonToMongoDB(String json, String collectionName) {
        Document document = Document.parse(json);
        getCollection(collectionName).insertOne(document);
    }

    // Read
    // public List<Document> queryByKeyValue(String queryKey, String queryValue) {
    public List<Document> queryByKeyValue(String queryString, String collectionName) {

        String queryKey = "", queryValue = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(queryString);

            queryKey = jsonNode.fieldNames().next();
            queryValue = jsonNode.get(queryKey).asText();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bson query = eq(queryKey, queryValue);
        List<Document> result = new ArrayList<>();

        // MongoDBConnect<Document> mongoDBConnect = new MongoDBConnect<Document>();
        // MongoCollection<Document> mongoCollection =
        // mongoDBConnect.getCollection(Document.class);

        // try (MongoCursor<Document> cursor = mongoCollection.find(query).iterator()) {
        try (MongoCursor<Document> cursor = getCollection(collectionName).find(query).iterator()) {
            StreamSupport.stream(Spliterators.spliteratorUnknownSize(cursor, Spliterator.ORDERED), false)
                    .collect(Collectors.toList()).forEach(x -> result.add(x));
            // log.debug("***MongoDB Query***");
            // log.debug("query : {}", query.toString());
            // List<String> filteredFruitNames = filterFruitNamesByQuery(queryKey, queryValue);
            // return (List<Document>) Response.ok(filteredFruitNames, MediaType.APPLICATION_JSON).build();
        } catch (MongoException me) {
            // log.error("***MongoDB Query***");
            // log.error("query : {}", query.toString());
            // log.error("Unable to query due to an error \n" + me);
        }

        return result;
    }

        // Sample method to filter fruit names based on key-value pair
    private List<String> filterFruitNamesByQuery(String queryKey, String queryValue) {
        List<String> fruitNames = new ArrayList<>();
        // Assuming fruitList contains the list of Fruit objects in your database
        // Filter the fruitList based on queryKey and queryValue
        // Add the filtered fruit names to the result list (filteredFruitNames)
        // For example:
        List<Fruit> fruitList = getFruitListFromDatabase();
        for (Fruit fruit : fruitList) {
            if (queryValue.equals(fruit.getName())) {
                fruitNames.add(fruit.getName());
            }
        }
        return fruitNames;
    }

    // Sample method to get a list of Fruit objects from the database
    private List<Fruit> getFruitListFromDatabase() {
        // Implement this method to fetch the list of Fruit objects from the database
        // For demonstration purposes, we'll return an empty list.
        return new ArrayList<>();
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return mongoClient.getDatabase("WIP").getCollection(collectionName);
    }
}