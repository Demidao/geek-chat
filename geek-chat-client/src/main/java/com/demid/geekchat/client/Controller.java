package com.demid.geekchat.client;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable { // управляет  сетью, реализует  Инициалайз для преднастроек работы
    private Network network;
    @FXML
    TextField msgField;
    @FXML
    TextArea mainArea;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network = new Network((args -> {
            mainArea.appendText((String) args[0] + "\n");
        })); // тут мы подключаемся сразу по сети к серваку. Не очень хорошо, нужно через "кнопку" в интерфейсе.
    }


    public void sendMessageAction(ActionEvent actionEvent) {
        network.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }

    public void exitAction(ActionEvent actionEvent) {
        network.close();
        Platform.exit();
    }


}
