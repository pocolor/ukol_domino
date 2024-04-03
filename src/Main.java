import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Dilky zadavejte pouze cisly.\n2|5 -> 25");

        final Random rd = new Random();
        final int amountOfPieces = UserInput.amountOfPieces();
        final ArrayList<Piece> pieces = new ArrayList<>();

        for (int i = 0; i < amountOfPieces; i++) {
            pieces.add(new Piece(rd.nextInt(1, 7), rd.nextInt(1, 7)));
        }

        if (UserInput.shouldSort()) pieces.sort(null);

        final Board board = new Board(pieces);
        board.play();

        UserInput.closeScanner();
    }
}