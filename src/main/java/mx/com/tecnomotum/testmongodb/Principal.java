/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.tecnomotum.testmongodb;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author carlos
 */
public class Principal {
    public static void main(String args[]){
        MongoClient mongoClient = new MongoClient("localhost",27017);
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> coleccion = db.getCollection("restaurants");
        long totalElementos = coleccion.count();
        System.out.println("Total de elementos en la colección:" + totalElementos);
        
        // Obtener el primer elemento de la colección
        Document myDoc = coleccion.find().first();
        System.out.println("Primer object:" + myDoc.toJson());
        
        //Crear y añadir un nuevo documento a la colección
        Document nuevoDoc = new Document("name","CARLITOS bufé");
        nuevoDoc.append("borough","Elviña");
        nuevoDoc.append("cuisine", "Gourmet");
        List<Document> puntuaciones = new ArrayList<>();
        Document punt = new Document();
        punt.append("grade","A");
        punt.append("date",new Date());
        punt.append("score", 9);
        puntuaciones.add(punt);
        nuevoDoc.append("grades", puntuaciones);
        coleccion.insertOne(nuevoDoc);
        System.out.println("Total de elementos en la colección:" + coleccion.count());
        
        //OBtener un objeto de una colección
        Document objetoResp = coleccion.find(eq("name","CARLITOS bufé")).first();
        System.out.println("OBjeto encontrado:" + objetoResp.toJson());

        //OBtener la proyección del documento
        Document objetoResp2 = coleccion.find(eq("name","CARLITOS bufé")).projection(fields(excludeId(),include("name"),include("grades.score"))).first();
        System.out.println("OBjeto encontrado:" + objetoResp2.toJson());
        
        //OBtener conjuntos de datos
        Block<Document> printBlock = new Block<Document>(){

            @Override
            public void apply(final Document doc) {
                System.out.println(doc.toJson()
                );
            }
        };
        
        coleccion.find(eq("cuisine", "Hamburgers")).projection(fields(excludeId(),include("name"))).sort(Sorts.ascending("name")).forEach(printBlock);
        
    }
}
