/**
 * Classe TransporterRoom - une salle spéciale qui téléporte le joueur.
 * Cette salle est une sous-classe de Room. Elle se comporte comme une salle normale
 * pour l'affichage, mais lorsque le joueur essaie d'en sortir (peu importe la direction),
 * il est transporté aléatoirement dans une autre salle du jeu.
 *
 * @author  Benoît de Keyn
 * @version 2026.01.14
 */
public class TransporterRoom extends Room
{
    /** Le générateur aléatoire utilisé pour déterminer la destination. */
    private RoomRandomizer aRandomizer;

    /**
     * Crée une salle de téléportation.
     *
     * @param pDescription la description textuelle de la salle
     * @param pImage       le nom du fichier image représentant la salle
     * @param pRandomizer  le générateur de pièces aléatoires à utiliser
     */
    public TransporterRoom( final String pDescription, final String pImage, final RoomRandomizer pRandomizer )
    {
        super( pDescription, pImage );
        this.aRandomizer = pRandomizer;
    } // Constructeur

    /**
     * Renvoie la salle de destination lors d'une sortie de cette pièce.
     * Cette méthode surcharge (override) celle de la classe Room.
     * Quelle que soit la direction demandée, une pièce aléatoire est retournée.
     *
     * @param pDirection la direction demandée (ignorée par cette salle)
     * @return une salle aléatoire choisie par le Randomizer
     */
    @Override
    public Room getExit( final String pDirection )
    {
        return this.aRandomizer.findRandomRoom();
    } // getExit(*)

} // TransporterRoom