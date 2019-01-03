package uk.ac.nott.cs.g53dia.multidemo;

import javafx.util.Pair;
import uk.ac.nott.cs.g53dia.multidemo.IMPplan;
import uk.ac.nott.cs.g53dia.multidemo.Exploration;
import uk.ac.nott.cs.g53dia.multidemo.GreedySearch;
import uk.ac.nott.cs.g53dia.multidemo.Plan;
import uk.ac.nott.cs.g53dia.multilibrary.*;

public class PlanManager {

    Plan plan;

    public PlanManager() {
        plan = new Plan();
    }

    public void makePlan(MemoryMap map) {
        MemoryMap clonedMap = new MemoryMap(map);

        IMPplan planMaker = new GreedySearch();
        plan = planMaker.makePlan(clonedMap, plan);
        planMaker = new Exploration();
        plan = planMaker.makePlan(clonedMap, plan);
    }

    public Pair<Action, Integer> senseAndMove(int id, VirtualPosition vp, MemoryMap map) {
        Pair<VirtualPosition, TankerState> p = plan.getTargets().get(id);
        VirtualPosition target = p.getKey();
        TankerState state = p.getValue();
        Action action = null;
        int lastMove = -1;

        if(vp.equals(target)) {
                switch (state) {
                case EXPLORE:
                    lastMove = moveTowards(vp, target);
                    action = new MoveAction(lastMove);
                    break;
                case COLLECT:
                    Task realTask = map.getTasks().get(vp).getRealTask();
                    action = new LoadWasteAction(realTask);
                    break;
                case DISPOSE:
                    action = new DisposeWasteAction();
                    break;
                case REFUEL:
                    action = new RefuelAction();
                    break;
            }
        } else {
            lastMove = moveTowards(vp, target);
            action = new MoveAction(lastMove);
        }
        return new Pair<>(action, lastMove);
    }
    private int moveTowards(VirtualPosition from, VirtualPosition to) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        if (dx < 0) {
            if(dy < 0) {
                return MoveAction.NORTHWEST;
            } else if(dy > 0) {
                return MoveAction.SOUTHWEST;
            } else {
                return MoveAction.WEST;
            }
        } else if (dx > 0) {
            if(dy < 0) {
                return MoveAction.NORTHEAST;
            } else if (dy > 0) {
                return MoveAction.SOUTHEAST;
            } else {
                return MoveAction.EAST;
            }
        } else {
            if(dy < 0) {
                return MoveAction.NORTH;
            } else if (dy > 0) {
                return MoveAction.SOUTH;
            }
        }
        return -1;
    }
}
