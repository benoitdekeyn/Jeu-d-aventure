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
    private HashMap<String, Room> aExits;
    
    /** Les portes de la salle (direction -> porte). */
    private HashMap<String, Door> aDoors;
    
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
        this.aExits = new HashMap<String, Room>();
        this.aDoors = new HashMap<String, Door>();
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
    private void setExit(final String pDirection, final Room pNeighbor)
    {
        this.aExits.put(pDirection, pNeighbor);
    } // setExit(*,*)

    /**
     * Renvoie la salle à laquelle mène la sortie donnée.
     *
     * @param pDirection la direction demandée
     * @return la Room voisine pour cette direction, ou null si aucune sortie dans cette direction
     */
    public Room getExit(final String pDirection)
    {
         return this.aExits.get(pDirection);
    } // getExit(*)

    /**
     * Définit une porte dans une direction donnée.
     *
     * @param pDirection la direction de la porte (ex: "nord", "est", "haut", ...)
     * @param pDoor la porte à placer
     */
    public void setDoor(final String pDirection, final Door pDoor)
    {
        this.aDoors.put(pDirection, pDoor);
    } // setDoor(*,*)

    /**
     * Renvoie la porte dans une direction donnée.
     *
     * @param pDirection la direction demandée
     * @return la Door pour cette direction, ou null s'il n'y a pas de porte
     */
    public Door getDoor(final String pDirection)
    {
        return this.aDoors.get(pDirection);
    } // getDoor(*)

    /**
     * Construit une chaîne de caractères listant les sorties disponibles depuis cette salle.
     *
     * @return une chaîne de caractères contenant les directions possibles
     */
    public String getExitString() 
    {
        StringBuilder vExitString = new StringBuilder("Les directions possibles sont :");
        for ( String vDirection : this.aExits.keySet() ) {
            vExitString.append("\n -> ").append(vDirection);
            
            // Afficher l'état de la porte si elle existe
            Door vDoor = this.aDoors.get(vDirection);
            if ( vDoor != null) {
                vExitString.append(" [porte ").append(vDoor.getStateDescription()).append("]");
            }
        }
        return vExitString.toString();
    } // getExitString()

    /**
     * Vérifie s'il existe une sortie de cette salle vers une salle donnée.
     * 
     * @param pRoom la salle cible à vérifier
     * @return true si une sortie vers pRoom existe, false sinon
     */
    public boolean hasExitTo( final Room pRoom )
    {
        return this.aExits.containsValue( pRoom );
    } // hasExitTo(*)


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

    /**
     * Connecte deux salles de manière unidirectionnelle sans porte (trap door).
     *
     * @param pRoom1 la salle de départ
     * @param pDirection la direction depuis la salle de départ
     * @param pRoom2 la salle d'arrivée
     */
    public static void connectRooms( final Room pRoom1, final String pDirection, final Room pRoom2 )
    {
        pRoom1.setExit(pDirection, pRoom2);
    } // connectRooms(*,*,*)

    /**
     * Connecte deux salles de manière bidirectionnelle sans porte.
     *
     * @param pRoom1 la première salle
     * @param pDirection1 la direction depuis la première salle vers la seconde
     * @param pRoom2 la seconde salle
     * @param pDirection2 la direction depuis la seconde salle vers la première
     */
    public static void connectRooms( final Room pRoom1, final String pDirection1, final Room pRoom2, final String pDirection2 )
    {
        pRoom1.setExit(pDirection1, pRoom2);
        pRoom2.setExit(pDirection2, pRoom1);
    } // connectRooms(*,*,*,*)

    /**
     * Connecte deux salles de manière unidirectionnelle avec une porte verrouillée (trap door avec clé).
     *
     * @param pRoom1 la salle de départ
     * @param pDirection la direction depuis la salle de départ
     * @param pRoom2 la salle d'arrivée
     * @param pKey la clé nécessaire pour ouvrir la porte
     */
    public static void connectRooms( final Room pRoom1, final String pDirection, final Room pRoom2, final Item pKey )
    {
        connectRooms(pRoom1, pDirection, pRoom2);
        Door vDoor = new Door(pKey);
        pRoom1.setDoor(pDirection, vDoor);
    } // connectRooms(*,*,*,*)

    /**
     * Connecte deux salles de manière bidirectionnelle avec une porte verrouillée par une clé.
     * Crée une seule porte partagée entre les deux directions (ouvrir d'un côté = ouvrir de l'autre).
     *
     * @param pRoom1 la première salle
     * @param pDirection1 la direction depuis la première salle vers la seconde
     * @param pRoom2 la seconde salle
     * @param pDirection2 la direction depuis la seconde salle vers la première
     * @param pKey la clé nécessaire pour ouvrir la porte
     */
    public static void connectRooms( final Room pRoom1, final String pDirection1, final Room pRoom2, final String pDirection2, final Item pKey )
    {
        connectRooms(pRoom1, pDirection1, pRoom2, pDirection2);
        Door vDoor = new Door(pKey);
        pRoom1.setDoor(pDirection1, vDoor);
        pRoom2.setDoor(pDirection2, vDoor);
    } // connectRooms(*,*,*,*,*)

} // Room
