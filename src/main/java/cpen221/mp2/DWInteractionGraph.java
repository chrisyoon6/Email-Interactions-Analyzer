package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/*
 * This class represents a directed weighted graph of user interactions within a specific window of time.
 */
public class DWInteractionGraph {
    private List<User> users = new ArrayList<>();
    private Set<Integer> senderIds;

    /*
     * Rep Invariant:
     * each element in users is unique, does not change, users not null and elements of users not null
     * each element in senderIds is unique, does not change, not null and its elements are not null
     * users.size() == senderIds.size()
     *
     * Abstraction Function:
     * users represents the collections of users that send emails (edges) in this graph, or in other words the nodes who are tails of edges
     * senderIds represents the IDs of the users who send emails in this graph, or in other words the IDs of the nodes who are tails of edges
     */

    /**
     * Check that the rep invariant is true
     */
    private void checkRep() {
        assert users != null;
        assert senderIds != null;
        assert users.size() == senderIds.size();

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
     * Creates a new DWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public DWInteractionGraph(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null;
                 fileLine = reader.readLine()) {
                String[] values = fileLine.split(" ");
                int senderID = Integer.parseInt(values[0]);
                int receiverID = Integer.parseInt(values[1]);
                int timeStamp = Integer.parseInt(values[2]);

                addToGraph(senderID, receiverID, timeStamp);
            }
        } catch (IOException ioe) {
            System.out.println("Problem reading file: " + fileName);
        }

        senderIds = getSenderIDs();

        checkRep();
    }

    /**
     * Creates a new DWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public DWInteractionGraph(String fileName, int[] timeWindow) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null;
                 fileLine = reader.readLine()) {
                String[] values = fileLine.split(" ");
                int senderID = Integer.parseInt(values[0]);
                int receiverID = Integer.parseInt(values[1]);
                int timeStamp = Integer.parseInt(values[2]);

                if (timeStamp >= timeWindow[0] && timeStamp <= timeWindow[1]) {
                    addToGraph(senderID, receiverID, timeStamp);
                }
            }
        } catch (IOException ioe) {
            System.out.println("Problem reading file: " + fileName);
        }

        senderIds = getSenderIDs();

        checkRep();
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the input
     *                   DWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, int[] timeFilter) {
        for (User user : inputDWIG.users) {
            User userCopy = user.filterToTimeRange(timeFilter[0], timeFilter[1]);

            if (!userCopy.getEdges().isEmpty()) {
                this.users.add(new User(userCopy));
            }
        }

        senderIds = getSenderIDs();

        checkRep();
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *                   should exclude those emails in the input
     *                   DWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, List<Integer> userFilter) {
        for (User user : inputDWIG.users) {
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

        senderIds = getSenderIDs();

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
     * Obtains a set of userIDs of the users that are within this DWInteractionGraph.
     *
     * @return a set of Integers, where every element in the set is a userID
     * in this DWInteractionGraph, not null and elements are not null
     */
    public Set<Integer> getUserIDs() {
        Set<Integer> userIds = new HashSet<>();

        for (User user : users) {
            userIds.add(user.getId());

            for (Edge edge : user.getEdges()) {
                userIds.add(edge.getId());
            }
        }

        return userIds;
    }

    /**
     * Obtains a set of userIDs of the users that have sent emails
     * within this DWInteractionGraph.
     *
     * @return a @code{Set<Integer>} where every element in the set is a userID
     * in this DWInteractionGraph, not null and elements are not null
     */
    public Set<Integer> getSenderIDs() {
        Set<Integer> userIds = new HashSet<>();

        for (User user : users) {
            userIds.add(user.getId());
        }

        return userIds;
    }

    /**
     * Obtains the number of emails sent from a sender to a receiver.
     *
     * @param sender   the User ID of the sender in the email transaction.
     * @param receiver the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     * receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        int senderIndex = findIndexOfUser(sender);
        boolean receiverExists = false;

        int emailCount = 0;
        if (senderIndex == -1) {
            return emailCount;
        }

        for (Edge edge : users.get(senderIndex).getEdges()) {
            if (edge.getId() == receiver) {
                emailCount = edge.getTimes().size();
                receiverExists = true;
                break;
            }
        }

        if (!receiverExists) {
            return 0;
        }

        return emailCount;
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
     * Gets a copy of the list of senders in this DWIG instance, including each
     * user's email interactions.
     *
     * @return @code{List<User>} of the senders in this DWIG instance, is not @code{null}
     */
    protected List<User> getUsers() {
        List<User> usersCopy = new ArrayList<>();

        for (User user : users) {
            User userCopy = new User(user.getId());

            for (Edge edge : user.getEdges()) {
                Edge edgeCopy = new Edge(edge);
                userCopy.addEdge(edgeCopy);
            }

            usersCopy.add(user);
        }

        return usersCopy;
    }

    /* ------- Task 2 ------- */

    /**
     * Given an int array, [t0, t1], reports email transaction details.
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included in this report.
     *
     * @param timeWindow is an int array of size 2 [t0, t1] where t0<=t1.
     * @return an int array of length 3, with the following structure:
     * [NumberOfSenders, NumberOfReceivers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        DWInteractionGraph copy = new DWInteractionGraph(this, timeWindow);

        int numSenders = copy.users.size();
        int numReceivers = 0;
        int numEmails = 0;

        Set<Integer> receiverIds = new HashSet<>();
        for (User user : copy.users) {
            for (Edge edge : user.getEdges()) {
                receiverIds.add(edge.getId());
                numEmails += edge.getWeight();
            }
        }
        numReceivers = receiverIds.size();

        return new int[] {numSenders, numReceivers, numEmails};
    }

    /**
     * Given a User ID, reports the specified User's email transaction history.
     *
     * @param userID the User ID of the user for which the report will be
     *               created.
     * @return an int array of length 3 with the following structure:
     * [NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0, 0].
     */
    public int[] ReportOnUser(int userID) {
        boolean userInGraph = false;

        int numEmailsSent = 0;
        int numEmailsReceived = 0;
        int uniqueUsersInteracted;

        Set<Integer> uniqueInteractions = new HashSet<>();

        for (User user : users) {
            if (user.getId() == userID) {
                for (Edge edge : user.getEdges()) {
                    numEmailsSent += edge.getWeight();
                    uniqueInteractions.add(edge.getId());

                    if (edge.getId() == userID) {
                        //sent to itself
                        numEmailsReceived += edge.getWeight();
                    }
                }

                userInGraph = true;
            } else {
                //not sender, check if receiver
                for (Edge edge : user.getEdges()) {
                    if (edge.getId() == userID) {
                        numEmailsReceived += edge.getWeight();
                        uniqueInteractions.add(user.getId());
                        userInGraph = true;
                    }
                }
            }
        }

        uniqueUsersInteracted = uniqueInteractions.size();

        if (!userInGraph) {
            return new int[] {0, 0, 0};
        }

        return new int[] {numEmailsSent, numEmailsReceived, uniqueUsersInteracted};
    }

    /**
     * Obtains the Nth most active user of this DWInteractionGraph in the specified interaction
     * type, where N=1 means the most active, and N = x means the least active for x number of
     * users in this DWInteractionGraph.
     * Modifies: this
     *
     * @param N               a positive number representing rank. N=1 means the most active.
     * @param interactionType Represent the type of interaction to calculate the rank for
     *                        Can be SendOrReceive.Send or SendOrReceive.RECEIVE
     * @return the User ID for the Nth most active user in specified interaction type.
     * Sorts User IDs by their number of sent or received emails first. In the case of a
     * tie, secondarily sorts the tied User IDs in ascending order.
     */
    public int NthMostActiveUser(int N, SendOrReceive interactionType) {
        if (N > getUserIDs().size()) {
            return -1;
        }

        List<int[]> userReports = new ArrayList<>();
        int index = interactionType == SendOrReceive.SEND ? 0 : 1;

        for (User user : users) {
            int[] userReport = ReportOnUser(user.getId());
            int emails = userReport[index];

            if (emails != 0) {
                userReports.add(new int[] {user.getId(), userReport[index]});
            }
        }

        userReports.sort(
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

        if (userReports.size() < N) {
            return -1;
        }

        int[] mostActiveUserInfo = userReports
            .get(N - 1); // array of emails and user of the Nth most active user (sent or received)
        return mostActiveUserInfo[0]; // index 0 of this array gives the Nth most active user's ID
    }


    /* ------- Task 3 ------- */

    /**
     * Performs breadth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    // TODO: create tests
    public List<Integer> BFS(int userID1, int userID2) {
        LinkedList<Integer> queue = new LinkedList<>();
        List<Integer> searched = new ArrayList<>();
        queue.add(userID1);
        searched.add(userID1);
        if (BFSPathFromSourceToTarget(userID2, queue, searched)) {
            return searched;    // path exists from user1 to user2
        } else {    // now check for from user2 to user1 (must clear)
            queue = new LinkedList<>();
            searched = new ArrayList<>();
            queue.add(userID2);
            searched.add(userID2);
            if (BFSPathFromSourceToTarget(userID1, queue, searched)) {
                return searched;     // path exists from user2 to user1
            } else {
                return null;    // no path in each direction
            }
        }
    }

    /**
     * Performs breadth first search on the DWInteractionGraph object to check path from the
     * users in the queue to the target user with targetID.
     *
     * @param targetID the user ID of the target user
     * @param queue    a linked list of user IDs of the source users, not null and elements are not null. Can be modified during the method call.
     * @param searched a list of user IDs of the users that have been visited during the breadth first search, not null and elements are not null. Can be modified during the method call.
     * @return true if there is a path between the source users and the target user, false otherwise.
     */
    private boolean BFSPathFromSourceToTarget(int targetID, LinkedList<Integer> queue,
                                              List<Integer> searched) {
        if (queue.isEmpty()) {
            return false;
        }
        int userToSearch = queue.poll();
        if (userToSearch == targetID) { // edge case - if source and target are same at the start
            return true;
        }
        if (senderIds.contains(userToSearch)) {
            User user = users.get(findIndexOfUser(userToSearch));
            List<Integer> toBeAdded = new ArrayList<>();
            boolean found = false;
            for (Edge edge : user.getEdges()) {
                int receivingID = edge.getId();
                if (receivingID ==
                    targetID) { // must be checked before other condition - if user and target are same
                    toBeAdded.add(receivingID);
                    found = true;
                    break;
                }
                if (!searched.contains(receivingID)) {
                    toBeAdded.add(receivingID);
                }
            }
            // sorting queue and searched to add smallest id of users in adjacent edges
            Collections.sort(toBeAdded);
            searched.addAll(toBeAdded);
            queue.addAll(toBeAdded);
            if (found) {
                return true;
            }
        }
        return BFSPathFromSourceToTarget(targetID, queue, searched);
    }

    /**
     * Performs depth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> DFS(int userID1, int userID2) {
        List<Integer> searchedUsers = new ArrayList<>();
        if (DFSSourceAndTarget(userID1, userID2,
            searchedUsers)) {  // path from user1 to user2 exists
            return searchedUsers;
        } else {
            searchedUsers = new ArrayList<>();  // clear users searched
            if (DFSSourceAndTarget(userID2, userID1,
                searchedUsers)) {  // path from user2 to user1 exists
                return searchedUsers;
            }
        }
        return null;  // no path exists between user1 and user2
    }

    /**
     * Performs depth first search on the DWInteractionGraph object
     * to check path from a source user to a target user, adding the users that were visited during
     * the search.
     *
     * @param sourceUserID the user ID for the first user
     * @param targetUserID the user ID for the second user
     * @param searched     a list containing the user IDs of the users that were searched, searched not
     *                     null and its element not null
     */
    private boolean DFSSourceAndTarget(int sourceUserID, int targetUserID, List<Integer> searched) {
        searched.add(sourceUserID);
        if (sourceUserID == targetUserID) { // user is the target user
            return true;
        }
        if (!senderIds.contains(sourceUserID)) {   // user does not send any emails
            return false;
        }

        // list of all the users that a user sends to that have not yet been searched, in ascending order of user IDs
        User user = users.get(findIndexOfUser(sourceUserID));
        List<Integer> receiversOfSource = new ArrayList<>();
        for (Edge edge : user.getEdges()) {
            if (!searched.contains(edge.getId())) {
                receiversOfSource.add(edge.getId());
            }
        }
        Collections.sort(receiversOfSource);    // always check smallest id of adjacent users

        for (int userID : receiversOfSource) {
            if (DFSSourceAndTarget(userID, targetUserID, searched)) {
                return true;
            }
        }
        return false;   // hits when for loop doesn't run - meaning receiversOfSource is empty (no unvisited users)
    }

    /* ------- Task 4 ------- */

    /**
     * Finds the maximum number of users that can be infected by a virus in N hours
     *
     * @param hours maximum number of hours
     * @return the maximum number of users that can be polluted in N hours
     */
    public int MaxBreachedUserCount(int hours) {
        int maxInfected = 0;
        for (User user : users) {
            //choose starting edge
            for (Edge edge : user.getEdges()) {
                for (int startTime : edge.getTimes()) {
                    int endTime = hours * 3600 + startTime;

                    Set<Integer> infected = new HashSet<>(List.of(user.getId()));
                    infectedUsers(user, startTime, endTime, infected);
                    int numInfections = infected.size();

                    if (numInfections > maxInfected) {
                        maxInfected = numInfections;
                    }
                }
            }
        }

        return maxInfected;
    }

    /**
     * Updates the number of people have been infected at a certain time and the maximum time the infection can be spread.
     *
     * @param parent      The current parent user that spreads the infection
     * @param currentTime current time of the email interactions
     * @param maxTime     the maximum time at which the infection can be spread
     * @param infected    a set of user IDs containing the current infected users in the graph, can be modified during the method call, not null
     */
    private void infectedUsers(User parent, int currentTime, int maxTime, Set<Integer> infected) {
        Set<Integer> infectedCopy = new HashSet<>(infected);

        for (Edge edge : parent.getEdges()) {
            if (infectedCopy.contains(edge.getId())) {
                // previously infected
                continue;
            }

            if (findIndexOfUser(edge.getId()) == -1) {
                // not yet infected, but dead end
                for (int time : edge.getTimes()) {
                    if (time >= currentTime && time <= maxTime) {
                        infectedCopy.add(edge.getId());
                    }
                }
                continue;
            }

            for (int time : edge.getTimes()) {
                if (time >= currentTime && time <= maxTime) {
                    // not yet infected, and can send to other users
                    infectedCopy.add(edge.getId()); //now infected
                    infectedUsers(users.get(findIndexOfUser(edge.getId())), time, maxTime,
                        infectedCopy);
                    // the number of infected users is all the users this parent infects
                }
            }
        }

        infected.addAll(infectedCopy);
    }

}
