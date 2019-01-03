package uk.ac.nott.cs.g53dia.multidemo;

import javafx.util.Pair;;
import uk.ac.nott.cs.g53dia.multilibrary.*;
import java.util.ArrayList;
import java.util.Random;

public class DemoTanker extends Tanker {
    private int ID;
    private DemoFleet fleet;
    private ArrayList<Bid> bids;
    private IMPbid bidding;
    private VirtualPosition VP;
    private Point prePoint;
    private int moveDirect;

    public DemoTanker(int id, DemoFleet fleet) {
        this(new Random(), id, fleet);
    }
    public DemoTanker(Random r, int id, DemoFleet fleet) {
        this.r = r;
        this.ID = id;
        this.fleet = fleet;
        VP = new VirtualPosition(0, 0);
        moveDirect = -1;
        bidding = new BidGenerator();
        bids = new ArrayList<>();
    }

    public Action senseAndAct(Cell[][] view, long timestep) {

        if(prePoint != null && !getPosition().equals(prePoint)) {
            updateMap(moveDirect);
        }
        fleet.getMap().drawMap(VP, view);
        makebids(fleet.getMap());
        prePoint = getPosition();
        Pair<Action, Integer> result = fleet.getActionAndMove(ID, VP);
        moveDirect = result.getValue();
        return result.getKey();
    }

    public int getId() {
        return ID;
    }

    public VirtualPosition getVp() {
        return VP;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    private void updateMap(int direction) {

        int x = VP.getX();
        int y = VP.getY();
        switch (direction) {
            case MoveAction.NORTH:
                y--;
                break;
            case MoveAction.SOUTH:
                y++;
                break;
            case MoveAction.EAST:
                x++;
                break;
            case MoveAction.WEST:
                x--;
                break;
            case MoveAction.NORTHEAST:
                x++;
                y--;
                break;
            case MoveAction.NORTHWEST:
                x--;
                y--;
                break;
            case MoveAction.SOUTHEAST:
                x++;
                y++;
                break;
            case MoveAction.SOUTHWEST:
                x--;
                y++;
                break;
        }
        VP.updatePosition(x, y);
    }

    private void makebids(MemoryMap map) {
        bids.clear();
        for(VirtualPosition taskVp : map.getTasks().keySet()) {
            Bid bid = new Bid(bidding.makeBid(taskVp, ID, fleet.getMap()));
            bids.add(bid);
        }
    }
}
