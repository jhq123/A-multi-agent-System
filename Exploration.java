package uk.ac.nott.cs.g53dia.multidemo;

import javafx.util.Pair;
import uk.ac.nott.cs.g53dia.multilibrary.Tanker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import static uk.ac.nott.cs.g53dia.multidemo.Parameter.*;
import static uk.ac.nott.cs.g53dia.multidemo.Technique.searchNearestPosition;

public class Exploration implements IMPplan {
    private ArrayList<VirtualPosition> exploreRoute;
    private ArrayList<VirtualPosition> starting;

    public Exploration() {
        starting = new ArrayList<>();
        exploreRoute = new ArrayList<>();
        exploreRoute.add(new VirtualPosition(0, 0));
        exploreRoute.add(new VirtualPosition(0, 20));
        exploreRoute.add(new VirtualPosition(20, 20));
        exploreRoute.add(new VirtualPosition(20, 40));
        exploreRoute.add(new VirtualPosition(0, 40));
    }

    private void addNewstarting(VirtualPosition vp) {
        if(!starting.contains(vp)) {
            starting.add(vp);
            exploreRoute.add(new VirtualPosition(vp.getX() - 40, vp.getY() - 40));
            exploreRoute.add(new VirtualPosition(vp.getX() + 40, vp.getY() - 40));
            exploreRoute.add(new VirtualPosition(vp.getX() + 40, vp.getY() + 40));
            exploreRoute.add(new VirtualPosition(vp.getX() - 40, vp.getY() + 40));
        }
    }

    @Override
    public Plan makePlan(MemoryMap map, Plan previousPlan) {
        for(VirtualPosition pumpVp : map.getPumps().keySet()) {
            addNewstarting(pumpVp);
        }
        ArrayList<VirtualPosition> exploredPoint = new ArrayList<>();
        for(int tankId : map.getTankers().keySet()) {
            if(previousPlan.targets.get(tankId) != null) {
                exploredPoint.add(previousPlan.targets.get(tankId).getKey());
            }
        }
        Iterator<Integer> iter = map.getTankers().keySet().iterator();
        while (iter.hasNext()) {
            int tankId = iter.next();
            DemoTanker tank = map.getTankers().get(tankId);
            VirtualPosition tankVp = tank.getVp();

            if(previousPlan.targets.get(tankId) != null &&
                    previousPlan.targets.get(tankId).getValue() == TankerState.REFUEL &&
                    tank.getFuelLevel() != Tanker.MAX_FUEL) {
                continue;
            }

            if(previousPlan.targets.get(tankId) == null || !exploreRoute.contains(previousPlan.targets.get(tankId).getKey())) {
                VirtualPosition target = searchNearestPosition(tank.getVp(), new HashSet<>(exploreRoute));
                if(target.equals(tankVp)) {
                    target = exploreRoute.get((exploreRoute.indexOf(target) + 1) % exploreRoute.size());
                }
                while(exploredPoint.contains(target)) {
                    target = exploreRoute.get((exploreRoute.indexOf(target) + exploreRoute.size() - 2) % exploreRoute.size());
                }

                VirtualPosition pump = searchNearestPosition(target, map.getPumps().keySet());
                int distPathTank = target.distanceTo(tankVp);
                int distPathPump = target.distanceTo(pump);
                if(tank.getFuelLevel() - FUEL_BACKUP >= distPathPump + distPathTank) {
                    exploredPoint.add(target);
                    previousPlan.targets.put(tankId, new Pair<>(target, TankerState.EXPLORE));

                } else {
                    if(tank.getFuelLevel() - FUEL_BACKUP < tankVp.distanceTo(pump)) {
                        pump = searchNearestPosition(tankVp, map.getPumps().keySet());
                    }
                    previousPlan.targets.put(tankId, new Pair<>(pump, TankerState.REFUEL));
                }
            } else {
                VirtualPosition target = previousPlan.targets.get(tankId).getKey();
                if(target.equals(tankVp)) {
                    target = exploreRoute.get((exploreRoute.indexOf(target) + 1) % exploreRoute.size());
                    while(exploredPoint.contains(target)) {
                        target = exploreRoute.get((exploreRoute.indexOf(target) + exploreRoute.size() - 2) % exploreRoute.size());
                    }
                    VirtualPosition pump = searchNearestPosition(target, map.getPumps().keySet());
                    int distPathTank = target.distanceTo(tankVp);
                    int distPathPump = target.distanceTo(pump);
                    if(tank.getFuelLevel() - FUEL_BACKUP >= distPathPump + distPathTank) {
                        exploredPoint.add(target);
                        previousPlan.targets.put(tankId, new Pair<>(target, TankerState.EXPLORE));
                    } else {
                        if(tank.getFuelLevel() - FUEL_BACKUP < tankVp.distanceTo(pump)) {
                            pump = searchNearestPosition(tankVp, map.getPumps().keySet());
                        }
                        previousPlan.targets.put(tankId, new Pair<>(pump, TankerState.REFUEL));
                    }
                }
            }
            iter.remove();
        }
        return previousPlan;
    }

}
