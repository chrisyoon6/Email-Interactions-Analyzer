package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Task1DWTests {

    private static DWInteractionGraph dwig;
    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph dwig4;
    private static DWInteractionGraph dwig5;
    private static DWInteractionGraph dwig6;
    private static DWInteractionGraph dwig7;
    private static DWInteractionGraph dwig8;
    private static DWInteractionGraph dwig9;
    private static DWInteractionGraph dwig10;
    private static DWInteractionGraph dwig11;
    private static DWInteractionGraph dwigADI;
    private static DWInteractionGraph dwigAEG;
    private static DWInteractionGraph dwigBEH;
    private static DWInteractionGraph dwigCFI;
    private static DWInteractionGraph dwigCFI2;
    private static DWInteractionGraph dwig_EuCoreTemporal;
    private static DWInteractionGraph dwig_EuCoreTemporalTest;
    private static DWInteractionGraph dwig_EuCoreTemporalTest2;
    private static DWInteractionGraph dwig_GraphTest;

    @BeforeAll
    public static void setupTests() {
        dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        dwigCFI = new DWInteractionGraph("resources/Task1-CFI");
        dwigADI = new DWInteractionGraph("resources/Task1-ADI");
        dwigAEG = new DWInteractionGraph("resources/Task1-AEG");
        dwigBEH = new DWInteractionGraph("resources/Task1-BEH");
        dwig1 = new DWInteractionGraph(dwig, new int[] {3, 9});
        dwig2 = new DWInteractionGraph(dwig, Arrays.asList(2, 3, 4));
        dwig3 = new DWInteractionGraph(dwig, new int[] {4, 6}); // none in time range
        dwig4 = new DWInteractionGraph(dwig, new int[] {0, 13}); // same time range
        dwigCFI2 = new DWInteractionGraph(dwigCFI, new int[] {1, 100}); // start before, end after
        dwig5 = new DWInteractionGraph(dwig, Arrays.asList(5, 6, 10)); // no users
        dwig6 = new DWInteractionGraph(dwig, Arrays.asList(2, 3, 4, 0, 1, 8)); // all users
        dwig7 =
            new DWInteractionGraph(dwig, Arrays.asList(2, 3, 4)); // only senders, only receivers
        dwig8 = new DWInteractionGraph("resources/Task1-2Transactions.txt", new int[] {3, 9});
        dwig9 = new DWInteractionGraph("resources/empty-interactions.txt");
        dwig10 = new DWInteractionGraph("resources/Task1-2Transactions.txt", new int[] {4, 6});
        dwig11 = new DWInteractionGraph("resources/self-send.txt");

        dwig_EuCoreTemporal = new DWInteractionGraph("resources/email-Eu-core-temporal.txt");
        dwig_EuCoreTemporalTest =
            new DWInteractionGraph(dwig_EuCoreTemporal, new int[] {39703992, 39704171});
        dwig_EuCoreTemporalTest2 =
            new DWInteractionGraph(dwig_EuCoreTemporalTest, Arrays.asList(293, 547, 752, 951));

        dwig_GraphTest = new DWInteractionGraph("resources/GraphTest.txt");

    }

    @Test
    public void test1GetUserIDsBase() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph1() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 4, 8));
        Assertions.assertEquals(expected, dwig1.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph2() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig2.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph8() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 4, 8));
        Assertions.assertEquals(expected, dwig8.getUserIDs());
    }

    @Test
    public void test1GetEmailCountBase() {
        Assertions.assertEquals(2, dwig.getEmailCount(2, 3));
        Assertions.assertEquals(0, dwig.getEmailCount(8, 4));
    }

    @Test
    public void test1GetEmailCountGraph1() {
        Assertions.assertEquals(1, dwig1.getEmailCount(1, 0));
        Assertions.assertEquals(1, dwig1.getEmailCount(8, 0));

    }

    @Test
    public void test1GetEmailCountGraph2() {
        Assertions.assertEquals(1, dwig2.getEmailCount(4, 8));
        Assertions.assertEquals(2, dwig2.getEmailCount(2, 3));
    }

    @Test
    public void test1GetEmailCountGraphSelfSend() {
        Assertions.assertEquals(2, dwig11.getEmailCount(1, 1));
    }

    // nothing in graph 3 (no intersect of time range)
    @Test
    public void test1GetEmailCountGraph3() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Assertions.assertEquals(0, dwig3.getEmailCount(i, j));
            }
        }
    }

    @Test
    public void test1GetUserIDsGraph3() {
        Set<Integer> expected = new HashSet<>();
        Assertions.assertEquals(expected, dwig3.getUserIDs());
    }

    @Test
    public void test1GetUserIDsEmptyGraph() {
        Set<Integer> expected = new HashSet<>();
        Assertions.assertEquals(expected, dwig9.getUserIDs());
        Assertions.assertEquals(expected, dwig10.getUserIDs());
    }

    // base and graph 4 should have same contents (same time range)
    @Test
    public void test1GetEmailCountGraph4() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Assertions.assertEquals(dwig.getEmailCount(i, j), dwig4.getEmailCount(i, j));
            }
        }
    }

    @Test
    public void test1GetUserIDsGraph4() {
        Assertions.assertEquals(dwig.getUserIDs(), dwig4.getUserIDs());
    }

    // CFI2 same as CFI but no userID = 0 and no (0,2) and one less (1,2) interaction
    @Test
    public void test1GetEmailCountGraph_CFI() {
        Assertions.assertEquals(2, dwigCFI2.getEmailCount(1, 2));
        Assertions.assertEquals(2, dwigCFI2.getEmailCount(1, 3));
        Assertions.assertEquals(1, dwigCFI2.getEmailCount(2, 1));

        Assertions.assertEquals(0, dwigCFI2.getEmailCount(0, 2));
        Assertions.assertEquals(0, dwigCFI2.getEmailCount(2, 0));
        Assertions.assertEquals(0, dwigCFI2.getEmailCount(2, 3));
        Assertions.assertEquals(0, dwigCFI2.getEmailCount(3, 0));
        Assertions.assertEquals(0, dwigCFI2.getEmailCount(3, 1));
        Assertions.assertEquals(0, dwigCFI2.getEmailCount(3, 2));

    }

    @Test
    public void test1GetUserIDsGraph_CFI() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(1, 2, 3));
        Assertions.assertEquals(expected, dwigCFI2.getUserIDs());
    }

    // nothing in graph 5 (no users)
    @Test
    public void test1GetEmailCountGraph5() {
        for (int i = 5; i < 11; i++) {
            for (int j = 5; j < 11; j++) {
                Assertions.assertEquals(0, dwig5.getEmailCount(i, j));
            }
        }
    }

    @Test
    public void test1GetUserIDsGraph5() {
        Set<Integer> expected = new HashSet<>();
        Assertions.assertEquals(expected, dwig5.getUserIDs());
    }

    // base and graph 6 should have same contents (same users)
    @Test
    public void test1GetEmailCountGraph6() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Assertions.assertEquals(dwig.getEmailCount(i, j), dwig6.getEmailCount(i, j));
            }
        }
    }

    @Test
    public void test1GetUserIDsGraph6() {
        Assertions.assertEquals(dwig.getUserIDs(), dwig4.getUserIDs());
    }

    // graph6 has users that only send and only receive from base
    @Test
    public void test1GetEmailCountGraph7() {
        Assertions.assertEquals(2, dwig7.getEmailCount(2, 3));
        Assertions.assertEquals(1, dwig7.getEmailCount(4, 8));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if ((i != 2 && j != 3) && (i != 4 && j != 8)) {
                    Assertions.assertEquals(0, dwig7.getEmailCount(i, j));
                }
            }
        }
    }

    @Test
    public void test1GetUserIDsGraph7() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig7.getUserIDs());
    }

    @Test
    public void test1GetEmailCountGraph_ADI() {
        Assertions.assertEquals(0, dwigADI.getEmailCount(0, 1));
        Assertions.assertEquals(0, dwigADI.getEmailCount(0, 2));
        Assertions.assertEquals(0, dwigADI.getEmailCount(0, 3));

        Assertions.assertEquals(1, dwigADI.getEmailCount(1, 2));
        Assertions.assertEquals(2, dwigADI.getEmailCount(1, 3));

        Assertions.assertEquals(0, dwigADI.getEmailCount(2, 0));
        Assertions.assertEquals(2, dwigADI.getEmailCount(2, 1));
        Assertions.assertEquals(1, dwigADI.getEmailCount(2, 3));

        Assertions.assertEquals(0, dwigADI.getEmailCount(3, 0));
        Assertions.assertEquals(1, dwigADI.getEmailCount(3, 1));
        Assertions.assertEquals(5, dwigADI.getEmailCount(3, 2));

        Assertions.assertEquals(2, dwigADI.getEmailCount(0, 0));
        Assertions.assertEquals(0, dwigADI.getEmailCount(1, 1));
        Assertions.assertEquals(1, dwigADI.getEmailCount(2, 2));
        Assertions.assertEquals(0, dwigADI.getEmailCount(3, 3));
    }

    @Test
    public void test1GetEmailCountGraph_AEG() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                Assertions.assertEquals(1, dwigAEG.getEmailCount(i, j));
            }
        }
    }

    @Test
    public void test1GetUserIDsGraph_AEG() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(1, 2, 3));
        Assertions.assertEquals(expected, dwigAEG.getUserIDs());
    }

    @Test
    public void testGetUserIDOfLargeGraph() {
        Set<Integer> expected = new HashSet<>(
            Arrays.asList(862, 293, 547, 178, 752, 323, 951, 391, 484, 288, 373, 353, 750));
        Assertions.assertEquals(expected, dwig_EuCoreTemporalTest.getUserIDs());
    }

    @Test
    public void test2GetUserIDOfLargeGraph() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(862, 293, 547, 178, 752, 323, 951));
        Assertions.assertEquals(expected, dwig_EuCoreTemporalTest2.getUserIDs());
    }

    @Test
    public void testGraphTestUserID() {
        Set<Integer> expected =
            new HashSet<>(Arrays.asList(11, 51, 23, 14, 501, 700, 80, 24, 114, 203));
        Assertions.assertEquals(expected, dwig_GraphTest.getUserIDs());
    }

    @Test
    public void testGraphTestEmailCount() {
        Assertions.assertEquals(0, dwig_GraphTest.getEmailCount(51, 23));
        Assertions.assertEquals(0, dwig_GraphTest.getEmailCount(23, 51));

        Assertions.assertEquals(4, dwig_GraphTest.getEmailCount(23, 11));
        Assertions.assertEquals(5, dwig_GraphTest.getEmailCount(11, 23));

        Assertions.assertEquals(3, dwig_GraphTest.getEmailCount(51, 700));
        Assertions.assertEquals(0, dwig_GraphTest.getEmailCount(700, 51));

        Assertions.assertEquals(3, dwig_GraphTest.getEmailCount(80, 80));
    }


}
