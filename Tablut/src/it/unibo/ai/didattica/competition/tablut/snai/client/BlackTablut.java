package it.unibo.ai.didattica.competition.tablut.snai.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class BlackTablut {

    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
        String[] arguments = new String[]{"BLACK", "60", "localhost"};

        if(args.length > 0) {
            arguments = new String[]{"BLACK", args[0]};
        }

        Tablut.main(arguments);
    }
}
