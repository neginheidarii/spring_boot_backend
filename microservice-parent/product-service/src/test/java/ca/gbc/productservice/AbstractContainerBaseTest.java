package ca.gbc.productservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public class AbstractContainerBaseTest {

    static final MongoDBContainer MONGO_DB_CONTAINER;

    static {
        MONGO_DB_CONTAINER = new MongoDBContainer("mongo:latest");
        MONGO_DB_CONTAINER.start();

//        the code from the testcontainers website, same as above:
//        var mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
//        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

}
