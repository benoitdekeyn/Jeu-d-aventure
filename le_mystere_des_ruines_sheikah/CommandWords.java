/**
 * Cette classe contient une table d'énumération de tous les mots de commande
 * connus du jeu. Elle est utilisée pour reconnaître les commandes saisies.
 *
 * @author  Michael Kolling and David J. Barnes + D.Bureau + Benoît de Keyn
 * @version 2008.03.30 + 2019.09.25 + 2025.10.25
 */
public class CommandWords
{
    // Liste des mots de commande valides
    private final String[] aValidCommands = {
        "aller", 
        "aide", 
        "quitter", 
        "regarder",
        "respirer"
    };

    // Liste des directions valides
    private final String[] aValidDirection = {
        "nord", 
        "est", 
        "sud", 
        "ouest", 
        "haut", 
        "bas"
    };

    /**
     * Constructeur par défaut
     */
    public CommandWords()
    {
        // rien à faire : les tableaux sont initialisés directement comme constantess
    } // constructeur

    /**
     * Vérifie si une chaîne de caractères donnée est un mot de commande valide.
     *
     * @param pString la chaîne de caractères à tester
     * @return true si la chaîne de caractères est un mot de commande connu, false sinon
     */
    public boolean isCommand( final String pString )
    {
        for ( int vI=0; vI<this.aValidCommands.length; vI++ ) {
            if ( this.aValidCommands[vI].equals( pString ) )
                return true;
        } // for
        // si on arrive ici, la chaîne de caractères n'a pas été trouvée parmi les commandes :
        return false;
    } // isCommand(*)

    /**
     * Vérifie si une chaîne de caractères donnée est une direction valide.
     *
     * @param pString la chaîne de caractères à tester
     * @return true si la chaîne de caractères est une direction valide, false sinon
     */
    public boolean isDirection( final String pString )
    {
        for ( int vI=0; vI<this.aValidDirection.length; vI++ ) {
            if ( this.aValidDirection[vI].equals( pString ) )
                return true;
        } // for
        // si on arrive ici, la chaîne de caractères n'a pas été trouvée parmi les directions :
        return false;
    } // isDirection(*)

    /**
     * Affiche toutes les commandes valides.
     */
    public String getValidCommandsString() {
        StringBuilder commands = new StringBuilder();
        for(String command : this.aValidCommands) {
            commands.append("\n -> ").append(command);
        }
        return commands.toString();
    } // getValidCommandsString
} // CommandWords