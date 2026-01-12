/**
 * Classe CommandWords - contient la liste des mots de commande valides pour le jeu "le mystère des ruines Sheikah".
 * Cette classe permet de vérifier si une commande ou une direction est valide.
 *
 * @author  Michael Kolling, David J. Barnes, D. Bureau, Benoît de Keyn
 * @version 2026.01.10
 */

public class CommandWords
{
    /** Liste des mots de commande valides reconnus par le jeu. */
    private final String[] aValidCommands = {
        "aller", 
        "retour",
        "aide", 
        "quitter", 
        "regarder",
        "respirer",
        "test",
        "prendre",
        "poser",
        "inventaire",
        "ingérer",
        "charger",
        "déclencher",
        "déverrouiller",
        "verrouiller"
    };

    /** Liste des directions valides reconnues par le jeu. */
    private final String[] aValidDirection = {
        "nord", 
        "est", 
        "sud", 
        "ouest", 
        "haut", 
        "bas"
    };

    /**
     * Crée un nouvel objet CommandWords.
     * Les tableaux de commandes et de directions valides sont initialisés directement.
     */
    public CommandWords()
    {
        // rien à faire : les tableaux sont initialisés directement comme constantes
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
     * Renvoie une chaîne de caractères contenant toutes les commandes valides.
     *
     * @return la liste formatée des commandes valides
     */
    public String getValidCommandsString() {
        StringBuilder commands = new StringBuilder();
        for(String command : this.aValidCommands) {
            commands.append("\n -> ").append(command);
        }
        return commands.toString();
    } // getValidCommandsString
} // CommandWords