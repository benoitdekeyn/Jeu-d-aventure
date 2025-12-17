/**
 * Classe Game - représente la boucle principale du jeu "le mystère des ruines Sheikah".
 * Cette classe initialise le moteur du jeu et l'interface utilisateur.
 *
 * @author  Benoît de Keyn
 * @version 2025.12.25
 */

public class Game
{
    private UserInterface aGui;
    private GameEngine aEngine;

    /**
     * Crée et initialise le jeu.
     * Instancie le moteur de jeu et l'interface utilisateur, puis les lie ensemble.
     */
    public Game() 
    {
        this.aEngine = new GameEngine();
        this.aGui = new UserInterface( this.aEngine );
        this.aEngine.setGUI( this.aGui );
    }
}
