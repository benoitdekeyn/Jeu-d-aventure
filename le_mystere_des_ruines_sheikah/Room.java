import java.util.HashMap;

/**
 * Classe Room - représente une salle ou un lieu dans le jeu "le mystère des ruines Sheikah".
 * Une salle peut contenir des objets et avoir des sorties vers d'autres salles.
 *
 * @author  Benoît de Keyn
 * @version 2025.10.25
 */
public class Room
{
    private String aDescription;
    private HashMap<String, Room> exits;
    private String aImageName;
    private HashMap<String, Item> aItems;
    
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
        this.aItems = new HashMap<String, Item>();
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
     * Ajoute un objet dans cette salle.
     *
     * @param pItem l'objet à placer dans la salle
     */
    public void addItem( final Item pItem )
    {
        this.aItems.put(pItem.getName(), pItem);
    }

    /**
     * Construit une chaîne de caractères décrivant les objets présents dans la salle.
     *
     * @return une description des objets ou un message indiquant l'absence d'objets
     */
    private String getItemString()
    {
        if ( !this.aItems.isEmpty() ) {
            StringBuilder vItemString = new StringBuilder("Objets présents :");
            for ( Item vItem : this.aItems.values() ) {
                vItemString.append("\n - ").append(vItem.getLongDescription());
            }
            return vItemString.toString();
        }
        else {
            return "Aucun objet ici.";
        }
    }

    /**
     * Renvoie une description complète de la salle.
     * Inclut la description de la salle, les objets présents et les sorties disponibles.
     *
     * @return une description détaillée de la salle
     */
    public String getLongDescription()
    {
        // On ajoute getItemString() à la description affichée
        return "Vous êtes " + this.aDescription + ".\n" + this.getItemString() + "\n" + this.getExitString();
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
