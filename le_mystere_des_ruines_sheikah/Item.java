/**
 * Classe Item - un objet du jeu "le mystere des ruines Sheikah".
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
     * Constructeur naturel
     * @param pDescription Description de l'objet (ex: "une épée rouillée")
     * @param pWeight Poids de l'objet (ex: 2.5)
     * @param pName Nom de l'objet (ex: "épée")
     */
    public Item( final String pName, final String pDescription, final double pWeight )
    {
        this.aDescription = pDescription;
        this.aWeight      = pWeight;
        this.aName        = pName;
    }

    /**
     * Accesseur pour la description
     */
    public String getDescription()
    {
        return this.aDescription;
    }

    /**
     * Accesseur pour le poids
     */
    public double getWeight()
    {
        return this.aWeight;
    }

    /**
     * Accesseur pour le nom
     */
    public String getName()
    {
        return this.aName;
    }
    
    /**
     * Retourne une description complète de l'item
     */
    public String getLongDescription()
    {
        return this.aName + " : " + this.aDescription + " (" + this.aWeight + " kg)";
    }
}