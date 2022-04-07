package cpen221.mp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Edge {
    private final int receiverId;
    private List<Integer> times = new ArrayList<>();

    /*
     * Rep Invariant:
     * receiverId does not change
     * times is not null
     * elements of time cannot be null
     */

    /*
     * Abstraction Function:
     * each Edge instance represents an email interaction from a graph
     * receiverId represents the user who received this email
     * times represents all the different times at which this interaction occurred
     */

    /**
     * Check that the rep invariant is true
     */
    private void checkRep() {
        assert times != null;

        for (Integer time : times) {
            assert time != null;
        }
    }

    /**
     * Given a user that received an email and a time then it was sent, a new edge is created
     *
     * @param userId    the id of the user that received an email
     * @param emailTime time when that email was received
     */
    public Edge(int userId, int emailTime) {
        this.receiverId = userId;
        addTime(emailTime);
        checkRep();
    }

    /**
     * Copy constructor for Edge class
     * Creates a new Edge instance from another Edge instance
     *
     * @param edge the Edge instance to copy
     */
    public Edge(Edge edge) {
        this.receiverId = edge.receiverId;
        times.addAll(new ArrayList<>(edge.times));
        Collections.sort(times);
    }

    /**
     * Adds a NEW time user id received an email form another certain user.
     *
     * @param time (not negative) time when the email was sent
     */
    public void addTime(int time) {
        times.add(time);
        Collections.sort(times);
        checkRep();
    }

    /**
     * Adds a collection of times to a given edge, representing all the times at which an email
     * was sent to the receiver of this edge on this edge
     *
     * @param times list of times to add to this edge
     */
    public void addTimes(List<Integer> times) {
        this.times.addAll(new ArrayList<>(times));
        Collections.sort(this.times);
        checkRep();
    }

    /**
     * provides with the user id that has received an email
     *
     * @return the user identifier
     */
    public int getId() {
        return receiverId;
    }

    /**
     * Provides with the weight of the connection
     *
     * @return integer number of times an email was sent to user id from another certain user
     */
    public int getWeight() {
        return times.size();
    }

    public Edge filterToTimeRange(int startTime, int endTime) {
        Edge edgeCopy = new Edge(this);

        Collections.sort(edgeCopy.times);

        List<Integer> timesCopy = new ArrayList<>(edgeCopy.times);

        for (Integer time : edgeCopy.times) {
            if (time < startTime || time > endTime) {
                timesCopy.remove(time);
            }
        }

        edgeCopy.times = timesCopy;
        Collections.sort(edgeCopy.times);

        return new Edge(edgeCopy);
    }

    /**
     * Obtains all times at which an email was sent on this edge to the user ID on this edge
     *
     * @return sorted list of times on this edge
     */
    public List<Integer> getTimes() {
        Collections.sort(times);
        return new ArrayList<>(times);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return receiverId == edge.receiverId &&
            times.equals(edge.times);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiverId, times);
    }
}
