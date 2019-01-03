package uk.ac.nott.cs.g53dia.multidemo;

import uk.ac.nott.cs.g53dia.multilibrary.Task;

public class MemoryTask {
    private Task realTask;
    private int remain;

    public MemoryTask(Task realTask) {
        this.realTask = realTask;
        this.remain = realTask.getWasteRemaining();
    }
    @Override
    protected Object clone() {
        return new MemoryTask(realTask);
    }
    public Task getRealTask() { return realTask; }
    public int getWasteRemaining() { return remain; }
}
