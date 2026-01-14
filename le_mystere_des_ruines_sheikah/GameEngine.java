import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Classe GameEngine - le moteur du jeu "le mystère des ruines Sheikah".
 * Cette classe gère la logique principale du jeu, y compris la création des salles,
 * le traitement des commandes et l'affichage des informations dans l'interface utilisateur.
 *
 * @author  Michael Kolling, David J. Barnes, Benoît de Keyn
 * @version 2026.01.13
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

    /** Compteur de déplacements. */
    private int aMovesCount;

    /** Nombre total de déplacements autorisés avant le Game Over. */
    private final int aMaxMoves = 100;
    
    /** Collection de toutes les salles du jeu */
    private HashMap<String, Room> aRooms;

    /**
     * Crée un nouveau moteur de jeu.
     * Initialise le parseur et crée toutes les salles.
     */
    public GameEngine()
    {
        this.aParser = new Parser();
        this.aRooms = new HashMap<String, Room>();
        this.aMovesCount = 0;
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
        // Création des salles
        Room vNord        = this.createRoom("zone_nord",        "dans la zone au NORD des ruines",               "zone nord.png");
        Room vEst         = this.createRoom("zone_est",         "dans la zone à l'EST des ruines",               "zone est.png");
        Room vSud         = this.createRoom("zone_sud",         "dans la zone au SUD des ruines",                "zone sud.png");
        Room vOuest       = this.createRoom("zone_ouest",       "dans la zone à l'OUEST des ruines",             "zone ouest.png");
        Room vMurNord     = this.createRoom("mur_nord",         "devant le mur NORD des ruines",                 "mur nord.png");
        Room vMurOuest    = this.createRoom("mur_ouest",        "devant le mur OUEST des ruines",                "mur ouest.png");
        Room vPorte       = this.createRoom("porte_sud",        "à la porte scellée du mur SUD des ruines",      "porte.png");
        Room vEscaliers   = this.createRoom("escaliers_est",    "aux escaliers du mur EST des ruines",           "escaliers.png");
        Room vToitRuines  = this.createRoom("toit_ruines",      "sur le dessus des ruines",                      "toit ruines.png");
        Room vArbre       = this.createRoom("arbre",            "en hauteur, dans l'arbre au-dessus des ruines", "arbre.png");
        Room vInterieur   = this.createRoom("interieur",        "à l'intérieur des ruines Sheikah",              "interieur.png");

        // Création des Items
        Item vTeleporteur = new Beamer();
        Item vCarte       = new Item("carte", "une carte ancienne", 0.1);
        Item vClef        = new Item("clé", "une clé ancienne", 0.5);
        Item vBranche     = new Item("branche", "une branche solide", 1.2);
        Item vEpee        = new Item("épée", "une épée rouillée", 2.0);
        Item vBuche       = new Item("bûche", "une bûche lourde", 5.0);
        Item vRocher      = new Item("rocher", "un gros rocher bien lourd", 12.0);
        Item vFiole       = new Item("fiole", "une fiole d'eau oxygénée", 0.2);

        // Création des passages entre les salles
        Room.connectRooms(vEst, "nord", vNord);
        Room.connectRooms(vNord, "sud", vMurNord, "nord");
        Room.connectRooms(vNord, "ouest", vOuest, "nord");
        Room.connectRooms(vEst, "sud", vSud, "est");
        Room.connectRooms(vEst, "ouest", vEscaliers, "est");
        Room.connectRooms(vSud, "nord", vPorte, "sud");
        Room.connectRooms(vSud, "ouest", vOuest, "sud");
        Room.connectRooms(vOuest, "est", vMurOuest, "ouest");
        Room.connectRooms(vEscaliers, "haut", vToitRuines, "bas");
        Room.connectRooms(vToitRuines, "haut", vArbre, "bas");
        Room.connectRooms(vPorte, "nord", vInterieur, "sud", vClef);

        // Placement des Items dans les salles
        vSud.addItem(vTeleporteur);
        vSud.addItem(vBuche);
        vSud.addItem(vRocher);
        vSud.addItem(vFiole);
        vMurNord.addItem(vCarte);
        vMurOuest.addItem(vEpee);
        vArbre.addItem(vClef);
        vArbre.addItem(vBranche);
        
        // Salle de départ (sera assignée au joueur quand il sera créé)
        this.aStartRoom = vSud;
    } // createRooms

    /**
     * Affiche le message de bienvenue et les informations de localisation initiales.
     */
    private void printWelcome()
    {
        this.aGui.println(
            "\nBonjour " + this.aPlayer.getName() + ",\nbienvenue dans la mystérieuse jungle Korugu ! \n" +
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
            case "quitter"       -> quit(vCommand);
            case "aller"         -> goRoom(vCommand);
            case "retour"        -> goBack(vCommand);
            case "aide"          -> printHelp();
            case "respirer"      -> breathe();
            case "regarder"      -> look();
            case "test"          -> executeTest(vCommand);
            case "prendre"       -> take(vCommand);
            case "poser"         -> drop(vCommand);
            case "inventaire"    -> showInventory();
            case "ingérer"       -> ingest(vCommand);
            case "charger"       -> chargeBeamer();
            case "déclencher"    -> triggerBeamer();
            case "déverrouiller" -> unlockDoor(vCommand);
            case "verrouiller"   -> lockDoor(vCommand);
            default              -> System.out.println("Cette commande n'a pas encore d'effet associé.");
        }

    } // interpretCommand(*)

    /**
     * Incrémente le compteur de déplacements et vérifie si le joueur a atteint la limite.
     * Si la limite est atteinte, affiche un message de Game Over et désactive l'interface.
     */
    private void countMoves() 
    {
        this.aMovesCount++;
        if (this.aMovesCount == this.aMaxMoves) {
            this.aGui.println(
                "\nVous avez atteint la limite de " + this.aMovesCount + " déplacements.\n" +
                "\n=============== GAME OVER ==============\n");
            this.aGui.enable( false );
        }
    }

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
        
        Room vCurrentRoom = this.aPlayer.getCurrentRoom();
        Room vNextRoom = vCurrentRoom.getExit( vDirection );

        if ( vNextRoom == null ) {
            this.aGui.println("Vous ne pouvez pas aller dans cette direction !");
            return;
        }
        
        // Vérification de la porte
        Door vDoor = vCurrentRoom.getDoor( vDirection );
        if ( vDoor != null && vDoor.isLocked() ) {
            this.aGui.println("Cette porte est fermée à clé. Vous devez la déverrouiller d'abord.");
            return;
        }

        this.aPlayer.goRoom( vNextRoom );

        if (! vNextRoom.hasExitTo( vCurrentRoom )) {
            this.aPlayer.clearHistory();
        }
        
        printLocationInfo();
        displayLocationImage();
        countMoves();
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
        countMoves();
    } // goBack

    /**
     * Affiche l'aide et la liste des commandes disponibles.
     */
    private void printHelp()
    {
        this.aGui.println(
            "Vous êtes au milieu de la jungle Korugu, \n" +
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
     * Exécute la commande "charger" pour charger le Beamer dans la salle courante.
     */
    private void chargeBeamer()
    {
        if ( ! this.aPlayer.hasItem( "téléporteur" ) ) {
            this.aGui.println("Vous ne possédez pas de Téléporteur.");
            return;
        }
        ((Beamer)this.aPlayer.getItem("téléporteur")).charge(this.aPlayer.getCurrentRoom());
        this.aGui.println("Vous avez chargé le Téléporteur dans cette salle.");
    } // chargeBeamer

    /**
     * Exécute la commande "déclencher" pour téléporter le joueur à la salle chargée dans le Beamer.
     */
    private void triggerBeamer()
    {
        if ( ! this.aPlayer.hasItem( "téléporteur" ) ) {
            this.aGui.println("Vous ne possédez pas de Téléporteur.");
            return;
        }
        Room vTargetRoom = ((Beamer)this.aPlayer.getItem("téléporteur")).trigger();
        if ( vTargetRoom == null ) {
            this.aGui.println("Le Téléporteur n'est pas chargé.");
            return;
        }
        this.aGui.println("Vous avez utilisé le Téléporteur pour vous téléporter !");
        this.aPlayer.goRoom(vTargetRoom);
        this.aPlayer.clearHistory();
        printLocationInfo();
        displayLocationImage();
        countMoves();
    } // triggerBeamer

    /**
     * Exécute la commande "déverrouiller" pour déverrouiller une porte dans une direction donnée.
     *
     * @param pCommand la commande reçue (doit contenir la direction de la porte à déverrouiller)
     */
    private void unlockDoor(final Command pCommand)
    {
        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Déverrouiller quelle porte ? Spécifiez une direction.");
            return;
        }
        String vDirection = pCommand.getSecondWord();
        
        if ( ! pCommand.isDirection( vDirection ) ) {
            this.aGui.println("Cette direction n'existe pas.");
            return;
        }
        
        Door vDoor = this.aPlayer.getCurrentRoom().getDoor( vDirection );
        
        if ( vDoor == null ) {
            this.aGui.println("Il n'y a pas de porte dans cette direction.");
            return;
        }
        
        if ( ! vDoor.isLocked() ) {
            this.aGui.println("Cette porte est déjà ouverte.");
            return;
        }
        
        if ( this.aPlayer.tryUnlockDoor( vDoor ) ) {
            this.aGui.println("Vous avez déverrouillé la porte " + vDirection + ".");
        } else {
            this.aGui.println("Vous n'avez pas la clé pour déverrouiller cette porte.");
        }
    } // unlockDoor(*)

    /**
     * Exécute la commande "verrouiller" pour verrouiller une porte dans une direction donnée.
     *
     * @param pCommand la commande reçue (doit contenir la direction de la porte à verrouiller)
     */
    private void lockDoor(final Command pCommand)
    {
        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Verrouiller quelle porte ? Spécifiez une direction.");
            return;
        }
        String vDirection = pCommand.getSecondWord();
        
        if ( ! pCommand.isDirection( vDirection ) ) {
            this.aGui.println("Cette direction n'existe pas.");
            return;
        }
        
        Door vDoor = this.aPlayer.getCurrentRoom().getDoor( vDirection );
        
        if ( vDoor == null ) {
            this.aGui.println("Il n'y a pas de porte dans cette direction.");
            return;
        }
        
        if ( vDoor.isLocked() ) {
            this.aGui.println("Cette porte est déjà fermée.");
            return;
        }
        
        if ( this.aPlayer.tryLockDoor( vDoor ) ) {
            this.aGui.println("Vous avez verrouillé la porte " + vDirection + ".");
        } else {
            this.aGui.println("Vous n'avez pas la clé pour verrouiller cette porte.");
        }
    } // lockDoor(*)    

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

    /**
     * Crée une nouvelle salle, l'ajoute à la HashMap des salles et la retourne.
     * Méthode helper pour simplifier la création et l'enregistrement simultané des salles.
     *
     * @param pDescription la description textuelle de la salle
     * @param pImage le nom du fichier image représentant la salle
     * @return la salle nouvellement créée
     */
    private Room createRoom(final String pName, final String pDescription, final String pImage )
    {
        Room vRoom = new Room( pDescription, pImage );
        this.aRooms.put( pName, vRoom );
        return vRoom;
    } // createRoom(*,*,*)

}
