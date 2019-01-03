package uk.ac.nott.cs.g53dia.multidemo;

import javafx.util.Pair;
import uk.ac.nott.cs.g53dia.multilibrary.*;
import java.util.*;

public class DemoFleet extends Fleet {

    private static int FLEET_SIZE = 6;
    private MemoryMap map;
    private PlanManager pc;

    public DemoFleet() {
        this(new Random());
    }

    public DemoFleet(Random r) {
        map = new MemoryMap();
        pc = new PlanManager();
        for (int i = 0; i < FLEET_SIZE; i++) {
            DemoTanker tank = new DemoTanker(r, i, this);
            this.add(tank);
            map.addTanker(tank);
        }
    }

    public MemoryMap getMap() {
        return map;
    }

    public Pair<Action, Integer> getActionAndMove(int id, VirtualPosition vp) {
        pc.makePlan(map);
        return pc.senseAndMove(id, vp, map);
    }

}
