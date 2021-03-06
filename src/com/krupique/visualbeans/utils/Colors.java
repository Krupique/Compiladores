package com.krupique.visualbeans.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

/**
 *
 * @author Henrique K. Secchi
 */
public class Colors {
    private CodeArea codeArea;
    private String exampleString =  "program exemplo;\n" +
                                    "{\n"+
                                    "   int x = 10;\n"+
                                    "   double y = 20;\n"+
                                    "   int test = 50;\n"+
                                    "   double test2 = 25;\n"+
                                    "   if(x < y)\n"+
                                    "   {\n"+
                                    "       while(x < y)\n"+
                                    "       {\n"+
                                    "           y++;\n"+
                                    "           x = x * 2;\n"+
                                    "       }\n"+
                                    "   }\n" +
                                    "   for(int var = 0; var < y; var++;)\n"+
                                    "   {\n"+
                                    "       x += 2;\n"+
                                    "   }\n" +
                                    "}";

    private static final String[] KEYWORDS = new String[]{"boolean", "char", "double", "else", "false", "for", "if", "int", "mentira", "null", "nulo", "NULL", "program", "string", "true", "verdade", "while"};
    //private static final String[] SIMBOLOS = new String[]{"a, b, c, d, e, f"};
    
    //private static final String SIMBOLOS_PATTERN = "\\b(" + String.join("|", SIMBOLOS) + ")\\b";
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String LETTERS_PATTERN = "[A-Za-z#`$._]+";
    private static final String SIMBOLOS_PATTERN = "\\{|\\}|\\[|\\]|\\(|\\)|\\%|\\;|\\<|\\>|\\*|\\=|\\?|\\:|\\||\\&|\\+|\\-|\\/|\\~|\\!|\\^";
    private static final String NUMBERS_PATTERN = "[0-9]+";
    private static final String CHAR_PATTERN = "\'[\\S+\\s]{1}\'";
    private static final String STRING_PATTERN = "\\\"([^\\\"\\\\\\\\]|\\\\\\\\.)*\\\"";
    private static final String COMENT_PATTERN = "\\@([^\\@\\\\\\\\]|\\\\\\\\.)*\\@";
    private static final String RESTO_PATTERN = "\\S+";
    
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")" +
            "|(?<LETTERS>" + LETTERS_PATTERN + ")" +
            "|(?<SIMBOLOS>" + SIMBOLOS_PATTERN + ")" +
            "|(?<NUMBERS>" + NUMBERS_PATTERN + ")" +
            "|(?<CHAR>" + CHAR_PATTERN + ")" +
            "|(?<STRING>" + STRING_PATTERN + ")" +
            "|(?<COMENT>" + COMENT_PATTERN + ")" +
            "|(?<RESTO>" + RESTO_PATTERN + ")"
    );
    //Preciso fazer em tempo de execução para ir identificando os erros.

    public Colors(CodeArea codeArea) {
        this.codeArea = codeArea;
        start();
    }
    
    public Colors(CodeArea codeArea, String texto){
        exampleString = texto;
        this.codeArea = codeArea;
        start();
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }
    
    public void start() {
        String css = "com/krupique/visualbeans/styles/main_code_area.css";
        codeArea.textProperty().addListener((obs, oldText, newText) -> 
        {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });
        codeArea.replaceText(0, 0, exampleString);
        codeArea.getStylesheets().add(css);
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting( String text)
    {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) 
        {
            String styleClass = 
                matcher.group("KEYWORD") != null ? "keyword": 
                matcher.group("LETTERS") != null ? "letters" :  
                matcher.group("SIMBOLOS") != null ? "simbolo" : 
                matcher.group("NUMBERS") != null ? "numeros" :
                matcher.group("CHAR") != null ? "char" :
                matcher.group("STRING") != null ? "string" :
                matcher.group("COMENT") != null ? "coment" :
                matcher.group("RESTO") != null ? "resto" : null;
            
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
