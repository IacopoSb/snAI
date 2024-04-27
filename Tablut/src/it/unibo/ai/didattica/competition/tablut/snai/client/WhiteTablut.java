package it.unibo.ai.didattica.competition.tablut.snai.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class WhiteTablut {

    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
        String[] arguments = new String[]{"WHITE", "60", "localhost"};

        if(args.length > 0) {
            arguments = new String[]{"WHITE", args[0]};
        }

        Tablut.main(arguments);
    }
}
