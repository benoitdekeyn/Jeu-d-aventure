import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe RoomRandomizer - gère la sélection aléatoire d'une pièce.
 * Cette classe est utilisée pour obtenir une pièce au hasard parmi celles du jeu,
 * indépendamment de la structure de stockage (HashMap) utilisée par le moteur.
 *
 * @author  Benoît de Keyn
 * @version 2026.01.14
 */
public class RoomRandomizer
{
    /** La collection de toutes les salles du jeu (référence vers GameEngine). */
    private HashMap<String, Room> aRooms;
    
    /** Le générateur de nombres aléatoires. */
    private Random aRandom;

    /**
     * Crée un nouveau générateur de pièces aléatoires.
     *
     * @param pRooms la collection de toutes les salles du jeu
     */
    public RoomRandomizer( final HashMap<String, Room> pRooms )
    {
        this.aRooms = pRooms;
        this.aRandom = new Random();
    } // Constructeur

    /**
     * Choisit et renvoie une pièce au hasard parmi toutes les salles connues.
     *
     * @return une salle (Room) sélectionnée aléatoirement
     */
    public Room findRandomRoom()
    {
        // Conversion des valeurs de la HashMap en une liste indexée pour le tirage au sort sauf  les salles Transporter
        List<Room> vRoomsList = new ArrayList<Room>();
        for ( Room vRoom : this.aRooms.values() ) {
            if ( ! ( vRoom instanceof TransporterRoom ) ) {
                vRoomsList.add( vRoom );
            }
        }
        
        // Tirage d'un index aléatoire
        int vRandomIndex = this.aRandom.nextInt( vRoomsList.size() );
        
        // Retourne la salle correspondante
        return vRoomsList.get( vRandomIndex );
    } // findRandomRoom()

} // RoomRandomizer