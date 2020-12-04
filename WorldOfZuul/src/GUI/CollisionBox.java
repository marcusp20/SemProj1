package GUI;

public class CollisionBox {
    public double x, y, width, height;
    private boolean isSolid = true;

    public CollisionBox(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //System.out.println(this);
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
    }

    public boolean isSolid() {
        return isSolid;
    }

    @Override
    public String toString() {
        return "CollisionBox{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
