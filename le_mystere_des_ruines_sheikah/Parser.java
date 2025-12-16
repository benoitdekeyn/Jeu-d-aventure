import java.util.StringTokenizer;

/**
 * Ce parser lit la saisie utilisateur et tente de l'interpréter comme une
 * commande du jeu. Il tente d' extraire de la ligne donnée en paramètre, 
 * une commande composée de deux mots au maximum. 
 * Il renvoie la commande sous la forme d'un objet de la classe Command.
 *
 * Le parser possède un ensemble de mots de commande connus. Il compare la
 * saisie utilisateur avec ces mots et, si le premier mot n'est pas connu,
 * retourne un objet Command marqué comme commande inconnue.
 *
 * @author  Michael Kolling and David J. Barnes + D.Bureau + Benoît de Keyn
 * @version 2008.03.30 + 2019.09.25 + 2025.12.15
 */
public class Parser 
{
    private CommandWords aValidCommands;  // (voir la classe CommandWords)

    /**
     * Constructeur par défaut
     */
    public Parser() 
    {
        this.aValidCommands = new CommandWords();
    } // Parser()

    /**
     * Lit la ligne suivante saisie par l'utilisateur et la convertit en objet Command.
     *
     * Règles :
     * - Si le premier mot n'est pas une commande connue, retourne Command(null, null).
     * - Si la commande accepte une direction et qu'un second mot est fourni :
     *     - retourne Command(mot, direction) si la direction est valide,
     *     - retourne Command(mot, "invalid") si la direction est invalide.
     * - Si aucun second mot n'est fourni, retourne Command(mot, null).
     *
     * @return un objet Command représentant la commande saisie
     */
    public Command getCommand( final String pInputLine) 
    {
        String vWord1 = null;
        String vWord2 = null;

        StringTokenizer vTokenizer = new StringTokenizer( pInputLine );
        
        if ( vTokenizer.hasMoreTokens() )
            vWord1 = vTokenizer.nextToken();      // on extrait le premier mot
        else
            vWord1 = null;

        if ( vTokenizer.hasMoreTokens() )
            vWord2 = vTokenizer.nextToken();      // on extrait le deuxième mot
        else
            vWord2 = null;
        // On ignore le reste de la ligne saisie.

        // Verifie si le premier mot est une commande connue.
        // Si non, cree une commande vide avec "null" (pour dire 'commande inconnue').
        // Si oui, cree une commande avec le deuxieme mot qui sera soit :
        // - null si pas de deuxieme mot
        // - un String contenant la direction si la direction est valide
        // - un String "invalid" si la direction est invalide

        if ( this.aValidCommands.isCommand( vWord1 ) ) {
            if ( vWord2 == null ) {
                return new Command( vWord1, null );
            }
            else if ( this.aValidCommands.isDirection( vWord2 ) ) {
                return new Command( vWord1, vWord2 );
            }
            else {
                return new Command( vWord1, "invalid" );
            }
        } 
        else {
            return new Command( null, null ); 
        }
    } // getCommand()

    /**
     * Affiche toutes les commandes valides.
     */
    public String getCommandsList(){
        return this.aValidCommands.getValidCommandsString();
    } // getCommandsList()
} // Parser
