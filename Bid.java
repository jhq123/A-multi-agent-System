package uk.ac.nott.cs.g53dia.multidemo;

public class Bid {

    private VirtualPosition taskVP;
    private double cost;
    private VirtualPosition target;
    private TankerState state;

    public Bid(Bid bid) {
        this.taskVP = bid.taskVP;
        this.cost = bid.cost;
        this.target = bid.target;
        this.state = bid.state;
    }

    public Bid(VirtualPosition taskVP, double cost) {
        this.taskVP = taskVP;
        this.cost = cost;
        this.target = null;
        this.state = null;
    }

    public Bid(VirtualPosition taskVP, double cost, VirtualPosition target, TankerState state) {
        this.taskVP = taskVP;
        this.cost = cost;
        this.target = target;
        this.state = state;
    }

    public VirtualPosition getTaskVp() {
        return taskVP;
    }

    public double getCost() {
        return cost;
    }

    public VirtualPosition getTarget() {
        return target;
    }

    public TankerState getState() {
        return state;
    }
}
