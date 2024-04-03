import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Board {
    private final LinkedList<Piece> line;
    private final ArrayList<Piece> pieces;
    private final int numberOfPieces;
    private boolean gameEnded;

    public Board(ArrayList<Piece> pieces) {
        this.line = new LinkedList<>();
        this.pieces = pieces;
        this.numberOfPieces = this.pieces.size();
        this.gameEnded = false;
    }

    public void play() {
        try {
            this.placePiece(1);
        } catch (CannotPlacePieceException ignored) {
        }

        int pieceNum = 2;

        while (!this.pieces.isEmpty() && !this.gameEnded) {
            try {
                this.placePiece(pieceNum);
                pieceNum++;
            } catch (CannotPlacePieceException e) {
                System.out.println("Tento dilek nejde polozit.");
            }
        }

        pieceNum -= 2;  // protoze:
        // 1. je to cislo nasledujiciho dilku
        // 2. v metode placePiece, pokud uz nejde dilek polozit, metoda konci, ale dane kolo cyclu ne

        if (!this.pieces.isEmpty()) {
            System.out.printf("Prohrali jste. Zvladli jste umistit %d z %d dilku.\n", pieceNum, this.numberOfPieces);
            return;
        }

        System.out.printf("Vyhrali jste. Zvladli jste umistit vsech %d dilku.\n", this.numberOfPieces);
    }

    private void placePiece(int pieceNum) throws CannotPlacePieceException {
        if (this.line.isEmpty()) {
            Piece piece = this.getPiece(pieceNum);
            this.line.add(piece);
            this.pieces.remove(piece);
            return;
        }

        if (!this.canPlacePiece()) {
            this.gameEnded = true;
            return;
        }

        Piece piece = this.getPiece(pieceNum);

        switch (this.howCanPlacePiece(piece)) {
            case FOLLOWS_FROM_BOTH -> {
                boolean toRight = UserInput.toRight();
                if (this.shouldRotatePiece(piece, toRight)) piece.rotate();

                if (toRight) {
                    this.line.addLast(piece);
                } else {
                    this.line.addFirst(piece);
                }
                this.pieces.remove(piece);
            }
            case FOLLOWS_FROM_LEFT -> {
                if (this.shouldRotatePiece(piece, false)) piece.rotate();
                this.line.addFirst(piece);
                this.pieces.remove(piece);
            }
            case FOLLOWS_FROM_RIGHT -> {
                if (this.shouldRotatePiece(piece, true)) piece.rotate();
                this.line.addLast(piece);
                this.pieces.remove(piece);
            }
            case DOESNT_FOLLOW -> throw new CannotPlacePieceException();
        }
    }

    private Piece getPiece(int pieceNum) {
        this.printBoard();
        System.out.println("Polozte " + pieceNum + ". dilek.");
        return Piece.createFromHash(UserInput.piece(this.pieces));
    }

    private PieceRelation howCanPlacePiece(Piece piece) {
        return piece.follows(this.line.getFirst(), this.line.getLast());
    }

    private boolean shouldRotatePiece(Piece piece, boolean fromRight) {
        if (fromRight) return this.line.getLast().getY() != piece.getX();
        return this.line.getFirst().getX() != piece.getY();
    }

    private boolean canPlacePiece() {
        HashSet<Integer> numbers = new HashSet<>();
        for (Piece piece : this.pieces) {
            numbers.add(piece.getX());
            numbers.add(piece.getY());
        }

        return numbers.contains(this.line.getFirst().getX()) || numbers.contains(this.line.getLast().getY());
    }

    private void printBoard() {
        final int lines = (this.pieces.size() >> 3) + Math.min(this.pieces.size() % 8, 1);

        StringBuilder sb = new StringBuilder();
        sb.append('╭').append("─".repeat(40)).append('╮').append("\n");  // 1

        Iterator<Piece> pieceIterator = this.pieces.iterator();
        for (int i = 0; i < lines; i++) {
            sb.append("│ ");
            for (int j = 0; j < 8; j++) sb.append(pieceIterator.hasNext() ? pieceIterator.next() + "  " : " ".repeat(5));
            sb.delete(sb.length() - 2, sb.length())
                    .append(" │\n");
        }  // 2
        
        sb.append('├').append("─".repeat(40)).append('┤').append("\n");  // 3

        sb.append("│ ");
        if (this.line.isEmpty()) sb.append(" ".repeat(38));
        else if (this.line.size() <= 8) {
            for (Piece piece : this.line) sb.append(piece).append("  ");
            if (this.line.size() != 8) sb.append(" ".repeat(38 - this.line.size() * 5));
            else sb.delete(sb.length() - 2, sb.length());
        } else {
            sb.append(this.line.getFirst()).append("  ")
                    .append(this.line.get(1)).append("  ")
                    .append(this.line.get(2)).append("  ")
                    .append(".".repeat(8)).append("  ")
                    .append(this.line.get(this.line.size() - 3)).append("  ")
                    .append(this.line.get(this.line.size() - 2)).append("  ")
                    .append(this.line.getLast());
        }
        sb.append(" │\n");  // 4

        sb.append('╰').append("─".repeat(40)).append('╯');  // 5

        System.out.println(sb);
    }
}
