package org.acme.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import io.vertx.core.json.JsonObject;

import org.bson.Document;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FruitService {

    @Inject
    MongoClient mongoClient;

    public void addJsonToMongoDB(String json, String collectionName) {
        Document document = Document.parse(json);
        getCollection(collectionName).insertOne(document);
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return mongoClient.getDatabase("mydb").getCollection(collectionName);
    }
}