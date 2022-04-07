package cpen221.mp2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User {
    private final int userId;
    private List<Edge> edges = new ArrayList<>();

    /*
     * Rep Invariant:
     * userId does not change
     * each element of edges is unique, which means it has a unique receiver ID
     */

    /*
     * Abstraction Function:
     * Each User instance represents a single node in a graph
     * userId represents the specific ID of this node
     * edges represents all the edges such that one of the endpoints are this node
     * each edge in edges represents an email interaction between two nodes
     */

    /**
     * Check that the rep invariant is true
     */
    private void checkRep() {
        Set<Integer> seenIds = new HashSet<>();
        for (Edge edge : edges) {
            assert edge != null;
            assert !seenIds.contains(edge.getId());
            seenIds.add(edge.getId());
        }
    }

    /**
     * A User object that holds a user ID. The User object can then hold edges from this user
     *
     * @param userId the id of the user
     */
    public User(int userId) {
        this.userId = userId;
    }

    public User(User user) {
        this.userId = user.userId;

        this.edges = new ArrayList<>();

        for (Edge edge : user.edges) {
            edges.add(new Edge(edge));
        }

        checkRep();
    }

    /**
     * Add an email interaction from @code{this} user to another
     *
     * @param edge the Edge instance representing the receiver of the email, and the time the email was sent
     */
    public void addEdge(Edge edge) {
        boolean added = false;

        for (Edge e : edges) {
            if (e.getId() == edge.getId()) {
                e.addTimes(edge.getTimes());
                added = true;
                break;
            }
        }

        if (!added) {
            edges.add(new Edge(edge));
        }
    }

    /**
     * Obtains a filtered user such that all of the edge on this user are within a time window
     *
     * @param startTime the start time of the time window to filter by
     * @param endTime   the end time of the time window to filter by
     * @return a filtered User, is not null
     */
    public User filterToTimeRange(int startTime, int endTime) {
        User userCopy = new User(this);

        List<Edge> edgesCopy = new ArrayList<>();

        for (Edge edge : userCopy.edges) {
            Edge edgeCopy = edge.filterToTimeRange(startTime, endTime);
            if (!edgeCopy.getTimes().isEmpty()) {
                edgesCopy.add(edgeCopy);
            }
        }

        userCopy.edges = edgesCopy;

        checkRep();
        return new User(userCopy);
    }

    /**
     * Each User instance contains a collection of Edges representing interaction from @{code this}
     * user to other uses. Each Edge in this collection holds the user on the other end of the
     * interaction, and the time of the interactino
     *
     * @return @code{List<Edge>} of Edge objects from this User instance
     */
    public List<Edge> getEdges() {
        ArrayList<Edge> edgesCopy = new ArrayList<>();

        for (Edge e : edges) {
            edgesCopy.add(new Edge(e));
        }

        return edgesCopy;
    }

    /**
     * Each User instance refers to a user with a specific user ID. Gets the ID of this user instance
     *
     * @return the user ID of this user instance
     */
    public int getId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return userId == user.userId &&
            edges.equals(user.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, edges);
    }
}