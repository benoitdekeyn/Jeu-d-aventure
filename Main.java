/*
 * Démarre automatiquement une nouvelle partie du Jeu d'aventure.
 */
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main
{
    public static void main(String[] args)
    {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Erreur d'encodage UTF-8");
        }
        Game jeu = new Game();
        jeu.play();
    }
}