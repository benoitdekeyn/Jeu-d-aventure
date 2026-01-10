import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Classe GameEngine - le moteur du jeu "le mystère des ruines Sheikah".
 * Cette classe gère la logique principale du jeu, y compris la création des salles,
 * le traitement des commandes et l'affichage des informations dans l'interface utilisateur.
 *
 * @author  Michael Kolling, David J. Barnes, Benoît de Keyn
 * @version 2026.01.10
 */
public class GameEngine
{
    /** Le parseur de commandes du jeu. */
    private final Parser aParser;
    
    /** Le joueur du jeu. */
    private Player aPlayer;
    
    /** L'interface utilisateur graphique. */
    private UserInterface aGui;
    
    /** Le chemin du dossier contenant les images du jeu. */
    private final String aImagesFolder = "Images/";
    
    /** La salle de départ du jeu. */
    private Room aStartRoom;

    /**
     * Crée un nouveau moteur de jeu.
     * Initialise le parseur et crée toutes les salles.
     */
    public GameEngine()
    {
        this.aParser = new Parser();
        this.createRooms();
    }

    /**
     * Définit le joueur du jeu et l'initialise dans la salle de départ.
     *
     * @param pPlayer le joueur à utiliser dans le jeu
     */
    public void setPlayer( final Player pPlayer )
    {
        this.aPlayer = pPlayer;
        this.aPlayer.setCurrentRoom( this.aStartRoom );
    }

    /**
     * Définit l'interface utilisateur graphique et affiche le message de bienvenue.
     *
     * @param pUserInterface l'interface utilisateur à utiliser pour les affichages
     */
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
        Item vBuche     = new Item("bûche", "une bûche lourde", 5.0);
        Item vRocher    = new Item("rocher", "un gros rocher bien lourd", 12.0);
        Item vFiole      = new Item("fiole", "une fiole d'eau oxygénée", 0.2);

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
        vSud.addItem(vBuche);
        vSud.addItem(vRocher);
        vSud.addItem(vFiole);
        
        // room de départ (sera assignée au joueur quand il sera créé)
        this.aStartRoom = vSud;
    } // createRooms

    /**
     * Affiche le message de bienvenue et les informations de localisation initiales.
     */
    private void printWelcome()
    {
        this.aGui.println(
            "\nBonjour " + this.aPlayer.getName() + ",\nbienvenue dans la mystérieuse jungle Korogu ! \n" +
            "Vous êtes enfin parvenu face aux ruines anciennes du peuple Sheikah. \n" +
            "Vous devez maintenant trouver cet artefact si précieux à l'intérieur des ruines. \n" +
            "\n" +
            "Tapez 'aide' si vous avez besoin d'aide. \n");
        printLocationInfo();
        displayLocationImage();
    } // printWelcome

    /**
     * Interprète et exécute une commande donnée sous forme de chaîne de caractères.
     *
     * @param pCommandLine la ligne de commande saisie par l'utilisateur
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
            case "quitter"     -> quit(vCommand);
            case "aller"       -> goRoom(vCommand);
            case "retour"      -> goBack(vCommand);
            case "aide"        -> printHelp();
            case "respirer"    -> breathe();
            case "regarder"    -> look();
            case "test"        -> executeTest(vCommand);
            case "prendre"     -> take(vCommand);
            case "poser"       -> drop(vCommand);
            case "inventaire"  -> showInventory();
            case "ingérer"     -> ingest(vCommand);
            default            -> System.out.println("Cette commande n'a pas encore d'action associée.");
        }
    } // interpretCommand(*)

    /**
     * Traite la commande "quitter".
     * Vérifie qu'aucun second mot n'est présent et termine le jeu.
     *
     * @param pCommand la commande reçue (ne doit pas avoir de second mot)
     */
    private void quit(final Command pCommand)
    {
        if (pCommand.hasSecondWord()) {
           this.aGui.println("tapez seulement \"quitter\" si vous voulez quitter le jeu."); 
           return;
        }
        this.aGui.println("Merci d'avoir joué, " + this.aPlayer.getName() + ". Au revoir.");
        this.aGui.enable( false );
    } // quit(*)

    /**
     * Exécute la commande "aller" pour se déplacer dans une direction.
     * Vérifie que la direction est valide et que la sortie existe.
     *
     * @param pCommand la commande à traiter (doit contenir un second mot indiquant la direction)
     */
    private void goRoom( final Command pCommand ) 
    {
        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Veuillez préciser une direction à la suite de la commande \"aller\". (ex: \"aller nord\")");
            return;
        }

        String vDirection = pCommand.getSecondWord();

        if ( ! pCommand.isDirection( vDirection ) ) {
            this.aGui.println("Cette direction n'existe pas.");
            return;
        }
        
        Room vNextRoom = this.aPlayer.getCurrentRoom().getExit( vDirection );

        if ( vNextRoom == null ) {
            this.aGui.println("Vous ne pouvez pas aller dans cette direction !");
            return;
        } 
        
        this.aPlayer.goRoom( vNextRoom );
        printLocationInfo();
        displayLocationImage();
    } // goRoom(*)

    /**
     * Exécute la commande "retour" pour revenir à la salle précédente.
     * Vérifie qu'aucun second mot n'est présent et qu'il existe un historique de salles visitées.
     *
     * @param pCommand la commande reçue (ne doit pas avoir de second mot)
     */
    private void goBack(final Command pCommand)
    {
        if (pCommand.hasSecondWord()) {
            this.aGui.println("tapez seulement \"retour\" si vous voulez revenir à la salle précédente.");
            return;
        }
        if ( ! this.aPlayer.goBack() ) {
            this.aGui.println("Vous ne pouvez pas revenir en arrière.");
            return;
        }
        printLocationInfo();
        displayLocationImage();
    } // goBack

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
     * Traite la commande "respirer".
     * Affiche un message indiquant que le joueur consomme de l'oxygène.
     */
    private void breathe()
    {
        this.aGui.println("Vous venez de consommer une bouffée d'oxygène de votre réserve.");
    }

    /**
     * Traite la commande "regarder".
     * Affiche la description de la salle courante, les objets présents et les sorties disponibles.
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
        this.aGui.println(this.aPlayer.getCurrentRoom().getLongDescription());
    } // printLocationInfo

    /**
     * Affiche l'image de la salle courante dans l'interface graphique.
     */
    private void displayLocationImage()
    {
        if ( this.aPlayer.getCurrentRoom().getImageName() != null )
            this.aGui.showImage( this.aPlayer.getCurrentRoom().getImageName() );
    } // displayLocationImage

    /**
     * Traite la commande "prendre" pour ramasser un objet dans la salle courante.
     * Vérifie que l'objet existe dans la salle et que le joueur peut le porter
     * (capacité de poids suffisante). Si l'objet est ramassé, il est retiré de la
     * salle et ajouté à l'inventaire du joueur.
     *
     * @param pCommand la commande reçue (doit contenir le nom de l'objet à prendre)
     */
    private void take( final Command pCommand )
    {
        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Veuillez préciser un objet à prendre.");
            return;
        }

        String vItemName = pCommand.getSecondWord();
        Item vItem = this.aPlayer.getCurrentRoom().getItem( vItemName );

        if ( vItem == null ) {
            this.aGui.println("Il n'y a pas de tel objet ici.");
            return;
        }
    
        if ( this.aPlayer.getInventoryWeight() + vItem.getWeight() > this.aPlayer.getInventoryCapacity() ) {
            this.aGui.println(
                "Vous ne pouvez porter que " + this.aPlayer.getInventoryCapacity() + " kg au maximum.\n" +
                "Et vous portez déjà " + this.aPlayer.getInventoryWeight() + " kg.\n" +
                "Or cet objet pèse " + vItem.getWeight() + " kg.");
        } else {
            this.aPlayer.addItem( vItem );
            this.aPlayer.getCurrentRoom().removeItem( vItemName );
            this.aGui.println("Vous avez ajouté \"" + vItem.getName() + "\" à votre inventaire.");
        }
        
    } // take(*)

    /**
     * Traite la commande "poser" pour déposer un objet dans la salle courante.
     * Vérifie que le joueur possède bien l'objet spécifié dans son inventaire.
     * Si l'objet est déposé, il est retiré de l'inventaire du joueur et ajouté
     * à la liste des objets de la salle courante.
     *
     * @param pCommand la commande reçue (doit contenir le nom de l'objet à poser)
     */
    private void drop( final Command pCommand )
    {
        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Poser quoi ? Spécifiez un objet.");
            return;
        }

        String vItemName = pCommand.getSecondWord();
        
        if ( ! this.aPlayer.hasItem( vItemName ) ) {
            this.aGui.println("Vous ne portez pas cet objet.");
            return;
        }

        Item vItem = this.aPlayer.getItem( vItemName );
        this.aPlayer.getCurrentRoom().addItem( vItem );
        this.aPlayer.removeItem( vItemName );
        this.aGui.println("Vous avez bien posé : " + vItem.getName());
    } // drop(*)

    /**
     * Traite la commande "inventaire" pour afficher le contenu de l'inventaire du joueur.
     * Affiche la liste de tous les objets que le joueur porte actuellement.
     */
    private void showInventory()
    {
        this.aGui.println("Vous portez : " + this.aPlayer.getInventoryContents());
    } // showInventory

    /**
     * Exécute la commande "ingérer" pour consommer un objet de l'inventaire.
     * Vérifie que l'objet existe dans l'inventaire du joueur. Si l'objet est ingéré,
     * il est retiré de l'inventaire du joueur et son effet est appliqué.
     *
     * @param pCommand la commande reçue (qui contient potentiellement le nom de l'objet à ingérer)
     */
    private void ingest( final Command pCommand )
    {
        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Qu'est-ce que tu veux te mettre sous la dent ? Réessaye pour voir.");
            return;
        }
        String vItemName = pCommand.getSecondWord();
        if ( ! this.aPlayer.hasItem( vItemName ) ) {
            this.aGui.println("Va falloir mieux chercher avec tes petits yeux ! T'as pas ça sur toi.");
            return;
        }
        Item vItem = this.aPlayer.getItem( vItemName );
        switch ( vItem.getName() ) {
            case "fiole" -> this.drinkH202();
            default -> { this.aGui.println("Mais ti'es complètment fadaaaa !!!"); return;}
        }
        this.aPlayer.removeItem( vItemName );
    } // ingest(*)

    /**
     * Applique l'effet de boire de l'eau oxygénée (H2O2).
     * Double la capacité maximale de poids de l'inventaire du joueur.
     */
    private void drinkH202()
    {
        this.aPlayer.doubleInventoryCapacity();
        this.aGui.println(
            "Vous avez consommé votre fiole d'eau oxygénée.\n"+
            "Mais comme on est dans un jeu,\n" + 
            "votre taux d'oxygène a doublé, et avec cela votre force 💪.\n" +
            "Vous pouvez maintenant porter jusqu'à " + this.aPlayer.getInventoryCapacity() + " kg.");
    } // drinkH202

    /**
     * Exécute la commande "test" pour lire et exécuter des commandes depuis un fichier.
     * Le fichier doit contenir une commande par ligne et se trouver à la racine du projet.
     *
     * @param pCommand la commande test avec le nom du fichier (sans extension .txt)
     */
    private void executeTest(final Command pCommand)
    {
        if (!pCommand.hasSecondWord()) {
            this.aGui.println("Test quel fichier ? Spécifiez un nom de fichier.");
            return;
        }
        String pNomFichier = pCommand.getSecondWord() + ".txt";
        Scanner vScanner;
        try { // pour "essayer" les instructions suivantes :
            vScanner = new Scanner( new File( pNomFichier ) ); // ouverture du fichier s'il existe
            this.aGui.println("\n============= Exécution TEST =============\n");
            while ( vScanner.hasNextLine() ) { // tant qu'il y a encore une ligne à lire dans le fichier
                String vLigne = vScanner.nextLine(); // lecture de la ligne dans le fichier
                this.interpretCommand( vLigne );
            } // while
            this.aGui.println("\n============= TEST terminé =============\n");
        } // try
        catch ( final FileNotFoundException pFNFE ) { // si le fichier n'existe pas
            this.aGui.println("Le fichier '" + pNomFichier + "' est introuvable.");
        } // catch
        
    } // executeTest(*)

}
