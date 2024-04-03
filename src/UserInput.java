import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public final class UserInput {
    private static final Scanner sc = new Scanner(System.in);

    public static int amountOfPieces() {
        while (true) {
            try {
                System.out.println("Zadejte pocet dilku.");
                int x = sc.nextInt(); sc.nextLine();
                if (x <= 0) {
                    System.out.println("Pocet dilku musi byt kladny.");
                    continue;
                }
                return x;
            } catch (InputMismatchException e) {
                System.out.println("Pocet dilku musi byt kladne cislo.");
            }
        }
    }

    public static int piece(ArrayList<Piece> pieces) {
        while (true) {
            System.out.println("Jaky dilek chcete polozit?");

            try {
                int x = sc.nextInt(); sc.nextLine();
                if (pieces.contains(Piece.createFromHash(x))) {
                    return x;
                }
            } catch (InputMismatchException e) {
                System.out.println("Nerozumim.");
            }
            System.out.println("Tento dilek nemate.");
        }
    }

    private static boolean trueFalse(String question) {
        while (true) {
            System.out.println(question);
            String s = sc.next(); sc.nextLine();

            switch (s) {
                case "1" -> {
                    return true;
                }
                case "0" -> {
                    return false;
                }
                default -> System.out.println("Nerozumim.");
            }
        }
    }

    public static boolean toRight() {
        return trueFalse("Vlevo [0] nebo vpravo [1]?");
    }

    public static boolean shouldSort() {
        return trueFalse("Chcete dilky seradit?\nAno: [1]\nNe: [0]");
    }

    public static void closeScanner() {
        sc.close();
    }
}
