
/**
 * Classe héritée d'Item représentant un Téléporteur dans le jeu "le mystère des ruines Sheikah".
 * Un Beamer permet au joueur de le charger dans une pièce A,
 * puis de le déclencher depuis une pièce B pour s'y téléporter immédiatement.
 *
 */
public class Beamer extends Item
{
    /** La salle où le Beamer a été chargé. */
    private Room aChargedRoom;

    /**
     * Constructeur par défaut du Téléporteur.
     *
     */
    public Beamer()
    {
        super("téléporteur", "Un téléporteur portable", 1.0);
        this.aChargedRoom = null;
    } // constructeur

    /**
     * Charge le Beamer avec la salle courante du joueur.
     *
     * @param pCurrentRoom la salle courante du joueur
     */
    public void charge(final Room pCurrentRoom)
    {
        this.aChargedRoom = pCurrentRoom;
    } // charge(*)

    /**
     * Déclenche le Beamer pour téléporter le joueur à la salle chargée.
     *
     * @return la salle chargée si le Beamer est chargé, sinon null
     */
    public Room trigger()
    {
        Room vRoomToReturn = this.aChargedRoom;
        this.aChargedRoom = null; // décharge le Beamer après utilisation
        return vRoomToReturn;
    } // trigger()

} // Beamer
