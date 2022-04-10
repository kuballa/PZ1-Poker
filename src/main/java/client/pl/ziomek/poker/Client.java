package client.pl.ziomek.poker;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client
{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    /**
     * constructor for the Client class
     * @param socket
     * @param username
     */
    public Client(Socket socket, String username)
    {
        try
        {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }
        catch(IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * this function connects to the ClientHandler and allows you to communicate with the server
     */
    public void sendMessage()
    {
        try
        {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected())
            {
                String messageToSend = scanner.nextLine();
                //odczytywanie co użytkownik wpisał na klawiaturze i przekazanie tego do clientHandler
                if(messageToSend.equals("help")) {
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("account")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("hand")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.matches("[1-5]")) {
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("end")) {
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("lic")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("pass")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("y")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("n")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.equals("zacz")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                if(messageToSend.matches("2|10|100")){
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        }
        catch(IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * a method that listens to whether the Client class sends something to the ClientHandler
     */
    public void listenForMessage()
    {
        new Thread(() -> {
            String msgFromGroupChat;

            while(socket.isConnected())
            {
                try
                {
                    msgFromGroupChat = bufferedReader.readLine();
                    System.out.println(msgFromGroupChat);
                }
                catch(IOException e)
                {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }).start();
    }

    /**
     * on shutdown, close methods bufferedReader, bufferedWriter, socket
     * @param socket
     * @param bufferedReader
     * @param bufferedWriter
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try
        {
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * main method in which everything takes place related to the client
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wpisz swój nick dla gry poker: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}