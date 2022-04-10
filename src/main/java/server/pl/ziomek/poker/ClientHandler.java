package server.pl.ziomek.poker;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable
{
    /**
     * Initially, these are used to change cards in your hand, for example
     */
    ArrayList<Integer> zamiana = new ArrayList<>();  //lista używana do sprawdzania czy się wymieniło liczbę
                                                            // na danym miejscu
    public static int turnsMadeObst; // int używany do turowania graczy
    public int numPlayerScanner;
    private int numPlayer;
    private Card[] hand = new Card[5];
    public static DeckOfCards deck;

    /**
     * variables for handling the server and what the client says
     */
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    /**
     * variables that are used when placing bets
     */
    public static ArrayList<Integer> accountPlayers = new ArrayList<>();
    public static ArrayList<Integer> tabPass = new ArrayList<>();
    public static ArrayList<Integer> tabBet = new ArrayList<>();
    public static int jackpot;
    public static int currentBet;
    public static boolean endOfBet = false;
    public static int turnsMadeBet;
    /**
     * checking which player won the round
     */
    public static int[] handValue;
    /**
     * rounds
     */
    public static int round;

    /**
     * constructor for the ClientHandler class
     * @param socket
     * @param hand
     * @param numPlayer
     * @param numPlayersScanner
     * @param account
     * @param deck
     */
    public ClientHandler(@NotNull Socket socket, Card[] hand, int numPlayer, int numPlayersScanner, int account, DeckOfCards deck)
    {
        try {
            numPlayerScanner = numPlayersScanner;
            this.numPlayer = numPlayer;
            this.hand = hand;
            ClientHandler.deck = deck;

            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);

            accountPlayers.add(account);
            tabPass.add(0);
            tabBet.add(0);
            currentBet = 10;
            handValue = new int[numPlayersScanner];

            broadcastMessagePeople("SERVER: " + clientUsername + " dołaczył do gry");
            // pokazywanie że dany gracz ma liczby o takich wartościach
            broadcastMessagePerson("Klient " + clientUsername + " ma karty " + Arrays.toString(hand));
            if(numPlayersScanner == numPlayer+1)
            {
                broadcastMessagePeople("Rozpoczynamy gre, wpisz (help) żeby zobaczyć jakie komendy wpisać");
                broadcastMessagePerson("Rozpoczynamy gre, wpisz (help) żeby zobaczyć jakie komendy wpisać");
            }
            else
            {
                broadcastMessagePeople("czekamy dalej");
                broadcastMessagePerson("czekamy dalej");
            }
        }
        catch(IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * message from the client and sent to the server
     */
    @Override
    public void run()
    {
        String messageFromClient;
        while(socket.isConnected()) {
            try {
                if (turnsMadeObst < numPlayerScanner) {
                    messageFromClient = bufferedReader.readLine();
                    if (messageFromClient.equals("help")) {
                        helpMessageShow();
                    }
                    if(messageFromClient.equals("hand")){
                        showHand();
                    }
                    if(messageFromClient.equals("account")){
                        showAccount();
                    }
                    if (messageFromClient.matches("[1-5]")) {
                        whichNumberYouWantToChange(messageFromClient);
                    }
                    if (messageFromClient.equals("end")) {
                        endChangeOfDeck();
                    }
                }
                if (turnsMadeObst == numPlayerScanner) {
                    writeAMessage();
                    turnsMadeObst++;
                }
                String messageFromClient1 = bufferedReader.readLine();
                if (turnsMadeObst > numPlayerScanner && messageFromClient1.equals("zacz")) {
                    broadcastMessagePerson("chce grac");
                    while (true) {
                        game(messageFromClient1);
                        if (endOfBet) {
                            break;
                        }
                    }
                    jackpotAdder();
                    accountSubstract();
                    pokerValue();
                    checkTheCard();
                    if(round == 2){
                        endOfTheRound();
                        accountPlayers.clear();
                        tabPass.clear();
                        tabBet.clear();
                        round = 0;
                    }
                }
            }
            catch(IOException e)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    /**
     * show the current player what happened
     * @param messageToSend
     */
    public void broadcastMessagePerson(String messageToSend)
    {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (clientHandler.clientUsername.equals(clientUsername))
                {
                    clientHandler.bufferedWriter.write(messageToSend + " --- " + clientHandler.numPlayer);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void showAccount()
    {
        broadcastMessagePerson(String.valueOf(accountPlayers));
    }

    /**
     * display what has happened to everyone except the current player
     * @param messageToSend
     */
    public void broadcastMessagePeople(String messageToSend)
    {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername))
                {
                    clientHandler.bufferedWriter.write(messageToSend + " --- " + clientHandler.numPlayer);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    /**
     * replacement of the card indicated by the player
     * @param number
     */
    public void changeNumber(int number)
    {
        hand[number] = deck.deal();
    }

    /**
     * a message by which players are informed that bets are starting
     */
    public void writeAMessage() {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(clientUsername)) {
                broadcastMessagePerson("rozpoczynamy zakłady (zacz)");
                broadcastMessagePeople("rozpoczynamy zakłady (zacz)");
            }
        }
    }

    /**
     * the message that the game is over
     * @throws IOException
     */
    public void endOfTheRound() throws IOException {
        broadcastMessagePerson("koniec gry");
        broadcastMessagePeople("koniec gry");
        int kon = accountPlayers.indexOf(Collections.max(accountPlayers));
        broadcastMessagePerson("wygrał gracz nr " + kon);
        broadcastMessagePeople("wygrał gracz nr " + kon);
    }

    /**
     * the method in which the cards in his hand are displayed
     */
    public void showHand(){
        broadcastMessagePerson("Klient " + clientUsername + " dostal karty " + Arrays.toString(hand));
    }

    /**
     * a method in which, by entering the number, we change the cards we have in our hands
     * @param messageFromClient
     */
    public void whichNumberYouWantToChange(String messageFromClient)
    {
        for (ClientHandler clientHandler : clientHandlers){
            if(clientHandler.clientUsername.equals(clientUsername) && clientHandler.numPlayer == turnsMadeObst){
                int number = Integer.parseInt(messageFromClient);
                if (zamiana.contains(number)) {
                    broadcastMessagePerson("Nie możesz już zmienić liczby w tym miejscu");
                } else {
                    changeNumber(number - 1);
                    zamiana.add(number);
                    broadcastMessagePerson("Tablica po zamianie " + Arrays.toString(hand));
                    broadcastMessagePerson(String.valueOf(turnsMadeObst));
                }
                break;
            }
        }
    }

    /**
     * shows what we can do when replacing cards
     */
    public void helpMessageShow()
    {
        broadcastMessagePerson(
                "\n[1-5] - wymień" +
                "\nend - kiedy jesteś gotowy na obstawianie wartości" +
                "\naccount - zobaczyc aktualny stan konta" +
                "\nhand - zobaczyc swoje karty");
    }

    /**
     * end of the turn to replace a player
     */
    public void endChangeOfDeck()
    {
        for (ClientHandler clientHandler : clientHandlers){
            if(clientHandler.clientUsername.equals(clientUsername)) {
                turnsMadeObst++;
                broadcastMessagePerson(String.format("ile osób zrobiło zamiane kart %d wszystkich osób %d"
                                                    , turnsMadeObst, numPlayerScanner));
                broadcastMessagePeople("Klient o numerze " + clientHandler.numPlayer + " skończył ture");
            }
        }
    }

    /**
     * showing what and how values when betting
     */
    public void showWhenCardsAreChanged()
    {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(clientUsername)) {
                broadcastMessagePerson("obstawione wartości: " + tabBet +
                        " kto pass: " + tabPass +
                        " aktualna stawka: " + currentBet);
            }
        }
    }

    /**
     * when a player bids his bet status is set to the current stake
     */
    public void licytowac()
    {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(clientUsername) && clientHandler.numPlayer == turnsMadeBet) {
                tabBet.set(clientHandler.numPlayer, currentBet);
            }
        }
    }

    /**
     * just a raise
     * @param messageFromClient
     */
    public void raiseTheBet(String messageFromClient)
    {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(clientUsername) && clientHandler.numPlayer == turnsMadeBet) {
                int add = Integer.parseInt(messageFromClient);
                currentBet += add;
                tabBet.set(clientHandler.numPlayer, currentBet);
            }
        }
    }

    /**
     * just pass
     */
    public void pass()
    {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(clientUsername) && clientHandler.numPlayer == turnsMadeBet) {
                tabPass.set(clientHandler.numPlayer, 1);
            }
        }
    }

    /**
     * check if you can now check who won
     */
    public void checkEndOfBet()
    {
        int liczba = 0;
        for(int i = 0; i < numPlayerScanner; i++) {
            if (tabPass.get(i) == 1) {
                liczba++;
            }else if(tabBet.get(i) == currentBet){
                liczba++;
            }
        }
        if(liczba == numPlayerScanner){
            endOfBet = true;
        }
    }

    /**
     * logic when betting values
     * @param messageFromClient1
     * @throws IOException
     */
    public void game(String messageFromClient1) throws IOException
    {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(clientUsername) && clientHandler.numPlayer == turnsMadeBet
                    && messageFromClient1.equals("zacz") && tabPass.get(clientHandler.numPlayer) != 1) {

                broadcastMessagePerson("chcesz licytowac(lic) czy pass(pass)");
                String messageFromClient;
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient.equals("lic")) {
                    licytowac();
                    broadcastMessagePerson("czy chcesz podbic? (y/n)");
                    messageFromClient = bufferedReader.readLine();
                    if (messageFromClient.equals("y")) {
                        broadcastMessagePerson("o ile? (2/10/100)");
                        messageFromClient = bufferedReader.readLine();
                        if (messageFromClient.matches("2|10|100")) {
                            raiseTheBet(messageFromClient);
                            broadcastMessagePerson("dodawanie wartosci");
                        }
                    } else if (messageFromClient.equals("n")) {
                        broadcastMessagePerson("nie chce podbijac");
                        checkEndOfBet();
                    }
                    showWhenCardsAreChanged();
                    turnsMadeBet++;
                } else if (messageFromClient.equals("pass")) {
                    broadcastMessagePerson("chce zpasowac");
                    pass();
                    turnsMadeBet++;
                    showWhenCardsAreChanged();
                    checkEndOfBet();
                }
            }
            if (turnsMadeBet == numPlayerScanner) {
                turnsMadeBet = 0;
            }
        }
    }

    /**
     * adding value to a pool
     */
    public void jackpotAdder(){
        for(int i = 0; i < numPlayerScanner; i++){
            jackpot += tabBet.get(i);
        }
    }

    /**
     * subtracting values from players' accounts
     */
    public void accountSubstract(){
        for(int i = 0; i < numPlayerScanner; i++){
            int liczba1 = tabBet.get(i);
            int liczba2 = accountPlayers.get(i);
            accountPlayers.set(i, (liczba2 - liczba1/2));
        }
    }

    /**
     * Inserting a value of cards in a hand for each player
     */
    public void pokerValue()
    {
        for (ClientHandler clientHandler : clientHandlers){
            if(clientHandler.clientUsername.equals(clientUsername) && checkNotZero(handValue, tabPass) != numPlayerScanner){
                handValue[clientHandler.numPlayer] = Poker.valueHand(clientHandler.hand);
                broadcastMessagePerson(Arrays.toString(handValue));
            }
        }
    }
    /**
     * checking who won
     */
    public void checkTheCard()
    {
        int win = max(handValue, tabPass);
        int key = maxIndex(handValue, tabPass);
        broadcastMessagePerson(String.valueOf(currentBet));
        int dodatek = currentBet*numPlayerScanner;

        for (ClientHandler clientHandler : clientHandlers) {
            if(clientHandler.clientUsername.equals(clientUsername)
                    && clientHandler.numPlayer == key
                    && checkNotZero(handValue, tabPass) == numPlayerScanner) {
                broadcastMessagePerson("Wygrałeś zakład");
                broadcastMessagePerson(showPokerHandsValue(win));
                broadcastMessagePeople(showPokerHandsValue(win));
                int wygrana = accountPlayers.get(clientHandler.numPlayer);
                accountPlayers.set(clientHandler.numPlayer, wygrana + dodatek);
                broadcastMessagePeople("Przegrałeś zakład");
                deck.shuffle(1000);
                round++;
                setToZeroElements();
            }
        }
    }

    /**
     * setting the value to zero in the ArrayList
     * @param values
     */
    public void toZero(ArrayList<Integer> values)
    {
          for(int i = 0; i < values.size(); i++) {
            values.set(i,0);
          }
    }

    public void toZeroArray(int[] arr)
    {
        for(int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }

    /**
     * after giving the result, resetting the value so that the next round could be played
     */
    public void setToZeroElements()
    {
        toZeroArray(handValue);
        turnsMadeBet = 0;
        endOfBet = false;
        currentBet = 10;
        jackpot = 0;
        toZero(tabPass);
        toZero(tabBet);
        turnsMadeObst = 0;
        toZero(zamiana);
        for (ClientHandler clientHandler : clientHandlers){
//            if(clientHandler.clientUsername.equals(clientUsername)){
                for(int i = 0; i < 5; i++){
                    clientHandler.hand[i] = deck.deal();
                }
//            }
        }
    }

    /**
     * finding maximum value in an array
     * @param t
     * @return
     */
    public int max(int[] t, ArrayList<Integer> pass){
        int maximum = t[0];   // start with the first value
        for (int i=0; i<numPlayerScanner; i++) {
            if(pass.get(i) != 1) {
                if (t[i] > maximum) {
                    maximum = t[i];   // new maximum
                }
            }
        }
        return maximum;
    }

    /**
     * find the index of which is the maximum value in the array
     * @param t, pass
     * @return index
     */
    public int maxIndex(int[] t, ArrayList<Integer> pass){
        int maximum = t[0];// start with the first value
        int index = 0;
        for (int i=0; i<numPlayerScanner; i++) {
            if(pass.get(i) == 0) {
                if (t[i] > maximum) {
                    maximum = t[i];   // new maximum
                    index = i;        // new maximum index
                }
            }
        }
        return index;
    }

    /**
     * checking if the checkTheCard () function can tell who won
     * @param t, pass
     * @return
     */
    public int checkNotZero(int[] t, ArrayList<Integer> pass){
        int zera = 0;
        for (int i = 0; i < t.length; i++) {
            int j = t[i];
            int k = 0;
            if(i < numPlayerScanner) {
                k = pass.get(i);
            }
            if (j != 0) {
                zera++;
            } else if ( k == 1){
                zera++;
            }
        }
        return zera;
    }

    /**
     * removing the client when it disconnects from the server
     */
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessagePeople("SERVER: " + clientUsername + " wyszedł z serwera");
    }
    /**
    * tells name of a winners' poker hand
    */
    public String showPokerHandsValue(int win){
        if(win > 8000000){
            return("Zwycięsca ma: pokera");
        }else if(win > 7000000){
            return("Zwycięsca ma: karete");
        }else if(win > 6000000){
            return("Zwycięsca ma: fulla");
        }else if(win > 5000000){
            return("Zwycięsca ma: kolor");
        }else if(win > 4000000){
            return("Zwycięsca ma: strita");
        }else if(win > 3000000){
            return("Zwycięsca ma: trójke");
        }else if(win > 2000000){
            return("Zwycięsca ma: dwie pary");
        }else if(win > 1000000){
            return("Zwycięsca ma: jedną pare");
        }else if(win < 1000000) {
            return ("Zwycięsca ma: wysoką karte");
        }
        return "błąd";
    }

    /**
     * just shutting down everything
     * @param socket
     * @param bufferedReader
     * @param bufferedWriter
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}