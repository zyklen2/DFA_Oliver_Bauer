/**@Autor: Johannes Herterich*/
package AST;

import org.junit.jupiter.api.Assertions;

class ASTGeneratorTest {

    @org.junit.jupiter.api.Test
    void getTreeFromRegexWithEnd() {
        String regex = "#";
        IVisitable ref = new OperandNode("#");
        Assertions.assertEquals(ref, new ASTGenerator(regex, true).getTreeFromRegex());
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "";
        Assertions.assertEquals(ref, new ASTGenerator(regex, false).getTreeFromRegex());
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "()#";
        ref = new BinOpNode("°", null, ref);
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "(AB1)#";
        OperandNode aNode = new OperandNode("A");
        OperandNode bNode = new OperandNode("B");
        OperandNode s1node = new OperandNode("1");
        ((BinOpNode) ref).left = new BinOpNode("°", new BinOpNode("°", aNode, bNode), s1node);
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "(AB1|2)#";
        OperandNode s2Node = new OperandNode("2");
        ((BinOpNode) ref).left = new BinOpNode("|", ((BinOpNode) ref).left, s2Node);
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "(AB(1|2))#";
        ((BinOpNode) ref).left = new BinOpNode("°", new BinOpNode("°", aNode, bNode), new BinOpNode("|", s1node, s2Node));
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "(A*)#";
        ((BinOpNode) ref).left = new UnaryOpNode("*", aNode);
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "((A|B)+)#";
        ((BinOpNode) ref).left = new UnaryOpNode("+", new BinOpNode("|", aNode, bNode));
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        regex = "(((A)))#";
        ((BinOpNode) ref).left = aNode;
        Assertions.assertEquals(ref, getTreeFromRegex(regex));

        //java needs final for lambda because java is not a good programming language (c# is better)
        String javaIsBad = "()";
        Class<IllegalArgumentException> illArgClass = IllegalArgumentException.class;
        Assertions.assertThrows(illArgClass, () -> new ASTGenerator(javaIsBad, true).getTreeFromRegex());
        String cSharpIsBetter = "(#";
        Assertions.assertThrows(illArgClass, () -> getTreeFromRegex(cSharpIsBetter));
        String whyCantJavaDoThis = "(+)#";
        Assertions.assertThrows(illArgClass, () -> getTreeFromRegex(whyCantJavaDoThis));
        String haveIMentionedILikeCSharp = "(#)#";
        Assertions.assertThrows(illArgClass, () -> getTreeFromRegex(haveIMentionedILikeCSharp));
    }

    private IVisitable getTreeFromRegex(String regex) {
        return new ASTGenerator(regex).getTreeFromRegex();
    }
}