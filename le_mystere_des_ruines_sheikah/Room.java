/**
 * Classe Room - un lieu du jeu "le mystere des ruines Sheikah".
 *
 * @author  Benoît de Keyn
 * @version 2025.10.25
 */

import java.util.HashMap;

public class Room
{
    private String aDescription;
    private HashMap<String, Room> exits;
    private String aImageName;
    private Item aItem;
    
    /**
     * Constructeur de la classe Room.
     *
     * @param pDescription la description textuelle de la salle
     */
    public Room(final String pDescription, final String pImage )
    {
        this.aDescription = pDescription;
        this.exits = new HashMap<String, Room>();
        this.aImageName = pImage;
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
     * Définit l'item contenu dans cette salle.
     * @param pItem L'item à placer (ou null pour retirer l'item)
     */
    public void setItem( final Item pItem )
    {
        this.aItem = pItem;
    }

    /**
     * Renvoie une description de l'item présent (s'il y en a un).
     */
    private String getItemString()
    {
        if ( this.aItem != null ) {
            return "Objets présents : \n - " + this.aItem.getLongDescription();
        }
        else {
            return "Aucun objet ici.";
        }
    }

    /**
     * Renvoie une description longue de la salle, incluant les sorties et les items.
     */
    public String getLongDescription()
    {
        // On ajoute getItemString() à la description affichée
        return "Vous êtes " + this.aDescription + ".\n" + this.getItemString() + "\n" + this.getExitString();
    } // getLongDescription

    /**
     * Return a string describing the room's image name
     */
    public String getImageName()
    {
        return this.aImageName;
    }
} // Room
