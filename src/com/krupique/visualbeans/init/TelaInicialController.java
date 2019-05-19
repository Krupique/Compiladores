/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.init;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.krupique.visualbeans.analysis.Lexica;
import com.krupique.visualbeans.analysis.Sintatica;
import com.krupique.visualbeans.structures.TabelaTokens;
import com.krupique.visualbeans.utils.Colors;
import com.krupique.visualbeans.utils.Erros;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
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
    @FXML
    private TableColumn<String, String> clPalavra;
    @FXML
    private TableColumn<String, String> clToken;
    @FXML
    private TableView<TabelaTokens> tbvTokens;
    @FXML
    private JFXTextArea textSintatico;
    @FXML
    private AnchorPane ancPrincipal;
    @FXML
    private TableColumn<String, String> clCategoria;
    @FXML
    private TableColumn<String, String> clTipo;
    @FXML
    private TableColumn<String, String> clValor;
    @FXML
    private JFXTextArea textSemantic;
    @FXML
    private JFXTextArea textIntermed;
    @FXML
    private JFXCheckBox ckExibir;
    
    
    private ArrayList<Tab> listtabs;
    private int flagTheme;
    private String corTheme;
    private String tecla;
    private String textoLexico;
    private Lexica lexica;
    private String string;
    private String stropen;
    private ArrayList<TabelaTokens> tabela;
    Object[] obj;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciarTableview();
        carregarImagens(1);
        carregarCss();
        
        string = "";
        listtabs = new ArrayList<Tab>();
        obj = new Object[5];
    }    
    
    private void iniciarTableview()
    {
        clPalavra.setCellValueFactory(new PropertyValueFactory<>("palavra"));
        clToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        clCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        clTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        clValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
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
        
        tbvTokens.getStylesheets().add("com/krupique/visualbeans/styles/table_view.css");
    }

    public void carregarImagens(int num)
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
        newTabFile("");
    }
    
    public void newTabFile(String str)
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
        Colors cr;
        if(str.equals(""))
            cr = new Colors(code);
        else
            cr = new Colors(code, str);
            
        
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
        Properties prop = System.getProperties();
        FileChooser fc = new FileChooser();
        
        if(prop.getProperty("os.name").toLowerCase().contains("windows"))
            fc.setInitialDirectory(new File("C:/"));
        else
            fc.setInitialDirectory(new File("/home"));
        
        File arq = fc.showOpenDialog(null);
        if(arq != null)
        {
            try
            {
                String line;
                BufferedReader br = new BufferedReader(new FileReader(arq));
                stropen = "";
                while(br.ready())
                {
                    line = br.readLine();
                    if(line.length() > 0)
                        stropen += line + "\n";
                }
                
                newTabFile(stropen);
                //System.out.println(stropen);
            }catch(Exception er){
                Alert a = new Alert(Alert.AlertType.ERROR, "Erro ao abrir programa!", ButtonType.OK);
                a.showAndWait();
            }
        }
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
        newTabFile("");
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
       
        if(c.getText().length() > 0)
        {
            

            Sintatica sintatica = new Sintatica(c.getText());
            obj = sintatica.analisar();

            setTextIntermed((ArrayList<TabelaTokens>)obj[4]);
            textLexico.setText((String)obj[0]);
            adicionarAnaliseSintatica((ArrayList<TabelaTokens>)obj[1]);
            adicionarNoTableview((ArrayList<TabelaTokens>)obj[1]);
        }
        else
        {
            Alert a = new Alert(Alert.AlertType.ERROR, "Não há texto para ser compilado! Por favor digite algum texto no campo de texto!", ButtonType.OK);
            a.showAndWait();
        }
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

    private void adicionarNoTableview(ArrayList<TabelaTokens> list) {
        ObservableList<TabelaTokens> obs;
        obs = FXCollections.observableArrayList(list);
        
        tbvTokens.setItems(obs);
    }

    private void adicionarAnaliseSintatica(ArrayList<TabelaTokens> list) {
        String strsint = "";
        String strseman = "";
        AnchorPane p = (AnchorPane)tabFiles.getSelectionModel().getSelectedItem().getContent();
        ArrayList<Erros> erros = new ArrayList<Erros>();
        Erros erro;
        int linha = 10;
        
        Image img = new Image("com/krupique/visualbeans/resources/error.png");
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getEstado() == 1) //Erro sintatico
            {
                strsint += list.get(i).getLog() + " Linha: " + list.get(i).getLinha() + " Coluna: " + list.get(i).getColuna() + "\n";
                erro = new Erros(new ImageView(img), 6, linha + (list.get(i).getLinha() - 1) * 15);
                erros.add(erro);
            }
            else if(list.get(i).getEstado() == 2) //Erro semantico
            {
                strseman += list.get(i).getLog() + " Linha: " + list.get(i).getLinha() + " Coluna: " + list.get(i).getColuna() + "\n";
                erro = new Erros(new ImageView(img), 6, linha + (list.get(i).getLinha() - 1) * 15);
                erros.add(erro);
            }
        }
        
        for (int i = 1; i < p.getChildren().size(); i++)
            p.getChildren().remove(i);
        
        for (int i = 0; i < erros.size(); i++)
            p.getChildren().add(erros.get(i).getImg());
        
        textSintatico.setText(strsint);
        textSemantic.setText(strseman);
    }

    @FXML
    private void evtTemaDark(ActionEvent event) {
    }

    @FXML
    private void evtTemaLight(ActionEvent event) {
    }

    @FXML
    private void evtTelaBNF(ActionEvent event) {
    }

    @FXML
    private void evtTelaFirstFollow(ActionEvent event) {
    }

    @FXML
    private void evtTelaSobre(ActionEvent event) {
        
    }

    @FXML
    private void evtExibr(ActionEvent event) {
        ArrayList<TabelaTokens> list;
        if(ckExibir.isSelected())
        {
            list = (ArrayList<TabelaTokens>)obj[3];
        }
        else
        {
            list = (ArrayList<TabelaTokens>)obj[1];
        }
        adicionarNoTableview(list);
    }

    private void setTextIntermed(ArrayList<TabelaTokens> ls) {
        String str = "";
        String pal;
        int cont = 0;
        for (int i = 0; i < ls.size(); i++) {
            pal = ls.get(i).getPalavra();
            if(pal.equals(";"))
            {
                str += ";\n";
                str = addSpace(str, cont);
            }
            else if(pal.equals("{"))
            {
                str += "{\n";
                str = addSpace(str, ++cont);
            }
            else if(pal.equals("}"))
            {
                str = addSpace(str, --cont);
                str += "}\n";
            }
            else
                str += pal + " ";
        }
        
        textIntermed.setText(str);
    }
    
    private String addSpace(String str, int cont)
    {
        for (int i = 0; i < cont; i++)
            str += "\t";
        return str;
    }
}
