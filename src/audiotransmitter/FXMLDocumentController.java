/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotransmitter;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author nathanr
 */
public class FXMLDocumentController implements Initializable
{

    ClientTest clientTest = new ClientTest();
    Thread clientThread = new Thread(clientTest);

    ServerTest serverTest = new ServerTest();
    Thread serverThread = new Thread(serverTest);

    @FXML
    private Label errorLabel;

    @FXML
    private TextField serverIPTextField;
    @FXML
    private Label label;

    @FXML
    private Button sendAudioButton;

    @FXML
    private Button clientButton;

    @FXML
    private Button serverStartButton;

    @FXML
    private Button serverStopButton;

    @FXML
    private void startServer(ActionEvent event)
    {

        System.out.println("Server starting...");
        serverThread.start();
    }

    @FXML
    private void startClient(ActionEvent event)
    {

        if (serverIPTextField.getText() == null || serverIPTextField.getText().isEmpty() || serverIPTextField.getText().length() < 5)
        {
            try
            {
                String serverIp = InetAddress.getLocalHost().getHostAddress();
                System.out.println("this ip? " + serverIp);
                errorLabel.setText("Please enter the IP address of the server\nYour IP is: " + serverIp);
            } catch (UnknownHostException ex)
            {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        } else
        {
            errorLabel.setText("");
            System.out.println("Client starting...");

            clientTest.setServerAddress(serverIPTextField.getText());
            clientTest.initialize();
            new Thread(clientThread).start();
        }
    }

    @FXML
    private void stopClient(ActionEvent event)
    {
        stopClient();
    }

    private void stopClient()
    {
        System.out.println("Trying to stop Client...");
        clientTest.terminate();
        try
        {
            clientThread.join();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void stopServer(ActionEvent event)
    {
        stopServer();
    }

    private void stopServer()
    {

        System.out.println("Trying to stop Server...");
        serverTest.terminate();
        try
        {
            serverThread.join();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

    /**
     * method to exit the program.
     */
    @FXML
    public void doExit()
    {
        stopClient();
        stopServer();
        Platform.exit();
    }

}
