package de.tum.rules.testenv2;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.util.Map;

public class Neo4jCallGraph {
    private final Driver driver;

    public Neo4jCallGraph(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void close() {
        driver.close();
    }

    public void addMethodNode(String methodSignature) {
        try (Session session = driver.session()) {
            session.run("MERGE (m:Method {signature: $signature})", Map.of("signature", methodSignature));
        }
    }

    public void addCallEdge(String callerSignature, String calleeSignature) {
        try (Session session = driver.session()) {
            session.run("MATCH (caller:Method {signature: $callerSignature}), " + "(callee:Method {signature: $calleeSignature}) " + "MERGE (caller)-[:CALLS]->(callee)", Map.of("callerSignature", callerSignature, "calleeSignature", calleeSignature));
        }
    }

    public void deleteMethodNode(String methodSignature) {
        try (Session session = driver.session()) {
            session.run("MATCH (m:Method {signature: $signature}) DETACH DELETE m", Map.of("signature", methodSignature));
        }
    }

    public void deleteCallEdge(String callerSignature, String calleeSignature) {
        try (Session session = driver.session()) {
            session.run("MATCH (caller:Method {signature: $callerSignature})-[r:CALLS]->(callee:Method {signature: $calleeSignature}) DELETE r", Map.of("callerSignature", callerSignature, "calleeSignature", calleeSignature));
        }
    }

    public void deleteAllData() {
        try (Session session = driver.session()) {
            session.run("MATCH (n) DETACH DELETE n");
        }
    }

}