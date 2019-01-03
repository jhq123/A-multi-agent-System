package uk.ac.nott.cs.g53dia.multidemo;

import uk.ac.nott.cs.g53dia.multilibrary.*;
import java.util.HashMap;
import java.util.Map;
import static uk.ac.nott.cs.g53dia.multilibrary.Tanker.VIEW_RANGE;

public class MemoryMap {
    private Map<Integer, DemoTanker> tankers;
    private Map<VirtualPosition, MemoryTask> tasks;
    private Map<VirtualPosition, Point> wells;
    private Map<VirtualPosition, Point> pumps;

    public MemoryMap() {
        tasks = new HashMap<>();
        tankers = new HashMap<>();
        pumps = new HashMap<>();
        wells = new HashMap<>();
    }

    public MemoryMap(MemoryMap map) {
        tankers = new HashMap<>(map.tankers);
        tasks = new HashMap<>();
        for(Map.Entry<VirtualPosition, MemoryTask> entry: map.tasks.entrySet()) {
            tasks.put(entry.getKey(), new MemoryTask(entry.getValue().getRealTask()));
        }
        wells = new HashMap<>(map.wells);
        pumps = new HashMap<>(map.pumps);
    }

    public Map<Integer, DemoTanker> getTankers() { return tankers; }
    public Map<VirtualPosition, MemoryTask> getTasks() { return tasks; }
    public Map<VirtualPosition, Point> getWells() { return wells; }
    public Map<VirtualPosition, Point> getPumps() { return pumps; }

    public void addTanker(DemoTanker tanker) {
        tankers.put(tanker.getId(), tanker);
    }

    public void drawMap(VirtualPosition vp, Cell[][] view) {
        int x = vp.getX();
        int y = vp.getY();
        for(int i = 0; i < VIEW_RANGE * 2 + 1; i++) {
            for(int j = 0; j < VIEW_RANGE * 2 + 1; j++) {
                Cell cell = view[i][j];
                if(cell instanceof Station) {
                    VirtualPosition pp = new VirtualPosition(x + i - VIEW_RANGE, y + j - VIEW_RANGE);
                    if(((Station) cell).getTask() != null) {
                        tasks.put(pp, new MemoryTask(((Station) view[i][j]).getTask()));
                    } else {
                        tasks.remove(pp);
                    }
                }
                else if(cell instanceof Well) {
                    VirtualPosition pp = new VirtualPosition(x + i - VIEW_RANGE, y + j - VIEW_RANGE);
                    if(!wells.containsKey(pp)) {
                        wells.put(pp, cell.getPoint());
                    }
                }
                else if(cell instanceof FuelPump) {
                    VirtualPosition pp = new VirtualPosition(x + i - VIEW_RANGE, y + j - VIEW_RANGE);
                    if(!pumps.containsKey(pp)) {
                        pumps.put(pp, cell.getPoint());
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Task: " + tasks.size() + "\nWell: " + wells.size() + "\nFuel pump: " + pumps.size();
    }
}
