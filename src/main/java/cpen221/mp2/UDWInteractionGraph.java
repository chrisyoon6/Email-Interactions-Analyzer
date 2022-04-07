package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UDWInteractionGraph {

    private List<User> users = new ArrayList<>();

    /*
     * Rep Invariant:
     * each element of users is not null
     * each element of users is unique, which means it has a unique user ID
     *
     * Abstraction Function:
     * users represents the nodes in this graph
     */

    /**
     * Check that the rep invariant is true
     */
    private void checkRep() {
        assert users != null;

        Set<Integer> seenIds = new HashSet<>();
        for (User user : users) {
            assert user != null;
            assert !seenIds.contains(user.getId());
            seenIds.add(user.getId());
        }
    }

    /* ------- Task 1 ------- */
    /* Building the Constructors */

    /**
     * Creates a new UDWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public UDWInteractionGraph(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null;
                 fileLine = reader.readLine()) {
                String[] values = fileLine.split(" ");
                int senderId = Integer.parseInt(values[0]);
                int receiverId = Integer.parseInt(values[1]);
                int timeStamp = Integer.parseInt(values[2]);

                addToGraph(senderId, receiverId, timeStamp);

                // now add "backwards" with no duplicates
                if (receiverId != senderId) {
                    addToGraph(receiverId, senderId, timeStamp);
                }
            }
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }

        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph using an email interaction file and considering
     * a time window filter.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public UDWInteractionGraph(String fileName, int[] timeWindow) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null;
                 fileLine = reader.readLine()) {
                String[] values = fileLine.split(" ");
                int senderId = Integer.parseInt(values[0]);
                int receiverId = Integer.parseInt(values[1]);
                int timeStamp = Integer.parseInt(values[2]);

                if (timeStamp >= timeWindow[0] && timeStamp <= timeWindow[1]) {
                    addToGraph(senderId, receiverId, timeStamp);

                    // now add "backwards" with no duplicates
                    if (receiverId != senderId) {
                        addToGraph(receiverId, senderId, timeStamp);
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }

        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created UDWInteractionGraph
     *                   should only include those emails in the input
     *                   UDWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, int[] timeFilter) {
        for (User user : inputUDWIG.users) {
            User userCopy = user.filterToTimeRange(timeFilter[0], timeFilter[1]);

            if (!userCopy.getEdges().isEmpty()) {
                this.users.add(new User(userCopy));
            }
        }

        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param userFilter a List of User IDs. The created UDWInteractionGraph
     *                   should exclude those emails in the input
     *                   UDWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, List<Integer> userFilter) {
        for (User user : inputUDWIG.users) {
            User userCopy = new User(user.getId());

            if (!userFilter.contains(userCopy.getId())) {
                for (Edge edge : user.getEdges()) {
                    for (int userId : userFilter) {
                        if (edge.getId() == userId) {
                            userCopy.addEdge(new Edge(edge));
                        }
                    }
                }
            } else {
                userCopy = new User(user);
            }

            if (!userCopy.getEdges().isEmpty()) {
                users.add(userCopy);
            }
        }

        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph from a DWInteractionGraph object.
     *
     * @param inputDWIG a DWInteractionGraph object
     */
    public UDWInteractionGraph(DWInteractionGraph inputDWIG) {
        for (User user : inputDWIG.getUsers()) {
            int senderId = user.getId();

            List<Edge> edges = user.getEdges();
            for (Edge edge : edges) {
                int receiverId = edge.getId();

                for (int time : edge.getTimes()) {
                    addToGraph(senderId, receiverId, time);

                    if (senderId != receiverId) {
                        addToGraph(receiverId, senderId, time);
                    }
                }
            }
        }

        checkRep();
    }

    /**
     * Updates the UDWInteractionGraph instance by adding a new email interaction between two
     * users at a certain time
     * modifies @code{this}
     *
     * @param senderId   the user ID of the sender of the email
     * @param receiverId the user ID of the receiver of the email
     * @param timeStamp  the time at which the email was sent
     */
    private void addToGraph(int senderId, int receiverId, int timeStamp) {
        User user = new User(senderId);
        Edge edge = new Edge(receiverId, timeStamp);
        user.addEdge(edge);
        addUser(user);
    }

    /**
     * Adds a user to the current UDWInteractionGraph instance
     * modifies @code{this}
     *
     * @param user the user to add to the graph
     */
    private void addUser(User user) {
        boolean added = false;

        for (User u : users) {
            if (u.getId() == user.getId()) {
                for (Edge edge : user.getEdges()) {
                    u.addEdge(edge);
                }

                added = true;
                break;
            }
        }

        if (!added) {
            users.add(user);
        }
    }

    /**
     * Obtains a set of userIDs of the users that are within this UDWInteractionGraph.
     *
     * @return a Set of Integers, where every element in the set is a User ID
     * in this UDWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        Set<Integer> userIds = new HashSet<>();

        for (User user : users) {
            userIds.add(user.getId());
        }

        return userIds;
    }

    /**
     * Obtains the number of emails sent between two users
     *
     * @param user1 the User ID of the first user.
     * @param user2 the User ID of the second user.
     * @return the number of email interactions (send/receive) between user1 and user2
     * 0 if one or both users are not in the graph
     */
    public int getEmailCount(int user1, int user2) {
        int index1 = findIndexOfUser(user1);
        int index2 = findIndexOfUser(user2);
        int emailCount = 0;
        if (index1 == -1 || index2 == -1) {
            return emailCount;
        }
        for (Edge edge : users.get(index1).getEdges()) {
            if (edge.getId() == user2) {
                emailCount = edge.getTimes().size();
            }
        }

        return emailCount;
    }

    /* ------- Task 2 ------- */

    /**
     * Given an int array, [t0, t1], reports email transaction details.
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included in this report.
     *
     * @param timeWindow is an int array of size 2 [t0, t1]
     *                   where t0<=t1
     * @return an int array of length 2, with the following structure:
     * [NumberOfUsers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        UDWInteractionGraph copy = new UDWInteractionGraph(this, timeWindow);

        int numEmails = 0;
        for (User user : copy.users) {
            for (Edge edge : user.getEdges()) {
                numEmails += edge.getWeight();

                if (edge.getId() == user.getId()) {
                    //sent to itself
                    numEmails += edge.getWeight();
                }
            }
        }

        numEmails /= 2;

        return new int[] {copy.users.size(), numEmails};
    }

    /**
     * Given a User ID, reports the specified User's email transaction history.
     *
     * @param userID the User ID of the user for which
     *               the report will be created
     * @return an int array of length 2 with the following structure:
     * [NumberOfEmails, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0].
     */
    public int[] ReportOnUser(int userID) {
        int[] report = {0, 0};
        int userIndex = findIndexOfUser(userID);
        if (userIndex == -1) {
            return report;
        }
        for (Edge edge : users.get(userIndex).getEdges()) {
            report[0] += edge.getTimes().size();
            report[1]++;
        }
        return report;
    }

    /**
     * Obtains the Nth most active user of this UDWInteractionGraph instance, where N = 1 means the
     * most active and N = x means the least active for x number of users in this graph
     * modifies: this
     *
     * @param N a positive number representing rank. N=1 means the most active.
     * @return the User ID for the Nth most active user
     * if N is larger than the number of users return -1
     */
    public int NthMostActiveUser(int N) {
        if (N > users.size()) {
            return -1;
        }

        List<int[]> activity = new ArrayList<>();
        Set<Integer> seenUsers = new HashSet<>();

        for (User user : users) {
            if (!seenUsers.contains(user.getId())) {
                seenUsers.add(user.getId());
                int[] emails = ReportOnUser(user.getId());
                activity.add(new int[] {user.getId(), emails[0]});
            }
        }

        activity.sort(
            (o1, o2) -> { // sorts this list in descending order of emailCounts of this index (user)
                int leftIndexEmailCount = o1[1];
                int rightIndexEmailCount = o2[1];

                if (leftIndexEmailCount < rightIndexEmailCount) {
                    return 1;
                } else if (leftIndexEmailCount == rightIndexEmailCount) {
                    if (o1[0] < o2[0]) {    // same emails, sorted by lower userID
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return -1;
                }
            });
        if (activity.size() < N) {
            return -1;
        }
        int[] mostActiveUserInfo = activity
            .get(N - 1); // array of emails and user of the Nth most active user (sent or received)
        return mostActiveUserInfo[0];   // index 0 of this array gives the Nth most active user's ID
    }



    /* ------- Task 3 ------- */

    /**
     * Obtains the number of completely disjoint graph components.
     *
     * @return @code{int} representing disjoint components in this graph
     */
    public int NumberOfComponents() {
        boolean[] seen = new boolean[users.size()];
        int numberOfComponents = 0;

        for (int i = 0; i < users.size(); i++) {
            if (!seen[i]) {
                numberOfComponents++;
                visitingNodes(i, seen);
            }
        }

        return numberOfComponents;
    }

    /**
     * Recursive function to visit the nodes of the graph, marking which nodes have been visited
     * before visiting new nodes
     *
     * @param index the index of the user from which other users are being visited
     *              0<= index < users.size
     * @param seen  each value represents whether a user was visited (true) or not (false)
     *              indices of seen represent same users as users indices
     *              Updates seen to account for the users that are visited (change false to true)
     */
    private void visitingNodes(int index, boolean[] seen) {
        seen[index] = true;

        for (Edge currentEdge : users.get(index).getEdges()) {
            if (!seen[findIndexOfUser(currentEdge.getId())]) {
                visitingNodes(findIndexOfUser(currentEdge.getId()), seen);
            }
        }
    }

    /**
     * Gets the index that represents the user with ID of userId in users list
     * or -1 if the user is not present in the graph object
     *
     * @param userId the user ID to search for
     * @return the index of the user with a given ID, or -1 if user with given ID not present
     */
    private int findIndexOfUser(int userId) {
        for (int index = 0; index < users.size(); index++) {
            if (users.get(index).getId() == userId) {
                return index;
            }
        }

        return -1; //bad thing
    }

    /**
     * Determines if a path exists in the undirected graph between two users
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return whether a path exists between the two users
     * returns false if one or both users are not in the graph
     */
    public boolean PathExists(int userID1, int userID2) {
        boolean[] seen = new boolean[users.size()];
        if (findIndexOfUser(userID1) == -1 || findIndexOfUser(userID2) == -1) {
            return false;
        }
        visitingNodes(findIndexOfUser(userID1), seen);
        return seen[findIndexOfUser(userID2)];
    }
}
