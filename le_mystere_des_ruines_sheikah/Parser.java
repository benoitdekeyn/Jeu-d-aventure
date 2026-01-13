import java.util.StringTokenizer;

/**
 * Classe Parser - interprète les commandes saisies par l'utilisateur dans le jeu "le mystère des ruines Sheikah".
 * Cette classe analyse les chaînes de caractères entrées et les convertit en objets Command.
 *
 * @author  Michael Kolling, David J. Barnes, D. Bureau, Benoît de Keyn
 * @version 2025.12.15
 */
public class Parser 
{
    /** L'ensemble des commandes valides reconnues par le parseur. */
    private CommandWords aValidCommands;

    /**
     * Crée un nouveau parseur de commandes.
     * Initialise la liste des commandes valides.
     */
    public Parser() 
    {
        this.aValidCommands = new CommandWords();
    } // Parser()

    /**
     * Analyse une ligne de commande et la convertit en objet Command.
     * La première chaîne séparée par des espaces est prise comme mot de commande,
     * la deuxième (si elle existe) comme second mot. Les mots suivants sont ignorés.
     * Si le premier mot n'est pas une commande connue, une commande inconnue est renvoyée.
     *
     * @param pInputLine la ligne de commande saisie par l'utilisateur
     * @return un objet Command représentant la commande analysée
     */
    public Command getCommand( final String pInputLine) 
    {

        StringTokenizer vTokenizer = new StringTokenizer( pInputLine );
        
        String vWord1;
        String vWord2;

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
            return new Command( vWord1, vWord2 );
        } 
        else {
            return new Command( null, null ); 
        }
    } // getCommand()

    /**
     * Renvoie une chaîne de caractères contenant toutes les commandes valides.
     *
     * @return la liste des commandes valides sous forme de chaîne
     */
    public String getCommandsList(){
        return this.aValidCommands.getValidCommandsString();
    } // getCommandsList()
} // Parser
