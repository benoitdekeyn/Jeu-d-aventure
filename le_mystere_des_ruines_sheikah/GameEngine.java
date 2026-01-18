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

    /** Debug mode pour truquer le hasard avec la commande alea dans les tests */
    private boolean aDebugMode;

    /** Etat du jeux (en cours ou finit) */
    private boolean aGameOver;

    /**
     * Crée un nouveau moteur de jeu.
     * Initialise le parseur et crée toutes les salles.
     */
    public GameEngine()
    {
        this.aParser = new Parser();
        this.aRooms = new HashMap<String, Room>();
        this.aMovesCount = 0;
        this.aDebugMode = false;
        this.aGameOver = false;
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
        // Création des Items
        Item vTeleporteur   = new Beamer();
        Item vOiseau        = new Item("oiseau", "un oiseau qui a fait son nid", 0.1);
        Item vPoussin       = new Item("poussin", "un poussin tout juste éclos", 0.2);
        Item vLezard        = new Item("lézard", "un lézard qui prend le soleil", 0.5);
        Item vPapillon      = new Item("papillon", "un papillon coloré", 0.1);
        Item vCoccinelle    = new Item("coccinelle", "une coccinelle porte-bonheur", 0.1);
        Item vLapin         = new Item("lapin", "un lapin qui se promène", 1.5);
        Item vCle           = new Item("clé", "une clé ancienne", 0.3);
        Item vTorche        = new Item("torche", "une torche enflammée", 0.7);
        Item vRocher        = new Item("rocher", "un gros rocher bien lourd", 12.0);
        Item vFiole         = new Item("fiole", "une fiole d'eau oxygénée", 0.5);
        Item vPyrotaris     = new Item("pyrotaris", "le fameux bijou de technologie sheikah", 3.0);

        // Création des salles
        Room vNord        = this.createRoom("zone_nord",        "dans la zone au NORD des ruines",               "zone nord.png");
        Room vOuest       = this.createRoom("zone_ouest",       "dans la zone à l'OUEST des ruines",             "zone ouest.png");
        Room vSud         = this.createRoom("zone_sud",         "dans la zone au SUD des ruines",                "zone sud.png");
        Room vEst         = this.createRoom("zone_est",         "dans la zone à l'EST des ruines",               "zone est.png");
        Room vMurNord     = this.createRoom("mur_nord",         "devant le mur NORD des ruines",                 "mur nord.png");
        Room vMurOuest    = this.createRoom("mur_ouest",        "devant le mur OUEST des ruines",                "mur ouest.png");
        Room vPorte       = this.createRoom("porte_sud",        "à la porte scellée du mur SUD des ruines",      "porte sud.png");
        Room vEscaliers   = this.createRoom("escaliers_est",    "aux escaliers du mur EST des ruines",           "escaliers est.png");
        Room vToitRuines  = this.createRoom("toit_ruines",      "sur le dessus des ruines",                      "toit ruines.png");
        Room vArbre       = this.createRoom("arbre",            "en hauteur, dans l'arbre au-dessus des ruines", "arbre.png");
        Room vInterieur   = this.createRoom("interieur_ruines", "à l'intérieur des ruines Sheikah",              "interieur ruines.png");
        Room vSalleFinale = this.createRoom("salle_finale",     "dans la salle qui renferme le trésor ultime",   "salle finale.png");

        // Salle spéciale TransporterRoom
        Room vTransporter = new TransporterRoom(
            "dans une salle mystérieuse qui vous aspire \nsans que vous puissiez vous en extraire...",
            "tunnel infini.png",
            new RoomRandomizer( this.aRooms )
        );
        this.aRooms.put( "teleporteur_aleatoire", vTransporter );

        // Création des passages bidirectionnels entre les salles
        Room.connectRooms(vNord, "sud", vMurNord, "nord");
        Room.connectRooms(vNord, "ouest", vOuest, "nord");
        Room.connectRooms(vNord, "est", vEst, "nord");
        Room.connectRooms(vEst, "sud", vSud, "est");
        Room.connectRooms(vEst, "ouest", vEscaliers, "est");
        Room.connectRooms(vSud, "nord", vPorte, "sud");
        Room.connectRooms(vSud, "ouest", vOuest, "sud");
        Room.connectRooms(vOuest, "est", vMurOuest, "ouest");
        Room.connectRooms(vEscaliers, "haut", vToitRuines, "bas");
        Room.connectRooms(vToitRuines, "haut", vArbre, "bas");
        Room.connectRooms(vPorte, "nord", vInterieur, "sud", vCle); // porte verrouillée avec la clé
        // Connexions trap-door
        Room.connectRooms(vToitRuines, "nord", vMurNord);
        Room.connectRooms(vToitRuines, "est", vEscaliers);
        Room.connectRooms(vToitRuines, "sud", vPorte);
        Room.connectRooms(vToitRuines, "ouest", vMurOuest);
        Room.connectRooms(vInterieur, "ouest", vSalleFinale, vRocher); // porte verrouillée avec le rocher

        // On peut accéder à vTransporter depuis vInterieur par le nord,
        // En revanche, toute sortie de vTransporter mène à une salle aléatoire.
        Room.connectRooms(vTransporter, "sud", vInterieur, "nord");
        Room.connectRooms(vTransporter, "nord", vInterieur);
        Room.connectRooms(vTransporter, "est", vInterieur);
        Room.connectRooms(vTransporter, "ouest", vInterieur);

        // Placement des Items dans les salles
        vSud.addItem(vTorche);
        vToitRuines.addItem(vLapin);
        vToitRuines.addItem(vTeleporteur);
        vArbre.addItem(vOiseau);
        vArbre.addItem(vPoussin);
        vArbre.addItem(vCle);
        vMurNord.addItem(vFiole);
        vMurNord.addItem(vLezard);
        vMurOuest.addItem(vCoccinelle);
        vMurOuest.addItem(vRocher);
        vEscaliers.addItem(vPapillon);
        vSalleFinale.addItem(vPyrotaris);
        
        // Salle de départ (sera assignée au joueur quand il sera créé)
        this.aStartRoom = vSud;
    } // createRooms
 
    /**
     * Crée une nouvelle salle, l'ajoute à la HashMap des salles et la retourne.
     *
     * @param pName le nom unique de la salle pour la HashMap
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
        if (this.aGameOver) {
            return;
        }

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
            case "alea"          -> bypassRandom(vCommand);
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
            this.aGui.showImage("game over.png");
            this.aGameOver = true;
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
        this.aGameOver = true;
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
            this.aGui.println("Il n'y a pas de tel objet ici.\n Attention aux accents et à la casse ...");
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
        if (vItem.getName().equals("pyrotaris")) {
            this.aGui.println(
                "\n=============== FÉLICITATIONS ===============\n" +
                "Vous avez trouvé le Pyrotaris, \n" +
                "l'artefact technologique Sheikah tant recherché !\n" +
                "Vous avez accompli votre mission avec succès.\n" +
                "Merci d'avoir joué à 'Le Mystère des Ruines Sheikah' !\n" +
                "=============================================\n");
            this.aGui.showImage("victoire.png");
            this.aGameOver = true;
            this.aGui.enable( false );
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
            this.aGui.println("Désolé mais t'as pas ça sur toi.");
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
            "votre taux d'oxygène a doublé, et avec cela votre force.\n" +
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
        
        if ( this.aPlayer.hasItem(vDoor.getKey().getName()) ) {
            vDoor.unlock();
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
        
        if ( this.aPlayer.hasItem(vDoor.getKey().getName()) ) {
            vDoor.lock();
            this.aGui.println("Vous avez verrouillé la porte " + vDirection + ".");
        } else {
            this.aGui.println("Vous n'avez pas la clé pour verrouiller cette porte.");
        }
    } // lockDoor(*)    

    /**
     * Impose une valeur fixe pour les prochains choix aléatoires de RoomRandomizer dans les tests.
     * 
     * @param pCommand la commande reçue (doit contenir le nom de la Room à forcer)
     */
    private void bypassRandom(final Command pCommand)
    {
        if (this.aDebugMode == false) {
            this.aGui.println("La commande 'alea' ne peut être utilisée que dans un fichier test.");
            return;
        }

        if ( ! pCommand.hasSecondWord() ) {
            this.aGui.println("Veuillez spécifier un nom de salle (ex: alea cuisine ).");
            return;
        }

        String vRoomName = pCommand.getSecondWord();
        Room vRoom = this.aRooms.get( vRoomName );
        if ( vRoom == null ) {
            this.aGui.println("Cette salle n'existe pas.");
            return;
        }
        ( (TransporterRoom) this.aRooms.get("teleporteur_aleatoire") ).setForcedRoom( vRoom );
    } // bypassRandom(*)

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
            this.aDebugMode = true;
            while ( vScanner.hasNextLine() ) { // tant qu'il y a encore une ligne à lire dans le fichier
                String vLigne = vScanner.nextLine(); // lecture de la ligne dans le fichier
                // ignorer les lignes qui commencent par # (commentaires) et les lignes vides
                if ( vLigne.startsWith( "#" ) || vLigne.trim().isEmpty() ) {
                    continue;
                }
                this.interpretCommand( vLigne );
            } // while
            this.aDebugMode = false;
            this.aGui.println("\n============= TEST terminé =============\n");
        } // try
        catch ( final FileNotFoundException pFNFE ) { // si le fichier n'existe pas
            this.aGui.println("Le fichier '" + pNomFichier + "' est introuvable.");
        } // catch
    } // executeTest(*)
}
