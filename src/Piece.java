public class Piece implements Comparable<Piece> { // x|y
    private int x;
    private int y;
    private boolean rotated;

    public Piece(int x, int y) {
        this.x = Math.min(x, y);
        this.y = Math.max(x, y);
        this.rotated = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Piece && o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return this.rotated ? this.y * 10 + this.x : this.x * 10 + this.y;
    }

    @Override
    public String toString() {
        return this.x + "|" + this.y;
    }

    public void rotate() {
        int temp = this.x;
        this.x = this.y;
        this.y = temp;
        this.rotated = !this.rotated;
    }

    public PieceRelation follows(Piece pLeft, Piece pRight) {
        if ((this.x == pLeft.x || this.y == pLeft.x) && (this.x == pRight.y || this.y == pRight.y)) return PieceRelation.FOLLOWS_FROM_BOTH;
        if (this.x == pLeft.x || this.y == pLeft.x) return PieceRelation.FOLLOWS_FROM_LEFT;
        if (this.x == pRight.y || this.y == pRight.y) return PieceRelation.FOLLOWS_FROM_RIGHT;

        return PieceRelation.DOESNT_FOLLOW;
    }

    public static Piece createFromHash(int hash) {
        return new Piece(hash / 10, hash % 10);
    }

    @Override
    public int compareTo(Piece o) {
        return Integer.compare(this.hashCode(), o.hashCode());
    }
}
