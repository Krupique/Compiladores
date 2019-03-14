/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.init;

import com.jfoenix.controls.JFXTextArea;
import com.krupique.visualbeans.analysis.Lexica;
import com.krupique.visualbeans.analysis.Sintatica;
import com.krupique.visualbeans.utils.Colors;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    @FXML
    private Pane panenew;
    @FXML
    private Pane paneopen;
    @FXML
    private Pane panesave;
    @FXML
    private Pane panecompile;
    @FXML
    private BorderPane panePrincipal;
    @FXML
    private AnchorPane paneButtons;
    @FXML
    private AnchorPane ancAnalyzes;
    @FXML
    private AnchorPane ancTabTokens;
    @FXML
    private TabPane tabTokens;
    @FXML
    private JFXTextArea textLexico;

    private ArrayList<Tab> listtabs;
    private int flagTheme;
    private String corTheme;
    
    private String tecla;
    private String textoLexico;
    private Lexica lexica;
    private String string;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carragarImagens(1);
        carregarCss();
        
        string = "";
        listtabs = new ArrayList<Tab>();
    }    
    
    public void carregarCss()
    {
        flagTheme = 1;
        /*if(flagTheme == 1)
            corTheme = "";
        else if(flagTheme == 2)
            corTheme = "";
        else if(flagTheme == 3)
            corTheme = "";*/
        corTheme = "#777777";
        panePrincipal.getStylesheets().add("com/krupique/visualbeans/styles/main_theme.css");
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
        newTabFile();
    }
    
    public void newTabFile()
    {
        Tab tab = new Tab("Sem título" + (listtabs.size() + 1));
        listtabs.add(tab);
        
        CodeArea code = new CodeArea();
        code.setOnKeyTyped(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event) {
                completaTexto(event);
            }
        });
        
        Colors cr = new Colors(code);
        code = cr.getCodeArea();
        
        
        AnchorPane pane = new AnchorPane();
        tab.setContent(pane);
        pane.getChildren().add(configurarCodeArea(code));
        
        tabFiles.getTabs().add(tab);
    }

    public CodeArea configurarCodeArea(CodeArea code)
    {
        code.setPrefSize(770, 336);
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
        double escala = 0.8;
        imgcompile.setScaleX(escala);
        imgcompile.setScaleY(escala);
        panecompile.setStyle("-fx-background-radius:7px;");
    }

    @FXML
    private void evtCompileEntered(MouseEvent event) {
        double escala = 1;
        imgcompile.setScaleX(escala);
        imgcompile.setScaleY(escala);
        panecompile.setStyle("-fx-background-color:" + corTheme
                           + ";-fx-background-radius:7px;");
    }

    @FXML
    private void evtNewExited(MouseEvent event) {
        double escala = 0.8;
        imgnew.setScaleX(escala);
        imgnew.setScaleY(escala);
        panenew.setStyle("-fx-background-radius:7px;");
    }

    @FXML
    private void evtNewEntered(MouseEvent event) {
        double escala = 1;
        imgnew.setScaleX(escala);
        imgnew.setScaleY(escala);
        panenew.setStyle("-fx-background-color:" + corTheme
                       + ";-fx-background-radius:7px;");
    }

    @FXML
    private void evtOpenExited(MouseEvent event) {
        double escala = 0.8;
        imgopen.setScaleX(escala);
        imgopen.setScaleY(escala);
        paneopen.setStyle("-fx-background-radius:7px;");
    }

    @FXML
    private void evtOpenEntered(MouseEvent event) {
        double escala = 1;
        imgopen.setScaleX(escala);
        imgopen.setScaleY(escala);
        paneopen.setStyle("-fx-background-color:" + corTheme
                        + ";-fx-background-radius:7px;");
    }

    @FXML
    private void evtSaveExited(MouseEvent event) {
        double escala = 0.8;
        imgsave.setScaleX(escala);
        imgsave.setScaleY(escala);
        panesave.setStyle("-fx-background-radius:7px;");
    }

    @FXML
    private void evtSaveEntered(MouseEvent event) {
        double escala = 1;
        imgsave.setScaleX(escala);
        imgsave.setScaleY(escala);
        panesave.setStyle("-fx-background-color:" + corTheme
                        + ";-fx-background-radius:7px;");
    }

    @FXML
    private void evtNewFile(MouseEvent event) {
        newTabFile();
    }

    @FXML
    private void evtOpenFile(MouseEvent event) {
    }

    @FXML
    private void evtSaveFile(MouseEvent event) {
    }

    @FXML
    private void evtCompileFile(MouseEvent event) {
        AnchorPane p = (AnchorPane)tabFiles.getSelectionModel().getSelectedItem().getContent();
        CodeArea c = (CodeArea)p.getChildren().get(0);
       
        Object[] obj = new Object[3];
        //lexica = new Lexica(c.getText());
        //lexica.gerarAnalise();
        
        Sintatica sintatica = new Sintatica(c.getText());
        obj = sintatica.analisar();
        //System.out.println("Foi por favor: " + c.getText());
        //textoLexico = 
        textLexico.setText((String)obj[0]);
        c.replaceText((String)obj[2]);
        Colors cor = new Colors(c);
        //System.out.println("" + (String)obj[2]);
    }
    
    public void completaTexto(KeyEvent event)
    {
        AnchorPane p = (AnchorPane)tabFiles.getSelectionModel().getSelectedItem().getContent();
        CodeArea c = (CodeArea)p.getChildren().get(0);
        
        //System.out.println(event.getCharacter());
        string += event.getCharacter();
        if(event.getCharacter().charAt(0) == 13)
        {
            string += '\n';
        }
    }
}
