import java.util.Stack;

/**
 * Classe Player - représente un joueur dans le jeu "le mystère des ruines Sheikah".
 * Un joueur possède un nom, une salle courante et un historique de ses déplacements.
 *
 * @author  Benoît de Keyn
 * @version 2025.12.25
 */
public class Player
{
    /** Le nom du joueur. */
    private final String        aName;
    
    /** La salle dans laquelle se trouve actuellement le joueur. */
    private Room          aCurrentRoom;
    
    /** L'historique des salles visitées pour la commande "retour". */
    private final Stack<Room>   aPreviousRooms;
    
    /** L'interface utilisateur graphique (pour certains affichages si nécessaire). */
    private UserInterface aGui;

    /**
     * Crée un nouveau joueur avec un nom donné.
     *
     * @param pName le nom du joueur
     */
    public Player( final String pName )
    {
        this.aName = pName;
        this.aPreviousRooms = new Stack<>();
    }

    /**
     * Renvoie le nom du joueur.
     *
     * @return le nom du joueur
     */
    public String getName()
    {
        return this.aName;
    }

    /**
     * Renvoie la salle dans laquelle se trouve actuellement le joueur.
     *
     * @return la salle courante du joueur
     */
    public Room getCurrentRoom()
    {
        return this.aCurrentRoom;
    }

    /**
     * Définit la salle courante du joueur.
     *
     * @param pRoom la nouvelle salle courante
     */
    public void setCurrentRoom( final Room pRoom )
    {
        this.aCurrentRoom = pRoom;
    }

    /**
     * Définit l'interface utilisateur graphique pour ce joueur.
     *
     * @param pGui l'interface utilisateur à utiliser
     */
    public void setGUI( final UserInterface pGui )
    {
        this.aGui = pGui;
    }

    /**
     * Déplace le joueur vers une nouvelle salle.
     * Mémorise la salle actuelle dans l'historique avant de changer de salle.
     *
     * @param pNextRoom la salle vers laquelle se déplacer
     */
    public void changeRoom( final Room pNextRoom )
    {
        this.aPreviousRooms.push( this.aCurrentRoom );
        this.aCurrentRoom = pNextRoom;
    }

    /**
     * Fait revenir le joueur à la salle précédente.
     * Retourne true si le retour a été effectué, false si l'historique est vide.
     *
     * @return true si le joueur a pu revenir en arrière, false sinon
     */
    public boolean goBack()
    {
        if ( this.aPreviousRooms.isEmpty() ) {
            return false;
        }
        this.aCurrentRoom = this.aPreviousRooms.pop();
        return true;
    }

} // Player
