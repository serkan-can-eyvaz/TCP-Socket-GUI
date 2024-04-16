package tcpserver;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TCPServer extends JFrame {
    private static ServerSocket serverSocket = null;
    private JTextArea messageArea;

    public TCPServer() {
        super("TCP Sunucu");
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TCPServer().startServer();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(1234);
            appendMessage("Sunucu TCP Soketi oluşturuldu. Bağlantı bekleniyor...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                appendMessage(clientSocket.toString() + " bağlandı.");
                handleClient(clientSocket);
            }
        } catch (IOException ex) {
            appendMessage("Bağlantı hatası: " + ex.getMessage());
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException ex) {
                appendMessage("Bağlantı kapatma hatası: " + ex.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner input = new Scanner(clientSocket.getInputStream());

            while (input.hasNextLine()) {
                String receivedMessage = input.nextLine();
                appendMessage(clientSocket.getInetAddress().getHostName() + " istemci: " + receivedMessage);
                out.println(receivedMessage.toUpperCase());
            }

            out.close();
            input.close();
            clientSocket.close();
        } catch (IOException ex) {
            appendMessage("İstemci işleme hatası: " + ex.getMessage());
        }
    }

    private void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }
}
