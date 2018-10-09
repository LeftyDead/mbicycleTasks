package bel.l4d.task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EchoServer {

    private static final int MAX_CONNECTIONS = 100;
    private ServerSocket echoServer = null;
    private int activeConnections = 0;
    private boolean isServerUp = false;

    ArrayList<Socket> clientSockets;

    public EchoServer() throws Exception {

        try {
            echoServer = new ServerSocket(8020);
        } catch (IOException ioe) {
            throw new Exception(ioe.getMessage());
        }

        clientSockets = new ArrayList<>();

        isServerUp = true;

        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            new Thread(() -> {
                listeningAndEchoing();
            }).start();
        }
    }

    private void listeningAndEchoing() {

        while(isServerUp) {

            try (
                    Socket clientSocket = echoServer.accept();
                    PrintStream out = new PrintStream(clientSocket.getOutputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {

                synchronized (this) {
                    clientSockets.add(clientSocket);
                    activeConnections++;
                }

                Supplier<String> supplier_SocketReader = () -> {
                    try {
                        return br.readLine();
                    } catch (IOException ex) {
                        if (isServerUp) {
                            if(ex.getMessage().equals("Connection reset")) {    //User (client) disconnected
                                synchronized (this) {
                                    activeConnections--;
                                }
                            }
                        }
                        return null;
                    }
                };

                Stream<String> stream_Echo = Stream.generate(supplier_SocketReader);
                stream_Echo.map(s -> {
                    if (isServerUp) {
                        out.println(s);     //Echo (s = Client String)
                    }
                    return s;
                })
                        .allMatch(s -> s != null);
            } catch (Exception e) {
                if (isServerUp) {
                    throw new IllegalArgumentException(e.getMessage());     //Something went wrong
                }
            }
        }
    }

    public void closeServer() throws Exception {

        isServerUp = false;

        for (int i = 0; i < activeConnections; i++) {
            try {
                if (clientSockets.get(i) != null ) {
                    clientSockets.get(i).close();
                }
            } catch (IOException e) {
                throw new Exception("Cannot close connection with client: " + e.getMessage());
            }
        }

        try {
            echoServer.close();
        } catch (IOException ioe) {
            throw new Exception("Cannot close Socket Server: " + ioe.getMessage());
        }
    }
}