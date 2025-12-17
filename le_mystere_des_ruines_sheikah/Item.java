/**
 * Classe Item - représente un objet manipulable dans le jeu "le mystère des ruines Sheikah".
 * Chaque objet possède un nom, une description et un poids.
 *
 * @author  Benoît de Keyn
 * @version 2025.12.25
 */

public class Item
{
    private String aName;
    private String aDescription;
    private double aWeight;

    /**
     * Crée un nouvel objet avec un nom, une description et un poids.
     *
     * @param pName le nom court de l'objet (ex: "épée")
     * @param pDescription la description détaillée de l'objet (ex: "une épée rouillée")
     * @param pWeight le poids de l'objet en kilogrammes
     */
    public Item( final String pName, final String pDescription, final double pWeight )
    {
        this.aDescription = pDescription;
        this.aWeight      = pWeight;
        this.aName        = pName;
    }

    /**
     * Renvoie la description détaillée de l'objet.
     *
     * @return la description de l'objet
     */
    public String getDescription()
    {
        return this.aDescription;
    }

    /**
     * Renvoie le poids de l'objet.
     *
     * @return le poids de l'objet en kilogrammes
     */
    public double getWeight()
    {
        return this.aWeight;
    }

    /**
     * Renvoie le nom court de l'objet.
     *
     * @return le nom de l'objet
     */
    public String getName()
    {
        return this.aName;
    }
    
    /**
     * Renvoie une description complète de l'objet incluant le nom, la description et le poids.
     *
     * @return une chaîne formatée avec toutes les informations de l'objet
     */
    public String getLongDescription()
    {
        return this.aName + " : " + this.aDescription + " (" + this.aWeight + " kg)";
    }
}