import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Classe UserInterface - l'interface graphique du jeu "le mystère des ruines Sheikah".
 * Cette classe implémente une interface graphique simple avec une zone de saisie,
 * une zone d'affichage de texte et une image optionnelle.
 * 
 * @author Michael Kolling, Benoît de Keyn
 * @version 2025.12.25
 */
public class UserInterface implements ActionListener
{
    private GameEngine aEngine;
    private JFrame     aMyFrame;
    private JTextField aEntryField;
    private JTextArea  aLog;
    private JLabel     aImage;
    private String     aImagesFolder;
    private JButton    aBreathButton;
    private JButton    aLookButton;

    /**
     * Crée une nouvelle interface utilisateur pour le jeu.
     * Initialise les composants graphiques et les lie au moteur de jeu.
     * 
     * @param pGameEngine le moteur de jeu qui traite et exécute les commandes
     */
    public UserInterface( final GameEngine pGameEngine )
    {
        this.aEngine = pGameEngine;
        this.createGUI();
        this.aImagesFolder = "";
    } // UserInterface(.)

    /**
     * Affiche du texte dans la zone de texte.
     *
     * @param pText le texte à afficher
     */
    public void print( final String pText )
    {
        this.aLog.append( pText );
        this.aLog.setCaretPosition( this.aLog.getDocument().getLength() );
    } // print(.)

    /**
     * Affiche du texte dans la zone de texte suivi d'un retour à la ligne.
     *
     * @param pText le texte à afficher
     */
    public void println( final String pText )
    {
        this.print( pText + "\n" );
    } // println(.)

    /**
     * Définit le dossier où sont stockées les images.
     *
     * @param pFolder le chemin du dossier contenant les images
     */
    public void setImagesFolder( final String pFolder )
    {
        this.aImagesFolder = pFolder;
    } // setImagesFolder(.)

    /**
     * Affiche une image dans l'interface graphique.
     * L'image est redimensionnée automatiquement à 600x450 pixels.
     *
     * @param pImageName le nom du fichier image à afficher
     */
    public void showImage( final String pImageName )
    {
        String vImagePath = this.aImagesFolder + pImageName; // to change the directory
        URL vImageURL = this.getClass().getClassLoader().getResource( vImagePath );
        if ( vImageURL == null )
            System.out.println( "Image not found : " + vImagePath );
        else {
            ImageIcon vIcon = new ImageIcon( vImageURL );
            Image vScaledImage = vIcon.getImage().getScaledInstance( 600, 450, Image.SCALE_SMOOTH );
            this.aImage.setIcon( new ImageIcon( vScaledImage ) );
            this.aMyFrame.pack();
        }
    } // showImage(.)

    /**
     * Active ou désactive la saisie dans le champ de texte.
     *
     * @param pOnOff true pour activer la saisie, false pour la désactiver
     */
    public void enable( final boolean pOnOff )
    {
        this.aEntryField.setEditable( pOnOff ); // enable/disable
        if ( pOnOff ) { // enable
            this.aEntryField.getCaret().setBlinkRate( 500 ); // cursor blink
            this.aEntryField.addActionListener( this ); // reacts to entry
        }
        else { // disable
            this.aEntryField.getCaret().setBlinkRate( 0 ); // cursor won't blink
            this.aEntryField.removeActionListener( this ); // won't react to entry
        }
    } // enable(.)

    /**
     * Configure et crée l'interface utilisateur graphique.
     * Initialise la fenêtre, les panneaux, les boutons et les écouteurs d'événements.
     */
    private void createGUI()
    {
        this.aMyFrame = new JFrame( "Le mystère des ruines Sheikah" );
        this.aEntryField = new JTextField( 34 );

        this.aLog = new JTextArea();
        this.aLog.setEditable( false );
        JScrollPane vListScroller = new JScrollPane( this.aLog );
        vListScroller.setPreferredSize( new Dimension(200, 300) );
        vListScroller.setMinimumSize( new Dimension(100, 200) );

        this.aImage = new JLabel();
        this.aBreathButton = new JButton( "Respirer" );
        this.aLookButton = new JButton( "Regarder" );

        JPanel vPanel = new JPanel();
        vPanel.setLayout( new BorderLayout() ); // ==> only five places
        vPanel.add( this.aImage, BorderLayout.NORTH );
        vPanel.add( vListScroller, BorderLayout.CENTER );
        vPanel.add( this.aEntryField, BorderLayout.SOUTH );
        vPanel.add( this.aBreathButton, BorderLayout.EAST );
        vPanel.add( this.aLookButton, BorderLayout.WEST );

        this.aMyFrame.getContentPane().add( vPanel, BorderLayout.CENTER );

        // add some event listeners to some components
        this.aEntryField.addActionListener( this );
        this.aBreathButton.addActionListener( this );
        this.aLookButton.addActionListener( this );

        // to end program when window is closed
        this.aMyFrame.addWindowListener(
            new WindowAdapter() { // anonymous class
                @Override public void windowClosing(final WindowEvent pE)
                {
                    System.exit(0);
                }
        } );

        this.aMyFrame.pack();
        this.aMyFrame.setVisible( true );
        this.aEntryField.requestFocus();
    } // createGUI()

    /**
     * Gère les événements d'action (clics sur boutons ou validation du champ de saisie).
     * Dirige l'action vers la commande appropriée selon la source de l'événement.
     *
     * @param pE l'événement d'action déclenché
     */
    @Override public void actionPerformed( final ActionEvent pE ) 
    {
        // clic du bouton
        if ( pE.getSource() == this.aBreathButton ) {
            this.aEngine.interpretCommand( "respirer" );
        }
        else if ( pE.getSource() == this.aLookButton ) {
            this.aEngine.interpretCommand( "regarder" );
        }
        else {
            // commande terminale
            this.processCommand();
        }
        
    } // actionPerformed(.)

    /**
     * Traite une commande saisie dans le champ de texte.
     * Récupère le texte saisi, vide le champ et transmet la commande au moteur de jeu.
     */
    private void processCommand()
    {
        String vInput = this.aEntryField.getText();
        this.aEntryField.setText( "" );

        this.aEngine.interpretCommand( vInput );
    } // processCommand()
} // UserInterface
