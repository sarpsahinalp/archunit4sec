package de.tum.rules.testenv2;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Neo4jDriver {
    private final Driver driver;
    private final Set<String> methodNodes;
    private final Set<String[]> methodCalls;

    public Neo4jDriver(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        methodNodes = new HashSet<>();
        methodCalls = new HashSet<>();
    }

    public void close() {
        driver.close();
    }

    public void saveToMethodNodes(String methodName) {
        methodNodes.add(methodName);
    }

    public void saveToMethodCalls(String[] methodCall) {
        methodCalls.add(methodCall);
    }

    public void addMethodNode(String methodName) {
        try (Session session = driver.session()) {
            session.run("CREATE (m:Method {name: $name})",
                    org.neo4j.driver.Values.parameters("name", methodName));
        }
    }

    public void addMethodCall(String caller, String callee) {
        try (Session session = driver.session()) {
            session.run("MATCH (a:Method {name: $caller}), (b:Method {name: $callee}) " +
                            "CREATE (a)-[:CALLS]->(b)",
                    org.neo4j.driver.Values.parameters("caller", caller, "callee", callee));
        }
    }

    public void deleteMethodNode(String methodName) {
        try (Session session = driver.session()) {
            session.run("MATCH (m:Method {name: $name}) DETACH DELETE m",
                    org.neo4j.driver.Values.parameters("name", methodName));
        }
    }

    public void deleteMethodCall(String caller, String callee) {
        try (Session session = driver.session()) {
            session.run("MATCH (a:Method {name: $caller})-[r:CALLS]->(b:Method {name: $callee}) DELETE r",
                    org.neo4j.driver.Values.parameters("caller", caller, "callee", callee));
        }
    }

    public void deleteAll() {
        try (Session session = driver.session()) {
            session.run("MATCH (n) DETACH DELETE n");
        }
    }

    public void saveNodesToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("name\n");
            for (String method : methodNodes) {
                writer.append(method).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCallsToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("caller,callee\n");
            for (String[] call : methodCalls) {
                writer.append(call[0]).append(",").append(call[1]).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importMethodsFromCSV(String filePath) {
        try (Session session = driver.session()) {
            session.run("LOAD CSV WITH HEADERS FROM $filePath AS row " +
                            "CREATE (m:Method {name: row.name})",
                    org.neo4j.driver.Values.parameters("filePath", "file:///" + filePath));
        }
    }

    public void importCallsFromCSV(String filePath) {
        try (Session session = driver.session()) {
            session.run("LOAD CSV WITH HEADERS FROM $filePath AS row " +
                            "MATCH (a:Method {name: row.caller}), (b:Method {name: row.callee}) " +
                            "CREATE (a)-[:CALLS]->(b)",
                    org.neo4j.driver.Values.parameters("filePath", "file:///" + filePath));
        }
    }
}
