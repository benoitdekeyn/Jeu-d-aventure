/**
 * Classe Command - une commande du jeu "le mystere des ruines Sheikah".
 *
 * @author  Benoît de Keyn
 * @version 2025.10.25
 */

public class Command
{
    private String aCommandWord;
    private String aSecondWord ;
    
    /**
     * Constructeur naturel
     *
     * @param pCommandWord le mot de la commande (ex: "aller", "aide", "quitter")
     * @param pSecondWord  le second mot de la commande (peut être null si absent)
     */
    public Command(final String pCommandWord, final String pSecondWord)
    {
        this.aCommandWord = pCommandWord;
        this.aSecondWord = pSecondWord;        
    } // Constructeur
    
    /**
     * Renvoie le mot principal de la commande.
     *
     * @return une chaîne de caractères contenant le mot de commande, ou null si commande inconnue
     */
    public String getCommandWord()
    {
        return this.aCommandWord;
    } // getCommandWord
    
    /**
     * Renvoie le second mot de la commande.
     *
     * @return une chaîne de caractères contenant le second mot, ou null si aucun second mot
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
    
    
} // Command
