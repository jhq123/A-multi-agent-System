package uk.ac.nott.cs.g53dia.multidemo;

import javafx.util.Pair;
import uk.ac.nott.cs.g53dia.multilibrary.Tanker;
import java.util.*;

public class GreedySearch implements IMPplan {

    @Override
    public Plan makePlan(MemoryMap map, Plan PreviousPlan) {

        if(map.getWells().size() == 0) {
            return PreviousPlan;
        }
        Iterator<Integer> tankerIt = map.getTankers().keySet().iterator();
        while(tankerIt.hasNext()) {
            int tankId = tankerIt.next();
            if(PreviousPlan.targets.get(tankId) != null &&
                    ((PreviousPlan.targets.get(tankId).getValue() == TankerState.DISPOSE &&
                            map.getTankers().get(tankId).getWasteLevel() != 0) ||
                            (PreviousPlan.targets.get(tankId).getValue() == TankerState.REFUEL &&
                            map.getTankers().get(tankId).getFuelLevel() != Tanker.MAX_FUEL))) {
                tankerIt.remove();
                continue;
            }
            Bid properBid = null;
            double cost = Integer.MAX_VALUE;

            ArrayList<Bid> bids = map.getTankers().get(tankId).getBids();
            if(bids == null) {
                continue;
            }
            for(Bid bid : bids) {
                if(map.getTasks().containsKey(bid.getTaskVp()) &&
                        map.getTasks().get(bid.getTaskVp()).getWasteRemaining() != 0 &&
                        bid.getCost() < cost) {
                    properBid = bid;
                    cost = bid.getCost();
                }
            }
            if(properBid != null) {
                map.getTasks().remove(properBid.getTaskVp());
                tankerIt.remove();
               PreviousPlan.targets.put(tankId, new Pair<>(properBid.getTarget(), properBid.getState()));
            }
        }
        return PreviousPlan;
    }
}
