/**
 * Classe Door - représente une porte entre deux salles dans le jeu "le mystère des ruines Sheikah".
 * Une porte peut être ouverte ou verrouillée. Passer d'un état à l'autre nécessite une clé spécifique.
 *
 * @author  Benoît de Keyn
 * @version 2026.01.12
 */
public class Door
{
    /** La clé nécessaire pour déverrouiller cette porte (null si porte non verrouillable). */
    private Item aKey;
    
    /** État de la porte : true = ouverte, false = verrouillée. */
    private boolean aIsLocked;

    /**
     * Constructeur : crée une porte verrouillée avec une clé spécifique.
     * Par défaut, la porte est verrouillée au lancement du jeu.
     *
     * @param pKey la clé nécessaire pour ouvrir/fermer cette porte
     */
    public Door( final Item pKey )
    {
        this.aKey = pKey;
        this.aIsLocked = true;
    } // Door(*,*)
    
    /**
     * Vérifie si la porte est verrouillée.
     *
     * @return true si la porte est verrouillée, false sinon
     */
    public boolean isLocked()
    {
        return this.aIsLocked;
    } // isLocked()
    
    /**
     * Permet d'ouvrir/déverrouiller la porte.
     */
    public void unlock()
    {
        this.aIsLocked = false;
    } // unlock()
    
    /**
     * Permet de fermer/verrouiller la porte.
     */
    public void lock()
    {
        this.aIsLocked = true;
    } // lock()
    
    /**
     * Renvoie une description de l'état de la porte.
     *
     * @return "ouverte" ou "verrouillée"
     */
    public String getStateDescription()
    {
        return this.aIsLocked ? "verrouillée" : "ouverte";
    } // getStateDescription()

    /**
     * Renvoie la clé associée à cette porte.
     * 
     * @return la clé de la porte
     */
    public Item getKey()
    {
        return this.aKey;
    } // getKey()
} // Door