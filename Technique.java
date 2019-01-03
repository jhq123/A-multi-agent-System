package uk.ac.nott.cs.g53dia.multidemo;

import java.util.Set;

public class Technique {
    public static VirtualPosition searchNearestPosition(VirtualPosition currentPosition, Set<VirtualPosition> set) {
        VirtualPosition nearestPosition = null;
        int minDistance = Integer.MAX_VALUE;
        for(VirtualPosition vp : set) {
            int tempDistance = currentPosition.distanceTo(vp);
            if(tempDistance < minDistance) {
                minDistance = tempDistance;
                nearestPosition = vp;
            }
        }
        return nearestPosition;
    }
}
