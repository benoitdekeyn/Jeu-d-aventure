/**
 * Classe Door - représente une porte entre deux salles dans le jeu "le mystère des ruines Sheikah".
 * Une porte peut être ouverte ou vérouillée. Passer d'un état à l'autre nécessite une clé spécifique.
 *
 * @author  Benoît de Keyn
 * @version 2026.01.12
 */
public class Door
{
    /** La clé nécessaire pour déverrouiller cette porte (null si porte non verrouillable). */
    private Item aKey;
    
    /** État de la porte : true = ouverte, false = fermée. */
    private boolean aIsLocked;

    /**
     * Constructeur : crée une porte verrouillée avec une clé spécifique.
     * Par défaut, la porte est fermée au lancement du jeu.
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
    } // canPass()
    
    /**
     * Tente d'ouvrir la porte avec une clé.
     * Si la porte n'a pas de clé (porte normale), elle est toujours ouverte.
     *
     * @param pKey la clé utilisée pour ouvrir
     * @return true si la porte a été ouverte avec succès, false si la clé ne correspond pas
     */
    public boolean unlock( final Item pKey )
    {   
        if ( pKey == this.aKey ) {
            this.aIsLocked = false;
            return true;
        }
        
        return false;
    } // unlock(*)
    
    /**
     * Tente de fermer/verrouiller la porte avec une clé.
     *
     * @param pKey la clé utilisée pour fermer
     * @return true si la porte a été verrouillée, false si la clé ne correspond pas
     */
    public boolean lock( final Item pKey )
    {
        if ( pKey == this.aKey ) {
            this.aIsLocked = true;
            return true;
        }
        
        return false;
    } // lock(*)
    
    /**
     * Renvoie une description de l'état de la porte.
     *
     * @return "ouverte" ou "verrouillée"
     */
    public String getStateDescription()
    {
        return this.aIsLocked ? "verrouillée" : "ouverte";
    } // getStateDescription()
} // Door