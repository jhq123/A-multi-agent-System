package uk.ac.nott.cs.g53dia.multidemo;

public class VirtualPosition {

    private int x;
    private int y;

    public VirtualPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int distanceTo(VirtualPosition pp) {
        return Math.max(Math.abs(x - pp.getX()), Math.abs(y - pp.getY()));
    }
    @Override
    public String toString() {
        return x + "," + y;
    }
    @Override
    public boolean equals(Object o) {
        VirtualPosition pp = (VirtualPosition)o;
        if (pp==null) return false;
        return (pp.getX() == x) && (pp.getY() == y);
    }
    @Override
    public int hashCode() {
        return (((x & 0xff) << 16) + (y & 0xff));
    }
}
