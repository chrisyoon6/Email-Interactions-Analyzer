package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task1UDWTests {
    private static UDWInteractionGraph testGraphBase;
    private static UDWInteractionGraph udwig1;
    private static UDWInteractionGraph udwig2;
    private static UDWInteractionGraph udwig3;
    private static UDWInteractionGraph udwig4;
    private static UDWInteractionGraph udwig5;
    private static UDWInteractionGraph udwig6;
    private static UDWInteractionGraph udwig7;
    private static UDWInteractionGraph udwig8;

    @BeforeAll
    public static void setupTests() {
        testGraphBase = new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        udwig1 = new UDWInteractionGraph("resources/simple-interaction.txt");
        udwig2 = new UDWInteractionGraph(testGraphBase, new int[] {0, 9});
        udwig3 = new UDWInteractionGraph(testGraphBase, new int[] {10, 11});

        udwig4 = new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt", new int[] {0, 9});
        udwig5 = new UDWInteractionGraph("resources/empty-interactions.txt");
        udwig6 =
            new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt", new int[] {13, 14});
        udwig7 = new UDWInteractionGraph(testGraphBase, new int[] {13, 14});
        udwig8 = new UDWInteractionGraph("resources/self-send.txt");
    }

    @Test
    public void testGetUserIds() {
        Assertions
            .assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), testGraphBase.getUserIDs());
    }

    @Test
    public void testGetUserIds1() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), udwig3.getUserIDs());
    }

    @Test
    public void testGetEmailCount() {
        Assertions.assertEquals(2, testGraphBase.getEmailCount(1, 0));
        Assertions.assertEquals(2, testGraphBase.getEmailCount(0, 1));
    }

    @Test
    public void testGetEmailCount1() {
        Assertions.assertEquals(2, udwig2.getEmailCount(1, 0));
        Assertions.assertEquals(2, udwig2.getEmailCount(0, 3));
    }

    @Test
    public void testGetEmailCount2() {
        Assertions.assertEquals(0, udwig3.getEmailCount(1, 0));
        Assertions.assertEquals(1, udwig3.getEmailCount(1, 3));
    }

    @Test
    public void testGetEmailCount3() {
        Assertions.assertEquals(4, udwig1.getEmailCount(0, 1));
        Assertions.assertEquals(4, udwig1.getEmailCount(1, 0));
        Assertions.assertEquals(2, udwig1.getEmailCount(0, 2));
        Assertions.assertEquals(2, udwig1.getEmailCount(2, 0));
        Assertions.assertEquals(0, udwig1.getEmailCount(2, 1));
        Assertions.assertEquals(2, udwig1.getEmailCount(3, 3));
    }

    @Test
    public void testGetEmailCount8() {
        Assertions.assertEquals(2, udwig8.getEmailCount(1, 1));
        Assertions.assertEquals(0, udwig8.getEmailCount(2, -1));
        Assertions.assertEquals(0, udwig8.getEmailCount(-1, 2));
    }

    @Test
    public void test1GetUserIDsEmptyGraph() {
        Set<Integer> expected = new HashSet<>();
        Assertions.assertEquals(expected, udwig5.getUserIDs());
        Assertions.assertEquals(expected, udwig6.getUserIDs());
        Assertions.assertEquals(expected, udwig7.getUserIDs());
    }

    @Test
    public void testUserConstructor() {
        List<Integer> userFilter = Arrays.asList(0, 1);
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, userFilter);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), t.getUserIDs());
        Assertions.assertEquals(2, t.getEmailCount(0, 1));
        Assertions.assertEquals(2, t.getEmailCount(0, 3));
    }

    @Test
    public void testConstructionFromDW() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }

    @Test
    public void testConstructionFromDW1() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }

}
