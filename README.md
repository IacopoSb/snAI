# snAI - supernatural AI Player for Tablut Challenge 23 - 24

![](snAI.png)

## Team

- [Matteo Bostrenghi](https://github.com/Bostre17)
- [Leonardo Gennaioli](https://github.com/leonardo-gennaioli)
- [Iacopo Sbalchiero](https://github.com/IacopoSb)
- [Lorenzo Severini](https://github.com/lorenzoseverini1)

## English 🇬🇧

### The Project

*snAI* is a repository containing an artificial intelligence project developed for participating in the Tablut Challenge 23 - 24. This project provides an AI player specifically designed to compete in the Tablut Challenge organized for the course of [Foundations Of Artificial Intelligence T](https://www.unibo.it/en/study/phd-professional-masters-specialisation-schools-and-other-programmes/course-unit-catalogue/course-unit/2023/468002) @ [UniBo](https://www.unibo.it/en).

### How to Run

To invoke the Tablut AI player on a Linux system, follow these steps:

1. Clone the repository: `git clone https://github.com/IacopoSb/snAI`
2. Execute the following command:
   ```
   ./runmyplayer.sh <role> <time> <ip>
   ```

   Replace `<role>` with either "white" or "black", `<time>` with the desired time limit in seconds (60 seconds recommended), and `<ip>` with the IP address of the machine where the Tablut server is running.

### Additional Options

You can also use the provided JAR files from the command line located in the `/Tablut/jars` directory. Invoke them using `java -jar FILENAME PARAMETERS`. The available JAR files are:

- `snai.jar` (with the same parameters as the base case)
- `snaiwhite.jar` (parameters: time, ip)
- `snaiblack.jar` (parameters: time, ip)

The last two launch the player of that color, so it isn't necessary in the parameters.

*Any references to betting websites is **not** purely coincidental.*

## Italiano 🇮🇹

### Il Progetto

*snAI* è un repository contenente un progetto di intelligenza artificiale sviluppato per partecipare alla Tablut Challenge 23 - 24. Questo progetto fornisce un giocatore IA appositamente progettato per competere nella Tablut Challenge organizzata nel corso di [Fondamenti di Intelligenza Artificiale M](https://www.unibo.it/it/studiare/dottorati-master-specializzazioni-e-altra-formazione/insegnamenti/insegnamento/2023/468002) @ [UniBo](https://www.unibo.it/it).

### Come Eseguirlo

Per invocare il giocatore IA di Tablut su un sistema Linux, segui questi passaggi:

1. Clone il repository: `git clone https://github.com/IacopoSb/snAI`
2. Esegui il seguente comando:
   ```
   ./runmyplayer.sh <role> <time> <ip>
   ```

   Sostituisci `<role>` con "white" o "black", `<time>` con il limite di tempo desiderato in secondi (si consigliano 60 secondi) e `<ip>` con l'indirizzo IP della macchina dove è in esecuzione il server Tablut.

### Opzioni Aggiuntive

È possibile utilizzare anche i file JAR forniti dalla linea di comando presenti nella cartella `/Tablut/jars`. Invocali utilizzando `java -jar NOMEFILE PARAMETRI`. I file JAR disponibili sono:

- `snai.jar` (con gli stessi parametri del caso base)
- `snaiwhite.jar` (parametri: tempo, IP)
- `snaiblack.jar` (parametri: tempo, IP)

Gli ultimi due lanciano il giocatore di quel colore, quindi non è necessario nei parametri.

*Ogni riferimento a siti di scommesse **non** è puramente casuale.*
