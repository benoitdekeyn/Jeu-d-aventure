import java.util.HashMap;

/**
 * Classe ItemList - gère une collection d'objets (Item) dans le jeu "le mystère des ruines Sheikah".
 * Permet d'ajouter, retirer, rechercher et lister des objets.
 *
 * @author  Benoît de Keyn
 * @version 2026.01.10
 */
public class ItemList
{
    /** La collection des objets indexés par leur nom. */
    private HashMap<String, Item> aItems;

    /**
     * Crée une nouvelle liste d'objets vide.
     */
    public ItemList(){
        this.aItems = new HashMap<>();
    } // ItemList

    /**
     * Ajoute un objet à la liste.
     *
     * @param pItem l'objet à ajouter
     */
    public void add( final Item pItem )
    {
        this.aItems.put( pItem.getName(), pItem );
    } // add

    /**
     * Retire un objet de la liste.
     *
     * @param pItemName le nom de l'objet à retirer
     */
    public void remove( final String pItemName )
    {
        this.aItems.remove( pItemName );
    } // remove

    /**
     * Renvoie un objet de la liste.
     *
     * @param pItemName le nom de l'objet recherché
     * @return l'objet correspondant, ou null si non trouvé
     */
    public Item get( final String pItemName )
    {
        return this.aItems.get( pItemName );
    } // get

    /**
     * Vérifie si un objet spécifique est présent dans la liste.
     *
     * @param pItemName le nom de l'objet recherché
     * @return true si l'objet est présent, false sinon
     */
    public boolean has( final String pItemName )
    {
        return this.aItems.containsKey( pItemName );
    } // has

    /**
     * Vérifie si la liste d'objets est vide.
     *
     * @return true si la liste est vide, false sinon
     */
    public boolean isEmpty()
    {
        return this.aItems.isEmpty();
    } // isEmpty

    /**
     * Construit une chaîne de caractères décrivant les objets présents dans la liste.
     *
     * @return une description des objets ou un message indiquant l'absence d'objets
     */
    public String getItemsString()
    {
        if ( !this.aItems.isEmpty() ) {
            StringBuilder vItemString = new StringBuilder("");
            for ( Item vItem : this.aItems.values() ) {
                vItemString.append("\n - ").append(vItem.getLongDescription());
            }
            return vItemString.toString();
        }
        else {
            return "aucun objet.";
        }
    } // getItemsString

    /**
     * Renvoie une liste de tous les Items présents dans la liste.
     */
    public Iterable<Item> getAllItems()
    {
        return this.aItems.values();
    } // getAllItems
} // ItemList
