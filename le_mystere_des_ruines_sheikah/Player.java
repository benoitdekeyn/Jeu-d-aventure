import java.util.Stack;

/**
 * Classe Player - représente un joueur dans le jeu "le mystère des ruines Sheikah".
 * Un joueur possède un nom, une salle courante, un historique de ses déplacements et un inventaire.
 *
 * @author  Benoît de Keyn
 * @version 2026.01.10
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
    private ItemList aInventory;

    /** Le poids maximum que le joueur peut porter en kilogrammes. */
    private double aInventoryCapacity;

    /** Le poids total actuel des objets portés par le joueur en kilogrammes. */
    private double aInventoryWeight;

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
        this.aInventory = new ItemList();
        this.aInventoryCapacity = 10.0;
        this.aInventoryWeight = 0.0;
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
     * Le poids de l'objet est automatiquement ajouté au poids total porté.
     *
     * @param pItem l'objet à ajouter à l'inventaire
     */
    public void addItem( final Item pItem )
    {
        this.aInventoryWeight += pItem.getWeight();
        this.aInventory.add( pItem );
    } // addItem

    /**
     * Retire un objet de l'inventaire du joueur.
     * Si l'objet n'existe pas dans l'inventaire, aucune action n'est effectuée.
     *
     * @param pItemName le nom de l'objet à retirer de l'inventaire
     */
    public void removeItem( final String pItemName )
    {
        this.aInventoryWeight -= this.aInventory.get( pItemName ).getWeight();
        this.aInventory.remove( pItemName );
    } // removeItem

    /**
     * Renvoie un objet de l'inventaire du joueur.
     *
     * @param pItemName le nom de l'objet recherché dans l'inventaire
     * @return l'objet correspondant, ou null si l'objet n'est pas dans l'inventaire
     */
    public Item getItem( final String pItemName )
    {
        return this.aInventory.get( pItemName );
    } // getItem

    /**
     * Vérifie si le joueur possède un objet spécifique dans son inventaire.
     *
     * @param pItemName le nom de l'objet recherché dans l'inventaire
     * @return true si l'objet est présent dans l'inventaire, false sinon
     */
    public boolean hasItem( final String pItemName )
    {
        return this.aInventory.has( pItemName );
    } // hasItem

    /**
     * Renvoie une chaîne de caractères listant tous les objets de l'inventaire du joueur.
     *
     * @return une description textuelle des objets présents dans l'inventaire
     */
    public String getInventoryContents()
    {
        return this.aInventory.getItemsString();
    } // getInventoryContents

    /**
     * Renvoie le poids total des objets portés par le joueur.
     *
     * @return le poids total en kilogrammes
     */    public double getInventoryWeight() 
    {
        return this.aInventoryWeight;
    } // getInventoryWeight

    /**
     * Renvoie la capacité maximale de l'inventaire du joueur.
     *
     * @return la capacité maximale en kilogrammes
     */
    public double getInventoryCapacity() 
    {
        return this.aInventoryCapacity;
    } // getInventoryCapacity

    /**
     * Double la capacité maximale de l'inventaire du joueur.
     */
    public void doubleInventoryCapacity() 
    {
        this.aInventoryCapacity *= 2;
    } // doubleInventoryCapacity

    /**
     * Vide l'historique des salles visitées.
     * Cette méthode est appelée lorsqu'un déplacement irréversible est effectué (Trap Door).
     */
    public void clearHistory()
    {
        this.aPreviousRooms.clear();
    } // clearHistory

    /**
     * Tente de déverrouiller une porte chaque Item de l'inventaire du joueur jusqu'à réussir.
     * 
     * @param pDoor la porte à déverrouiller
     * @return true si la porte a été déverrouillée, false sinon
     */
    public boolean tryUnlockDoor( final Door pDoor )
    {
        for ( Item vItem : this.aInventory.getAllItems() ) {
            if ( pDoor.unlock( vItem ) ) {
                return true;
            }
        }
        return false;
    } // tryUnlockDoor(*)

    /**
     * Tente de verrouiller une porte chaque Item de l'inventaire du joueur jusqu'à réussir.
     * 
     * @param pDoor la porte à verrouiller
     * @return true si la porte a été verrouillée, false sinon
     */
    public boolean tryLockDoor( final Door pDoor )
    {
        for ( Item vItem : this.aInventory.getAllItems() ) {
            if ( pDoor.lock( vItem ) ) {
                return true;
            }
        }
        return false;
    } // tryLockDoor(*)
} // Player
