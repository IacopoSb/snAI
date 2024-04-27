package it.unibo.ai.didattica.competition.tablut.snai.client;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

import java.io.IOException;
import java.net.UnknownHostException;

public class Tablut extends TablutClient {
    public static final String TEAM_NAME = "snAI";

    private int game;
    private boolean debug;

    /**
     * Main constructor
     *
     * @param player    the side the player plays for [WHITE | BLACK]
     * @param name      the name of the team
     * @param timeout   the maximum number of seconds the player has to choose its next move
     * @param ipAddress the server address
     * @param game      the game rules [1 - BasicTablut | 2 - ModernTablut | 3 - BrandubTablut | <strong>4 - AshtonTablut</strong>]
     *
     * @throws UnknownHostException if the IP address of the host could not be determined.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public Tablut(String player, String name, int timeout, String ipAddress, int game) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
        this.game = game;
    }
    public Tablut(String player) throws IOException {
        super(player, TEAM_NAME, 60, "localhost");
        this.game = 4;
    }

    public static void main(String[] args) throws IOException {
        int gameType = 4;
        String role = "";
        String ip = "localhost";
        int timeout = 60;

        if(args.length != 3) {
            System.out.printf("Parameter error: \n" +
                "\tREQUIRED: <black|white> <timeout-in-seconds> <server-ip> \n");
            System.exit(1);
        } else {
            role = args[0].toLowerCase();
            try {
            timeout = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.printf("Parameter error: timeout must be an integer representing seconds\n" +
                "\\tREQUIRED: <black|white> <timeout-in-seconds> <server-ip> \\n");
            System.exit(1);
            }
            ip = args[2];
        }

        // presenting myself to the audience
        AsciiLogo();
        System.out.println("Team snAI\n\nMembers:\n\tMatteo Bostrenghi\n\tLeonardo Gennaioli\n\tIacopo Sbalchiero\n\tLorenzo Severini\n");
        System.out.println("Player:  " + role);
        System.out.println("Timeout: " + timeout + " secondi");
        System.out.println("Server:  " + ip);

        Tablut client = new Tablut(role, "Team " + TEAM_NAME, timeout, ip, gameType);
        client.run();
    }

    @Override
    public void run() {
        // present myself to the server
        try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        State state = new StateTablut();
        state.setTurn(State.Turn.WHITE); // WHITE makes the first move

        GameAshtonTablut tablut = new GameAshtonTablut(0, -1, "logs", "white_ai", "black_ai");

        while(true) {
            try {
                // Receive the state from the Server
                this.read();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // Update the state received
            state = this.getCurrentState();
            System.out.println(TEAM_NAME.toUpperCase() + ": new state");
            System.out.println(state.toString());

            // if i'm WHITE
            if(this.getPlayer().equals(State.Turn.WHITE)) {

                switch (state.getTurn()) {
                    case WHITE:
                        // my TURN
                        System.out.println(TEAM_NAME.toUpperCase() + ": thinking the next move");

                        Action a = findBestMove(tablut, state);

                        System.out.println(TEAM_NAME.toUpperCase() + ": move predicted: " + a.toString() + "\"");

                        try {
                            this.write(a);
                        } catch (ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case BLACK:
                        // opponent TURN
                        System.out.println(TEAM_NAME.toUpperCase() + ": waiting for the opponent...");
                        break;
                    case WHITEWIN:
                        // if WHITE win
                        System.out.println(TEAM_NAME.toUpperCase() + ": WIN");
                        System.exit(0);
                        break;
                    case BLACKWIN:
                        // if BLACK win
                        System.out.println(TEAM_NAME.toUpperCase() + ": LOSE");
                        System.exit(0);
                        break;
                    case DRAW:
                        System.out.println(TEAM_NAME.toUpperCase() + ": DRAW");
                        System.exit(0);
                        break;
                }
            }
            // i'm BLACK
            else {

                switch (state.getTurn()) {
                    case BLACK:
                        // my TURN
                        System.out.println(TEAM_NAME.toUpperCase() + ": thinking the next move...");

                        Action a = findBestMove(tablut, state);

                        System.out.println(TEAM_NAME.toUpperCase() + ": move predicted: " + a.toString() + "\"");

                        try {
                            this.write(a);
                        } catch (ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case WHITE:
                        // opponent TURN
                        System.out.println(TEAM_NAME.toUpperCase() + ": waiting for the opponent");
                        break;
                    case BLACKWIN:
                        // if WHITE win
                        System.out.println(TEAM_NAME.toUpperCase() + ": WIN");
                        System.exit(0);
                        break;
                    case WHITEWIN:
                        // if BLACK win
                        System.out.println(TEAM_NAME.toUpperCase() + ": LOSE");
                        System.exit(0);
                        break;
                    case DRAW:
                        System.out.println(TEAM_NAME.toUpperCase() + ": DRAW");
                        System.exit(0);
                        break;
                }

            }
        }
    }

    private Action findBestMove(GameAshtonTablut tablutGame, State state) {

        // TODO implement the search algorithm
        
        return null;
    }

    private static void AsciiLogo() {
        System.out.println(
        		        "                                                                \n"+
        				"                                                                \n"+
        				"                                                                \n"+
        				"                                                                \n"+
        				"                                                                \n"+
        				"                            @@@@@@@@%                           \n"+
        				"                      @@@@@@@@@@@@@@@@@@@@@                     \n"+
        				"                  @@@@@@@@@           @@@@@@@@////////////      \n"+
        				"                @@@@@@                    @@/////////////       \n"+
        				"               @@@@@                             //////         \n"+
        				"             @@@@@                              ////            \n"+
        				"             @@@@                            ////@@@            \n"+
        				"            @@@@@                         ////  @@@@            \n"+
        				"            @@@@@                      ///      @@@@            \n"+
        				"            @@@@@                  ///         @@@@@            \n"+
        				"             @@@@              ///             @@@@             \n"+
        				"            ////           ///               @@@@@/             \n"+
        				"          //////      ////                 *@@@@@               \n"+
        				"        //////////////@@                %@@@@@@                 \n"+
        				"       //////////   @@@@@@@@@@@@@@@@@@@@@@@@@                   \n"+
        				"                        @@@@@@@@@@@@@@@@@                       \n"+
        				"                                                                \n"+
        				"                                                                \n"+
        				"                                                                \n"+
        				"                                                                \n");
        }
}

