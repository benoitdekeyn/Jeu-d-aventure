/**
 * Classe Game - représente la boucle principale du jeu "le mystère des ruines Sheikah".
 * Cette classe initialise le moteur du jeu et l'interface utilisateur.
 *
 * @author  Benoît de Keyn
 * @version 2025.12.25
 */

public class Game
{
    /** L'interface utilisateur graphique du jeu. */
    private final UserInterface aGui;
    
    /** Le moteur de jeu qui gère la logique. */
    private final GameEngine aEngine;
    
    /** Le joueur du jeu. */
    private final Player aPlayer;

    /**
     * Crée et initialise le jeu.
     * Demande le nom du joueur, instancie le joueur, le moteur de jeu et l'interface utilisateur,
     * puis les lie ensemble.
     */
    public Game() 
    {
        this.aEngine = new GameEngine();
        this.aPlayer = new Player();
        this.aEngine.setPlayer( this.aPlayer );
        this.aGui = new UserInterface( this.aEngine );
        this.aEngine.setGUI( this.aGui );
    }
}
