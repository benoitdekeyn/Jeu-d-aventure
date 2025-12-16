/**
 * Classe Game - la main loop du jeu "le mystere des ruines Sheikah".
 *
 * @author  Benoît de Keyn
 * @version 2025.12.25
 */
public class Game
{
    private UserInterface aGui;
    private GameEngine aEngine;

    /**
     * Crée le jeu et initialise sa carte interne. Crée l'interface et la lie au moteur du jeu.
     */
    public Game() 
    {
        this.aEngine = new GameEngine();
        this.aGui = new UserInterface( this.aEngine );
        this.aEngine.setGUI( this.aGui );
    }
}
