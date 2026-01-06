import java.util.Stack;

/**
 * Classe Player - représente un joueur dans le jeu "le mystère des ruines Sheikah".
 * Un joueur possède un nom, une salle courante, un historique de ses déplacements et un inventaire.
 *
 * @author  Benoît de Keyn
 * @version 2025.12.25
 */
public class Player
{
    /** Le nom du joueur. */
    private String aName;
    
    /** La salle dans laquelle se trouve actuellement le joueur. */
    private Room aCurrentRoom;
    
    /** L'historique des salles visitées pour la commande "retour". */
    private Stack<Room> aPreviousRooms;

    /** L'inventaire du joueur contenant les objets portés. */
    private ItemList aInventaire;

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
        this.aInventaire = new ItemList();
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

    /**
     * Ajoute un objet à l'inventaire du joueur.
     *
     * @param pItem l'objet à ajouter
     */
    public void addItem( final Item pItem )
    {
        this.aInventaire.add(pItem);
    } // addItem

    /**
     * Retire un objet de l'inventaire du joueur.
     *
     * @param pItemName le nom de l'objet à retirer
     */
    public void removeItem( final String pItemName )
    {
        this.aInventaire.remove( pItemName );
    } // removeItem

    /**
     * Renvoie un objet porté par le joueur.
     *
     * @param pItemName le nom de l'objet recherché
     * @return l'objet correspondant, ou null si non trouvé
     */
    public Item getItem( final String pItemName )
    {
        return this.aInventaire.get( pItemName );
    } // getItem

    /**
     * Vérifie si le joueur porte un objet spécifique.
     *
     * @param pItemName le nom de l'objet recherché
     * @return true si le joueur porte cet objet, false sinon
     */
    public boolean hasItem( final String pItemName )
    {
        return this.aInventaire.has( pItemName );
    } // hasItem

    /**
     * Renvoie une chaîne de caractères listant les objets portés par le joueur.
     *
     * @return une description des objets portés
     */
    public String getItemsString()
    {
        return this.aInventaire.getItemString();
    } // getItemsString

} // Player
