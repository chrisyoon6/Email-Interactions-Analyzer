package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UDWMadeTests {
    private static UDWInteractionGraph testGraphBase;
    private static UDWInteractionGraph testGraph1;
    private static UDWInteractionGraph testGraph2;

    @BeforeAll
    public static void setupTests() {
        testGraphBase = new UDWInteractionGraph("resources/GraphTest.txt");
    }

    @Test
    public void testEmailCount() {
        Assertions.assertEquals(0, testGraphBase.getEmailCount(51, 23));
        Assertions.assertEquals(9, testGraphBase.getEmailCount(23, 11));
        Assertions.assertEquals(3, testGraphBase.getEmailCount(51, 700));
        Assertions.assertEquals(3, testGraphBase.getEmailCount(80, 80));
    }

    @Test
    public void testActivityInTimeWindow() {
        Assertions.assertArrayEquals(new int[] {7, 5},
            testGraphBase.ReportActivityInTimeWindow(new int[] {37, 55}));
        Assertions.assertArrayEquals(new int[] {0, 0},
            testGraphBase.ReportActivityInTimeWindow(new int[] {38, 40}));
        Assertions.assertArrayEquals(new int[] {10, 45},
            testGraphBase.ReportActivityInTimeWindow(new int[] {0, 500}));
    }

    @Test
    public void testReportOnUser() {
        Assertions.assertArrayEquals(new int[] {9, 3}, testGraphBase.ReportOnUser(80));
        Assertions.assertArrayEquals(new int[] {14, 4}, testGraphBase.ReportOnUser(24));
        Assertions.assertArrayEquals(new int[] {5, 2}, testGraphBase.ReportOnUser(51));
    }

    @Test
    public void testNthMostActive() {
        Assertions.assertEquals(24, testGraphBase.NthMostActiveUser(1));
        Assertions.assertEquals(501, testGraphBase.NthMostActiveUser(7));
        Assertions.assertEquals(203, testGraphBase.NthMostActiveUser(10));
        Assertions.assertEquals(-1, testGraphBase.NthMostActiveUser(11));
    }

    @Test
    public void testNumComponent() {
        Assertions.assertEquals(3, testGraphBase.NumberOfComponents());
    }

    @Test
    public void testPathExists() {
        Assertions.assertEquals(false, testGraphBase.PathExists(51, 23));
        Assertions.assertEquals(true, testGraphBase.PathExists(80, 23));
        Assertions.assertEquals(true, testGraphBase.PathExists(80, 80));
        Assertions.assertEquals(true, testGraphBase.PathExists(501, 114));
        Assertions.assertEquals(false, testGraphBase.PathExists(80, 3000));
    }
}
