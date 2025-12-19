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
    private final String aName;
    
    /** La salle dans laquelle se trouve actuellement le joueur. */
    private Room aCurrentRoom;
    
    /** L'historique des salles visitées pour la commande "retour". */
    private final Stack<Room> aPreviousRooms;

    /**
     * Crée un nouveau joueur en demandant son nom via une boîte de dialogue.
     * Si aucun nom n'est fourni, le nom par défaut est utilisé.
     */
    public Player()
    {
        String vPlayerName = javax.swing.JOptionPane.showInputDialog( "Quel est votre prénom ?" );
        if ( vPlayerName == null || vPlayerName.trim().isEmpty() ) {
            this.aName = "Link";
        } else {
            this.aName = vPlayerName;
        }
        this.aPreviousRooms = new Stack<>();
    } // Player

    /**
     * Renvoie le nom du joueur.
     *
     * @return le nom du joueur
     */
    public String getName()
    {
        return this.aName;
    } // getName

    /**
     * Renvoie la salle dans laquelle se trouve actuellement le joueur.
     *
     * @return la salle courante du joueur
     */
    public Room getCurrentRoom()
    {
        return this.aCurrentRoom;
    } // getCurrentRoom

    /**
     * Définit la salle courante du joueur.
     *
     * @param pRoom la nouvelle salle courante
     */
    public void setCurrentRoom( final Room pRoom )
    {
        this.aCurrentRoom = pRoom;
    } // setCurrentRoom

    /**
     * Déplace le joueur vers une nouvelle salle.
     * Mémorise la salle actuelle dans l'historique avant de changer de salle.
     *
     * @param pNextRoom la salle vers laquelle se déplacer
     */
    public void goRoom( final Room pNextRoom )
    {
        this.aPreviousRooms.push( this.aCurrentRoom );
        this.aCurrentRoom = pNextRoom;
    } // goRoom

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
    } // goBack

} // Player
