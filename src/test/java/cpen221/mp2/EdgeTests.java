package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class EdgeTests {

    private static Edge edge1;
    private static Edge edge2;
    private static Edge edge3;

    @BeforeAll
    public static void setupTests() {
        edge1 = new Edge(1, 10);
        edge2 = new Edge(1, 10);
        edge3 = new Edge(1, 30);
    }

    @Test
    public void testEdgeRepExposure() {
        edge1.getTimes().add(20);
        edge1.getTimes().add(30);

        List<Integer> expected = new ArrayList<>();
        expected.add(10);

        Assertions.assertEquals(expected, edge1.getTimes());
    }

    @Test
    public void testEdgeEquality() {
        Assertions.assertNotEquals(edge1, null);
        Assertions.assertNotEquals(edge1, new ArrayList<>());
        Assertions.assertNotEquals(edge1, edge3);
        Assertions.assertEquals(edge1, edge2);
        Assertions.assertEquals(edge1, edge1);

        Assertions.assertEquals(edge1.hashCode(), edge2.hashCode());
    }

}
