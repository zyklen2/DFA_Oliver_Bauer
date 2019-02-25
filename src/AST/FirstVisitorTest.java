package AST;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Lars Eckenfels
 */

class FirstVisitorTest
{
    private FirstVisitor firstVisitor;
    private OperandNode operandNode1;
    private OperandNode operandNode2;
    private OperandNode operandNode3;

    @BeforeEach
    void setup() {
        operandNode1 = new OperandNode("a");
        operandNode1.nullable = true;
        operandNode1.firstpos.add(1);
        operandNode1.lastpos.add(1);

        operandNode2 = new OperandNode("b");
        operandNode2.nullable = true;
        operandNode2.firstpos.add(2);
        operandNode2.lastpos.add(2);

        operandNode3 = new OperandNode("c");
        operandNode3.nullable = false;
        operandNode3.firstpos.add(3);
        operandNode3.firstpos.add(4);
        operandNode3.lastpos.add(5);
        operandNode3.lastpos.add(6);

        firstVisitor = new FirstVisitor();
    }

    @Test
    void execute()
    {
    }

    @Test
    void visitBinOpNodeOR()
    {
        BinOpNode binOpNode = new BinOpNode("|", operandNode1, operandNode2);
        assertBinOpNode(binOpNode);

        binOpNode = new BinOpNode("|", operandNode1, operandNode3);
        firstVisitor.visit(binOpNode);
        assertArrayEquals(new Integer[] {1, 3, 4}, binOpNode.firstpos.toArray());
        assertArrayEquals(new Integer[] {1, 5, 6}, binOpNode.lastpos.toArray());
        assertTrue(binOpNode.nullable);
    }

    @Test
    void visitBinOpNodeAND()
    {
        BinOpNode binOpNode = new BinOpNode("°", operandNode1, operandNode2);
        assertBinOpNode(binOpNode);

        binOpNode = new BinOpNode("°", operandNode1, operandNode3);
        firstVisitor.visit(binOpNode);
        assertArrayEquals(new Integer[] {1, 3, 4}, binOpNode.firstpos.toArray());
        assertArrayEquals(new Integer[] {5, 6}, binOpNode.lastpos.toArray());
        assertFalse(binOpNode.nullable);
    }

    void assertBinOpNode(BinOpNode node) {
        firstVisitor.visit(node);
        assertArrayEquals(new Integer[] {1, 2}, node.firstpos.toArray());
        assertArrayEquals(new Integer[] {1, 2}, node.lastpos.toArray());
        assertTrue(node.nullable);
    }

    @Test
    void visitUnaryOpNodeStart()
    {
        UnaryOpNode unaryOpNode = new UnaryOpNode("*", operandNode1);
        assertUnaryOpNode(unaryOpNode);
    }

    @Test
    void visitUnaryOpNodePlus()
    {
        UnaryOpNode unaryOpNode = new UnaryOpNode("+", operandNode1);
        assertUnaryOpNode(unaryOpNode);
    }

    @Test
    void visitUnaryOpNodeOption()
    {
        UnaryOpNode unaryOpNode = new UnaryOpNode("?", operandNode1);
        assertUnaryOpNode(unaryOpNode);
    }

    void assertUnaryOpNode(UnaryOpNode node) {
        firstVisitor.visit(node);
        assertArrayEquals(new Integer[] {1}, node.firstpos.toArray());
        assertArrayEquals(new Integer[] {1}, node.lastpos.toArray());
        assertTrue(node.nullable);
    }

    @Test
    void visitOperandOpNode()
    {
        OperandNode operandNodeTest1 = new OperandNode("a");
        OperandNode operandNodeTest2 = new OperandNode(null);
        OperandNode operandNodeTest3 = new OperandNode("b");

        //Visit visitor
        firstVisitor.visit(operandNodeTest1);
        firstVisitor.visit(operandNodeTest2);
        firstVisitor.visit(operandNodeTest3);

        //Assert nullable
        assertFalse(operandNodeTest1.nullable);
        assertTrue(operandNodeTest2.nullable);
        assertFalse(operandNodeTest3.nullable);

        //Assert position
        assertEquals(1, operandNodeTest1.position);
        assertEquals(-1, operandNodeTest2.position);
        assertEquals(2, operandNodeTest3.position);

        //Assert firstpos
        assertArrayEquals(new Integer[] {1}, operandNodeTest1.firstpos.toArray());
        assertArrayEquals(new Integer[] {}, operandNodeTest2.firstpos.toArray());
        assertArrayEquals(new Integer[] {2}, operandNodeTest3.firstpos.toArray());

        //Assert lastpos
        assertArrayEquals(new Integer[] {1}, operandNodeTest1.lastpos.toArray());
        assertArrayEquals(new Integer[] {}, operandNodeTest2.lastpos.toArray());
        assertArrayEquals(new Integer[] {2}, operandNodeTest3.lastpos.toArray());
    }
}