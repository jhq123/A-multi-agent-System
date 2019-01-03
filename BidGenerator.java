package uk.ac.nott.cs.g53dia.multidemo;

import uk.ac.nott.cs.g53dia.multilibrary.Point;
import uk.ac.nott.cs.g53dia.multilibrary.Tanker;
import java.util.Map;
import static uk.ac.nott.cs.g53dia.multidemo.Parameter.*;
import static uk.ac.nott.cs.g53dia.multidemo.Technique.searchNearestPosition;

public class BidGenerator implements IMPbid {

    @Override
    public Bid makeBid(VirtualPosition taskVp, int tankId, MemoryMap map) {

        if(map.getWells().size() == 0) {
            return new Bid(taskVp, Integer.MAX_VALUE);
        }
        Map<VirtualPosition, Point> wells = map.getWells();
        Map<VirtualPosition, Point> pumps = map.getPumps();
        MemoryTask task = map.getTasks().get(taskVp);
        DemoTanker tank = map.getTankers().get(tankId);
        VirtualPosition wellVp = searchNearestPosition(taskVp, wells.keySet());
        VirtualPosition taskPumpVp = searchNearestPosition(taskVp, pumps.keySet());
        VirtualPosition wellPumpVp = searchNearestPosition(wellVp, pumps.keySet());

        int cost = 0;
        int taskWaste = task.getWasteRemaining();
        int fuelLevel = tank.getFuelLevel();
        int wasteLevel = tank.getWasteLevel();

        TankerState state = TankerState.COLLECT;
        VirtualPosition tankVp = tank.getVp();
        VirtualPosition tempPump = null;
        VirtualPosition firProperTar = null;
        TankerState firProperState = null;

        while(state != TankerState.EXPLORE){
            if(tempPump != null) {
                state = TankerState.REFUEL;
            } else if (taskWaste == 0) {
                if(wasteLevel == 0) {
                    state = TankerState.EXPLORE;
                } else {
                    state = TankerState.DISPOSE;
                }
            } else {
                if(wasteLevel > THRESHOLD_WASTE) {
                    state = TankerState.DISPOSE;
                } else {
                    state = TankerState.COLLECT;
                }
            }
            switch (state) {
                case COLLECT:
                    int distTaskTank = taskVp.distanceTo(tankVp);
                    int distTaskPump = taskVp.distanceTo(taskPumpVp);
                    if(fuelLevel - FUEL_BACKUP >= distTaskTank + distTaskPump + 1) {
                        int wasteCapacity = Tanker.MAX_WASTE - wasteLevel;
                        taskWaste = wasteCapacity > taskWaste ? 0 : taskWaste - wasteCapacity;
                        fuelLevel -= distTaskTank;
                        cost += distTaskTank + 1;
                        tankVp = taskVp;
                        if(firProperTar == null) {
                            firProperTar = tankVp;
                            firProperState = state;
                        }
                    } else {
                        tempPump = taskPumpVp;
                    }
                    break;
                case DISPOSE:
                    int distWellTank = wellVp.distanceTo(tankVp);
                    int distWellPump = wellVp.distanceTo(wellPumpVp);
                    if(fuelLevel - FUEL_BACKUP >= distWellTank + distWellPump + 1) {
                        wasteLevel = 0;
                        fuelLevel -= distWellTank;
                        cost += distWellTank + 1;
                        tankVp = wellVp;
                        if(firProperTar == null) {
                            firProperTar = tankVp;
                            firProperState = state;
                        }
                    } else {
                        tempPump = wellPumpVp;
                    }
                    break;
                case REFUEL:
                    if(fuelLevel == Tanker.MAX_FUEL) {
                        return new Bid(tankVp, Integer.MAX_VALUE);
                    }
                    int distPumpTank = tempPump.distanceTo(tankVp);
                    if(fuelLevel - FUEL_BACKUP < distPumpTank) {
                        tempPump = searchNearestPosition(tankVp, pumps.keySet());
                        distPumpTank = tempPump.distanceTo(tankVp);
                        if(fuelLevel - FUEL_BACKUP < distPumpTank) {
                            return new Bid(tankVp, Integer.MAX_VALUE);
                        }
                    }
                    fuelLevel = Tanker.MAX_FUEL;
                    cost += distPumpTank + 1;
                    tankVp = tempPump;
                    tempPump = null;
                    if(firProperTar == null) {
                        firProperTar = tankVp;
                        firProperState = state;
                    }
                    break;
            }
        }
        cost += tank.getFuelLevel() - fuelLevel;
        return new Bid(taskVp, cost, firProperTar, firProperState);
    }
}
