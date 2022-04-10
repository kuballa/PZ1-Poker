package server.pl.ziomek.poker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
    public static int liczba;
    private final ServerSocket serverSocket;
    public int numPlayersScanner;
    public int numPlayer;
    public Card[] hand;
    public int account = 100;
    public DeckOfCards deck = new DeckOfCards();

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    /**
     * What server do when started
     */
    public void startServer()
    {
            deck.shuffle(100);
            numPlayer = 0;
            System.out.println("---Game Server---");

            //Wpisywanie ilu chce się graczy, ale nie mniej niż 2 i nie więcej niż 4
            passNumberOfPlayers();

            System.out.println("Oczekiwanie na graczy...");

            try {
                while (numPlayer < numPlayersScanner) {
                    Socket socket = serverSocket.accept();
                    System.out.println("A new client has connected!");
                    randomNumberGenerator();

                    //wyświetlanie na serwerze tablicy 5 elementów
                    for (int i = 0; i < 5; i++) {
                        System.out.print(hand[i] + " ");
                    }
                    // tutaj przekazujemy numer gracza i tablice z wylosowanymi wartościami
                    ClientHandler clientHandler =
                            new ClientHandler(socket, hand, numPlayer, numPlayersScanner, account, deck);

                    Thread thread = new Thread(clientHandler);
                    thread.start();

                    numPlayer++;
                }
                System.out.println();
                System.out.println("We now have " + numPlayer + " players. No longer accepting connections");

            }catch(IOException e)
            {
                System.out.println("koniec dzialania");
            }
    }

    /**
     * each player get 5 random cards to his/her hand
     */
    public void randomNumberGenerator()
    {
        hand = new Card[5];
        System.out.println(deck);
        for(int i = 0; i < 5; i++)
        {
            hand[i] = deck.deal();
        }
    }

    /**
     * there is an secure to not input different value than 2,3,4
     */
    public void passNumberOfPlayers()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of players: ");
        numPlayersScanner = scanner.nextInt();

        while(numPlayersScanner > 4 || numPlayersScanner < 2)
        {
            System.out.println("Musisz ponownie podać liczbę graczy");
            System.out.println("Enter number of players: ");
            numPlayersScanner = scanner.nextInt();
        }
    }

    /**
     * main method in which works server
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        do {
            server.startServer();
        } while (liczba != 5);
    }
}