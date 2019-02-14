/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.init;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * FXML Controller class
 *
 * @author Henrique K. Secchi
 */
public class TelaInicialController implements Initializable {

    @FXML
    private ImageView imgnew;
    @FXML
    private ImageView imgopen;
    @FXML
    private ImageView imgsave;
    @FXML
    private ImageView imgcompile;
    @FXML
    private TabPane tabAnalyzes;
    @FXML
    private TabPane tabFiles;
    
    private ArrayList<Tab> listtabs;
    @FXML
    private Pane panenew;
    @FXML
    private Pane paneopen;
    @FXML
    private Pane panesave;
    @FXML
    private Pane panecompile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carragarImagens(1);
        
        listtabs = new ArrayList<Tab>();
    }    

    public void carragarImagens(int num)
    {
        double escala = 0.8;
        
        imgnew.setScaleX(escala);
        imgnew.setScaleY(escala);
        
        imgopen.setScaleX(escala);
        imgopen.setScaleY(escala);
        
        imgsave.setScaleX(escala);
        imgsave.setScaleY(escala);
        
        imgcompile.setScaleX(escala);
        imgcompile.setScaleY(escala);
        
        Image img = new Image("com/krupique/visualbeans/resources/new" + num + ".png");
        imgnew.setImage(img);
        img = new Image("com/krupique/visualbeans/resources/open" + num +".png");
        imgopen.setImage(img);
        img = new Image("com/krupique/visualbeans/resources/save" + num +".png");
        imgsave.setImage(img);
        img = new Image("com/krupique/visualbeans/resources/play" + num + ".png");
        imgcompile.setImage(img);
    }
    
    @FXML
    private void newFile(ActionEvent event) {
        Tab tab = new Tab("Sem título" + (listtabs.size() + 1));
        listtabs.add(tab);
        
        CodeArea code = new CodeArea();
        
        AnchorPane pane = new AnchorPane();
        tab.setContent(pane);
        pane.getChildren().add(configurarCodeArea(code));
        
        tabFiles.getTabs().add(tab);
    }

    public CodeArea configurarCodeArea(CodeArea code)
    {
        code.setPrefSize(1070, 336);
        code.setLayoutX(5);
        code.setLayoutY(5);
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        return code;
    }
    
    @FXML
    private void openFile(ActionEvent event) {
    }

    @FXML
    private void closeFile(ActionEvent event) {
        tabFiles.getTabs().remove(tabFiles.getSelectionModel().getSelectedItem());
        
    }

    @FXML
    private void saveFile(ActionEvent event) {
        tabFiles.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void saveAs(ActionEvent event) {
    }

    @FXML
    private void exitProgram(ActionEvent event) {
    }

    @FXML
    private void evtCompileExited(MouseEvent event) {
        panecompile.setStyle("-fx-background-color:white");
    }

    @FXML
    private void evtCompileEntered(MouseEvent event) {
        panecompile.setStyle("-fx-background-color:black");
    }

    @FXML
    private void evtNewExited(MouseEvent event) {
        panenew.setStyle("-fx-background-color:white");
    }

    @FXML
    private void evtNewEntered(MouseEvent event) {
        panenew.setStyle("-fx-background-color:black");
    }

    @FXML
    private void evtOpenExited(MouseEvent event) {
        paneopen.setStyle("-fx-background-color:white");
    }

    @FXML
    private void evtOpenEntered(MouseEvent event) {
        paneopen.setStyle("-fx-background-color:black");
    }

    @FXML
    private void evtSaveExited(MouseEvent event) {
        panesave.setStyle("-fx-background-color:white");
    }

    @FXML
    private void evtSaveEntered(MouseEvent event) {
        panesave.setStyle("-fx-background-color:black");
    }
}
