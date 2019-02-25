/**@Autor: Johannes Herterich*/
package AST;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTGenerator {
    private static final String concat = "°";
    private static final String endSymbol = "#";
    private static final String or = "|";
    private static final String kleen = "*";
    private static final String pos = "+";
    private static final String opt = "?";
    private static final String closeBrack = ")";
    private static final String openBrack = "(";
    private static final List<String> multOps = Arrays.asList(kleen, pos, opt);
    private static final List<String> alphaNums = getAlphaNums();

    private static List<String> getAlphaNums() {
        List<Character> ret = new LinkedList<>();
        for (int i = 48; i < 58; i++) {
            ret.add((char) i);
        }

        for (int i = 65; i < 91; i++) {
            ret.add((char) i);
        }

        for (int i = 97; i < 123; i++) {
            ret.add((char) i);
        }
        return ret.stream().map(Object::toString).collect(Collectors.toList());
    }

    private int currIndex = 0;
    private String regex;

    public ASTGenerator(String regex, boolean hasEndSymbol) {
        this.regex = String.copyValueOf(regex.toCharArray());
        if (!hasEndSymbol) {
            this.regex = this.regex + endSymbol;
        }
    }

    public ASTGenerator(String regex) {
        this(regex, regex.endsWith("#"));
    }

    public IVisitable getTreeFromRegex() {
        int regexLength = regex.length();
        if (regexLength < 1 || regex.lastIndexOf(endSymbol) != regexLength - 1) {
            throw getIllArgException("Expression must end with " + getNameAndValue("endSymbol", endSymbol) + "(even if empty)");
        } else if (regex.equals(endSymbol)) {
            return new OperandNode(endSymbol);
        } else if (getFirstFromRegex().equals(openBrack)) {
            IVisitable regExp = regExpWrapper();
            IVisitable leaf = new OperandNode(endSymbol);
            IVisitable ret = new BinOpNode(concat, regExp, leaf);
            removeFirstFromRegex();
            if (regex.length() > 0) {
                throw getIllArgException("Fail in compiler, regex should have no more symbols");
            }
            return ret;
        } else {
            throw getIllArgException("Expression needs to start with " + getNameAndValue("opening brackets", openBrack));
        }
    }

    private IVisitable regExpWrapper() {
        removeFirstFromRegex();
        IVisitable ret = regExp();
        if (!getFirstFromRegex().equals(closeBrack)) {
            throw getIllArgException("Expression must have " + getNameAndValue("closing brackets", closeBrack));
        }
        removeFirstFromRegex();
        return ret;
    }

    private String getNameAndValue(String name, String val) {
        return name + "('" + val + "')";
    }

    private IllegalArgumentException getIllArgException(String message) {
        return new IllegalArgumentException(message + " " + getEndText());
    }

    private String getEndText() {
        return "At index: " + currIndex;
    }

    private IVisitable regExp() {
        return re(term(null));
    }

    private IVisitable re(IVisitable param) {
        if (!getFirstFromRegex().equals(or)) {
            return param;
        } else {
            removeFirstFromRegex();
            return re(new BinOpNode(or, param, term(null)));
        }
    }

    private void removeFirstFromRegex() {
        regex = regex.substring(1);
        currIndex++;
    }

    private String getFirstFromRegex() {
        return regex.substring(0, 1);
    }

    private IVisitable term(IVisitable param) {
        String firstFromRegex = getFirstFromRegex();
        if (!isAlphaNum(firstFromRegex) && !firstFromRegex.startsWith(openBrack)){
            return param;
        } else{
            IVisitable termRekParam = factor();
            if (param != null) {
                termRekParam = new BinOpNode(concat, param, termRekParam);
            }
            return term(termRekParam);
        }
    }

    private IVisitable factor() {
        return hop(elem());
    }

    private IVisitable elem() {
        if (getFirstFromRegex().equals(openBrack)) {
            return regExpWrapper();
        } else {
            return alphaNum();
        }
    }

    private IVisitable alphaNum() {
        String startOfRegex = getFirstFromRegex();
        if (isAlphaNum(startOfRegex)) {
            removeFirstFromRegex();
            return new OperandNode(startOfRegex);
        } else {
            throw getIllArgException("Alpha-numeric symbol expected");
        }
    }

    private boolean isAlphaNum(String startOfRegex) {
        return alphaNums.stream().anyMatch(an -> an.equals(startOfRegex));
    }

    private IVisitable hop(IVisitable param) {
        String firstFromRegex = getFirstFromRegex();
        if (regex.length() < 1 || multOps.stream().noneMatch(firstFromRegex::equals)) {
            return param;
        } else {
            removeFirstFromRegex();
            return new UnaryOpNode(firstFromRegex, param);
        }
    }
}
