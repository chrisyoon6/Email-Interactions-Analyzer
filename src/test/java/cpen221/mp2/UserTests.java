package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTests {

    private static User user1;
    private static User user2;
    private static User user3;
    private static User user4;

    @BeforeAll
    public static void setupTests() {
        user1 = new User(0);
        user2 = new User(0);
        user3 = new User(0);
        user4 = new User(1);

        user1.addEdge(new Edge(0, 10));

        user2.addEdge(new Edge(0, 10));

        user3.addEdge(new Edge(0, 10));
        user3.addEdge(new Edge(1, 40));

        user4.addEdge(new Edge(0, 10));
    }

    @Test
    public void testUserRepExposure() {
        user1.getEdges().add(new Edge(0, 1));
        user1.getEdges().add(new Edge(0, 2));
        user1.getEdges().add(new Edge(1, 10));

        List<Edge> expected = new ArrayList<>();
        expected.add(new Edge(0, 10));

        Assertions.assertEquals(expected, user1.getEdges());
    }

    @Test
    public void testUserEquality() {
        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user3, user3);
        Assertions.assertNotEquals(user1, null);
        Assertions.assertNotEquals(user1, new ArrayList<>());
        Assertions.assertNotEquals(user1, user3);
        Assertions.assertNotEquals(user1, user4);
        Assertions.assertNotEquals(user2, user3);
        Assertions.assertNotEquals(user2, user4);
        Assertions.assertNotEquals(user3, user4);

        Assertions.assertEquals(user1.hashCode(), user2.hashCode());
    }

}
