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
    private String exampleString = "This is a WARNING for an INFO! Please stay tuned";

    private static final String[] KEYWORDS = new String[]{"and", "boolean", "catch", "char", "double", "else", "false", "for", "if", "int", "mentira", "new", "null", "nulo", "NULL", "or", "program", "string", "true", "try", "var", "verdade", "while"};
    //private static final String[] SIMBOLOS = new String[]{"azar"};

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    //private static final String SIMBOLOS_PATTERN = "\\b(" + String.join("|", SIMBOLOS) + ")\\b";
    private static final String LETTERS_PATTERN = "[A-Za-z@#`$.]+";
    
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String PARENTESES_PATTERN = "\\(|\\)";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String SIMBOLOS_PATTERN = "[=?:|&^+-/~!]+";
    //Falta os de números e alguns caracteres.
    //Preciso fazer em tempo de execução para ir identificando os erros.
    //Falta fazer o pattern de comentários também.
    //Dar um jeito de arrumar o cursor piscante (tá me irritando demais essa buceta).
    
    private static final String STRING_PATTERN = "\"([^\"]|\\\")*\"";
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")" + 
            "|(?<SIMBOLO>" + SIMBOLOS_PATTERN + ")" +
            "|(?<LETTERS>" + LETTERS_PATTERN + ")" +
            "|(?<STRING>" + STRING_PATTERN + ")" +
            "|(?<BRACE>" + BRACE_PATTERN + ")" +
            "|(?<BRACKET>" + BRACKET_PATTERN + ")" +
            "|(?<PAREN>" + PARENTESES_PATTERN + ")" +
            "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
    );

    public Colors(CodeArea codeArea) {
        this.codeArea = codeArea;
        start();
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }
    
    public void start() {
        String css = "com/krupique/visualbeans/styles/main_code_area.css";
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });
        codeArea.replaceText(0, 0, exampleString);
        codeArea.getStylesheets().add(css);
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting(
            String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = 
                    matcher.group("KEYWORD") != null ? "keyword": 
                    matcher.group("SIMBOLO") != null ? "simbolo" : 
                    matcher.group("LETTERS") != null ? "letters" : 
                    matcher.group("BRACE") != null ? "simbolo" : 
                    matcher.group("BRACKET") != null ? "simbolo" : 
                    matcher.group("SEMICOLON") != null ? "simbolo" : 
                    matcher.group("PAREN") != null ? "simbolo" : 
                    matcher.group("STRING") != null ? "string" : null;
            /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start()
                    - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end()
                    - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
