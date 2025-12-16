/**
 * Classe GameEngine - le moteur du jeu "le mystere des ruines Sheikah".
 * 
 * @author  Michael Kolling and David J. Barnes + Benoît de Keyn
 * @version 1.0 (Jan 2003) DB edited (2019) + 2025.12.25
 */
public class GameEngine
{
    private Parser        aParser;
    private Room          aCurrentRoom;
    private UserInterface aGui;
    private String        aImagesFolder = "Images/";

    /**
     * Constructor for objects of class GameEngine
     */
    public GameEngine()
    {
        this.aParser = new Parser();
        this.createRooms();
    }

    public void setGUI( final UserInterface pUserInterface )
    {
        this.aGui = pUserInterface;
        this.aGui.setImagesFolder( this.aImagesFolder );
        this.printWelcome();
    }

    /**
     * Crée toutes les salles (rooms) du jeu et configure leurs sorties.
     * La méthode instancie les objets Room et définit la salle de départ.
     */
    private void createRooms()
    {
        // Création des rooms
        Room vNord        = new Room("dans la zone au NORD des ruines",               "zone nord.png");
        Room vEst         = new Room("dans la zone à l'EST des ruines",               "zone est.png");
        Room vSud         = new Room("dans la zone au SUD des ruines",                "zone sud.png");
        Room vOuest       = new Room("dans la zone à l'OUEST des ruines",             "zone ouest.png");
        Room vMurNord     = new Room("devant le mur NORD des ruines",                 "mur nord.png");
        Room vMurOuest    = new Room("devant le mur OUEST des ruines",                "mur ouest.png");
        Room vPorte       = new Room("à la porte scellée du mur SUD des ruines",      "porte.png");
        Room vEscaliers   = new Room("aux escaliers du mur EST des ruines",           "escaliers.png");
        Room vToitRuines  = new Room("sur le dessus des ruines",                      "toit ruines.png");
        Room vArbre       = new Room("en hauteur, dans l'arbre au-dessus des ruines", "arbre.png");

        // Création des Items
        Item vCarte     = new Item("carte", "une carte ancienne", 0.1);
        Item vClef      = new Item("clé", "une clé ancienne", 0.5);
        Item vBranche   = new Item("branche", "une branche solide", 1.2);
        Item vEpee      = new Item("épée", "une épée rouillée", 2.0);

        // zones extérieures
        vNord.setExit("est", vEst);
        vNord.setExit("sud", vMurNord);
        vNord.setExit("ouest", vOuest);
        vEst.setExit("nord", vNord);
        vEst.setExit("sud", vSud);
        vEst.setExit("ouest", vEscaliers);  
        vSud.setExit("nord", vPorte);
        vSud.setExit("est", vEst);
        vSud.setExit("ouest", vOuest);     
        vOuest.setExit("nord", vNord);
        vOuest.setExit("est", vMurOuest);
        vOuest.setExit("sud", vSud);

        // murs
        vMurNord.setExit("nord", vNord);
        vEscaliers.setExit("est", vEst);
        vEscaliers.setExit("haut", vToitRuines);
        vPorte.setExit("sud", vSud);
        vMurOuest.setExit("ouest", vOuest);

        // dessus / arbre
        vToitRuines.setExit("bas", vEscaliers);
        vToitRuines.setExit("haut", vArbre);
        vArbre.setExit("bas", vToitRuines);

        // Placement des Items dans les rooms
        vMurNord.addItem(vCarte);
        vMurOuest.addItem(vEpee);
        vArbre.addItem(vClef);
        vArbre.addItem(vBranche);
        
        // room de départ
        this.aCurrentRoom = vSud;
    } // createRooms

    /**
     * Affiche le message de bienvenue et les informations de localisation initiales.
     */
    private void printWelcome()
    {
        this.aGui.println(
            "\nBienvenue dans la mystérieuse jungle Korogu ! \n" +
            "Vous êtes enfin parvenu face aux ruines anciennes du peuple Sheikah. \n" +
            "Vous devez maintenant trouver cet artefact si précieux à l'intérieur des ruines. \n" +
            "\n" +
            "Tapez 'aide' si vous avez besoin d'aide. \n");
        printLocationInfo();
        displayLocationImage();
    } // printWelcome

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    public void interpretCommand( final String pCommandLine ) 
    {
        this.aGui.println( "\n> " + pCommandLine );
        Command vCommand = this.aParser.getCommand( pCommandLine );

        if ( vCommand.isUnknown() ) {
            this.aGui.println("Cette commande n'existe pas.");
            return;
        }

        switch (vCommand.getCommandWord()) {
            case "quitter" :
                quit(vCommand);
                break;
            case "aller" :
                goRoom(vCommand);
                break;
            case "aide" :
                printHelp();
                break;
            case "respirer" :
                breathe();
                break;
            case "regarder" :
                look();
                break;
            default :
                System.out.println("Cette commande n'a pas encore d'action associée.");
                break;
        }
    } // interpretCommand(*)

    /**
     * Traite la commande "quitter".
     *
     * @param pCommand la commande reçue (doit ne pas avoir de second mot)
     * @return true si le jeu doit se terminer, false sinon
     */
    private void quit(final Command pCommand)
    {
        if (pCommand.hasSecondWord()) {
           this.aGui.println("tapez seulement \"quitter\" si vous voulez quitter le jeu."); 
           return;
        }
        this.aGui.println("Merci d'avoir joué. Au revoir.");
        this.aGui.enable( false );
    } // quit(*)

    /**
     * Exécute la commande "aller" : tente de se déplacer dans la direction donnée.
     *
     * @param pCommand la commande à traiter (doit contenir un second mot indiquant la direction)
     *                  si le second mot est absent, affiche une erreur
     */
    private void goRoom( final Command pCommand ) 
    {
        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Veuillez préciser une direction à la suite de la commande \"aller\". (ex: \"aller nord\")");
            return;
        }

        String vDirection = pCommand.getSecondWord();

        if (vDirection.equals("invalid")) {
            System.out.println("Cette direction n'existe pas.");
            return;
        }
        
        Room vNextRoom = this.aCurrentRoom.getExit( vDirection );

        if ( vNextRoom == null ) {
            this.aGui.println("Vous ne pouvez pas aller dans cette direction !");
            return;
        } 
        
        this.aCurrentRoom = vNextRoom;
        printLocationInfo();
        displayLocationImage();
    } // goRoom(*)


    /**
     * Affiche l'aide et la liste des commandes disponibles.
     */
    private void printHelp()
    {
        this.aGui.println(
            "Vous êtes au milieu de la jungle Korogu, \n" +
            "parmi les ruines anciennes du peuple Sheikah. \n" +
            "Trouvez l'artefact technologique caché à l'intérieur des ruines !"
        );
        this.aGui.print("\nVos commandes sont :");
        this.aGui.println(this.aParser.getCommandsList());
    } // printHelp
    /**
     * Traite la commande "respirer". Affiche seulement un texte.
     */
    private void breathe()
    {
        this.aGui.println("Vous venez de consommer une bouffée d'oxygène de votre réserve.");
    }

    /**
     * Affiche la description de la salle courante et les sorties disponibles.
     */
    private void look()
    {
        printLocationInfo();
    } // look

    /**
     * Affiche la description de la salle courante et les sorties disponibles.
     */
    private void printLocationInfo()
    {
        this.aGui.println(this.aCurrentRoom.getLongDescription());
    } // printLocationInfo

    /**
     * Affiche l'image de la salle courante dans l'interface graphique.
     */
    private void displayLocationImage()
    {
        if ( this.aCurrentRoom.getImageName() != null )
            this.aGui.showImage( this.aCurrentRoom.getImageName() );
    } // displayLocationImage

}
