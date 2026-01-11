import java.util.HashMap;

/**
 * Classe Room - représente une salle ou un lieu dans le jeu "le mystère des ruines Sheikah".
 * Une salle peut contenir des objets et avoir des sorties vers d'autres salles.
 *
 * @author  Benoît de Keyn
 * @version 2026.01.10
 */
public class Room
{
    /** La description textuelle de la salle. */
    private String aDescription;
    
    /** Les sorties de la salle (direction -> salle voisine). */
    private HashMap<String, Room> exits;
    
    /** Le nom du fichier image représentant la salle. */
    private String aImageName;
    
    /** Les objets présents dans la salle (nom -> objet). */
    private ItemList aItems;
    
    /**
     * Crée une nouvelle salle avec une description et une image associée.
     *
     * @param pDescription la description textuelle de la salle
     * @param pImage le nom du fichier image représentant la salle
     */
    public Room(final String pDescription, final String pImage )
    {
        this.aDescription = pDescription;
        this.exits = new HashMap<String, Room>();
        this.aImageName = pImage;
        this.aItems = new ItemList();
    } // constructeur

    /**
     * Renvoie la description textuelle de la salle.
     *
     * @return la description de la salle
     */
    public String getDescription()
    {
        return this.aDescription;
    } // getDescription

    /**
     * Définit une sortie depuis cette salle vers une salle voisine.
     *
     * @param pDirection la direction de la sortie (ex: "nord", "est", "haut", ...)
     * @param pNeighbor  la salle cible pour cette direction
     */
    public void setExit(final String pDirection, final Room pNeighbor)
    {
        this.exits.put(pDirection, pNeighbor);
    } // setExit(*,*)

    /**
     * Renvoie la salle à laquelle mène la sortie donnée.
     *
     * @param pDirection la direction demandée
     * @return la Room voisine pour cette direction, ou null si aucune sortie dans cette direction
     */
    public Room getExit(final String pDirection)
    {
         return this.exits.get(pDirection);
    } // getExit(*)

    /**
     * Construit une chaîne de caractères listant les sorties disponibles depuis cette salle.
     *
     * @return une chaîne de caractères contenant les directions possibles
     */
    public String getExitString() 
    {
        StringBuilder vExitString = new StringBuilder("Les directions possibles sont :");
        for ( String vDirection : this.exits.keySet() ) {
            vExitString.append("\n -> ").append(vDirection);
        }
        return vExitString.toString();
    } // getExitString

    /**
     * Renvoie la direction opposée à une direction donnée.
     */
    public static String opppositeOf( final String pDirection )
    {
        return switch ( pDirection ) {
            case "nord"  -> "sud";
            case "sud"   -> "nord";
            case "est"   -> "ouest";
            case "ouest" -> "est";
            case "haut"  -> "bas";
            case "bas"   -> "haut";
            default      -> "";
        };
    }

    /**
     * Ajoute un objet dans la liste des objets présents dans cette salle.
     *
     * @param pItem l'objet à ajouter dans la salle
     */
    public void addItem( final Item pItem )
    {
        this.aItems.add(pItem);
    }

    /**
     * Retire un objet de la liste des objets présents dans cette salle.
     * Si l'objet n'existe pas dans la salle, aucune action n'est effectuée.
     *
     * @param pItemName le nom de l'objet à retirer de la salle
     */
    public void removeItem( final String pItemName )
    {
        this.aItems.remove(pItemName);
    } // removeItem

    /**
     * Renvoie un objet de la liste des objets présents dans cette salle.
     *
     * @param pItemName le nom de l'objet recherché dans la salle
     * @return l'objet correspondant, ou null si l'objet n'est pas présent dans la salle
     */
    public Item getItem( final String pItemName )
    {
        return this.aItems.get(pItemName);
    } // getItem

    /**
     * Renvoie une chaîne de caractères listant tous les objets présents dans cette salle.
     *
     * @return une description textuelle des objets présents dans la salle
     */
    public String getItemsString() {
        return this.aItems.getItemsString();
    } // getItemsString

    /**
     * Renvoie une description complète de la salle.
     * Inclut la description de la salle, les objets présents et les sorties disponibles.
     *
     * @return une description détaillée de la salle
     */
    public String getLongDescription()
    {
        // On ajoute getItemsString() à la description affichée
        return "Vous êtes " + this.aDescription + ".\n" + "La pièce contient : " + this.getItemsString() + "\n" + this.getExitString();
    } // getLongDescription

    /**
     * Renvoie le nom du fichier image associé à la salle.
     *
     * @return le nom du fichier image
     */
    public String getImageName()
    {
        return this.aImageName;
    }
} // Room
