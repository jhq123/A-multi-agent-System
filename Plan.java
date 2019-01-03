package uk.ac.nott.cs.g53dia.multidemo;

import javafx.util.Pair;
import uk.ac.nott.cs.g53dia.multidemo.TankerState;
import uk.ac.nott.cs.g53dia.multidemo.VirtualPosition;
import java.util.HashMap;
import java.util.Map;

public class Plan {
    protected Map<Integer, Pair<VirtualPosition, TankerState>> targets;
    protected double cost;
    public Plan() {
        targets = new HashMap<>();
        cost = 0;
    }
    public Map<Integer, Pair<VirtualPosition, TankerState>> getTargets() {
        return targets;
    }
}
