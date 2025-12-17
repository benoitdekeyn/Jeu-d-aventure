/** 
 * Classe Command - représente une commande saisie par l'utilisateur dans le jeu "le mystère des ruines Sheikah".
 * Une commande est composée d'un mot principal et éventuellement d'un second mot.
 *
 * @author  Benoît de Keyn
 * @version 2025.10.25
 */

public class Command
{
    /** Le mot principal de la commande. */
    private String aCommandWord;
    
    /** Le second mot de la commande (peut être null). */
    private String aSecondWord ;
    
    /**
     * Crée une nouvelle commande avec un mot principal et éventuellement un second mot.
     *
     * @param pCommandWord le mot principal de la commande (ex: "aller", "aide", "quitter")
     * @param pSecondWord le second mot de la commande, ou null si absent
     */
    public Command(final String pCommandWord, final String pSecondWord)
    {
        this.aCommandWord = pCommandWord;
        this.aSecondWord = pSecondWord;        
    } // Constructeur
    
    /**
     * Renvoie le mot principal de la commande.
     *
     * @return le mot de commande, ou null si la commande est inconnue
     */
    public String getCommandWord()
    {
        return this.aCommandWord;
    } // getCommandWord
    
    /**
     * Renvoie le second mot de la commande.
     *
     * @return le second mot, ou null si aucun second mot n'est présent
     */
    public String getSecondWord()
    {
        return this.aSecondWord;
    } // getSecondWord
    
    /**
     * Indique si la commande possède un second mot.
     *
     * @return true si un second mot est présent, false sinon
     */
    public boolean hasSecondWord()
    {
        return this.aSecondWord != null;
    } // hasSecondWord
    
    /**
     * Indique si la commande est inconnue (mot principal null).
     *
     * @return true si la commande est inconnue, false sinon
     */
    public boolean isUnknown()
    {
        return this.aCommandWord == null;
    } //isUnknown

    /**
     * Vérifie si une chaîne de caractères donnée est une direction valide.
     *
     * @param pString la chaîne de caractères à tester
     * @return true si la chaîne de caractères est une direction valide, false sinon
     */
    public boolean isDirection( final String pString )
    {
        CommandWords vCommandWords = new CommandWords();
        return vCommandWords.isDirection( pString );
    } // isDirection(*)
    
    
} // Command
