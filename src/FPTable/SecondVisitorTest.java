package FPTable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import AST.*;

import java.util.HashSet;

/**
 * @Author: Maximilian Linsler
 */
class SecondVisitorTest {
    private SecondVisitor secondVisitor = new SecondVisitor();
    @BeforeEach
    void createTree(){
        // Create Tree for Test
        // node1
        OperandNode node1 = new OperandNode("a");
        node1.position = 1;
        node1.nullable = false;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        // node2
        OperandNode node2 = new OperandNode("1");
        node2.position = 2;
        node2.nullable = false;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        // node3
        OperandNode node3 = new OperandNode("2");
        node3.position = 3;
        node3.nullable = false;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        // node4
        BinOpNode node4 = new BinOpNode("|",node2,node3);
        node4.nullable = false;
        node4.firstpos.add(2);
        node4.firstpos.add(3);
        node4.lastpos.add(2);
        node4.lastpos.add(3);
        // node5
        UnaryOpNode node5 = new UnaryOpNode("*",node4);
        node5.nullable = true;
        node5.firstpos.add(2);
        node5.firstpos.add(3);
        node5.lastpos.add(2);
        node5.lastpos.add(3);
        // node6
        BinOpNode node6 = new BinOpNode("°",node1,node5);
        node6.nullable = false;
        node6.firstpos.add(1);
        node6.lastpos.add(1);
        node6.lastpos.add(2);
        node6.lastpos.add(3);
        // node7
        OperandNode node7 = new OperandNode("a");
        node7.position = 4;
        node7.nullable = false;
        node7.firstpos.add(4);
        node7.lastpos.add(4);
        // node8
        BinOpNode node8 = new BinOpNode("°",node6,node7);
        node8.nullable = false;
        node8.firstpos.add(1);
        node8.lastpos.add(4);
        // node9
        OperandNode node9 = new OperandNode("#");
        node9.position = 5;
        node9.nullable = false;
        node9.firstpos.add(5);
        node9.lastpos.add(5);
        // Top Node
        BinOpNode topNode = new BinOpNode("°",node8,node9);
        topNode.nullable = false;
        topNode.firstpos.add(1);
        topNode.lastpos.add(5);

        // Run Second Visitor
        DepthFirstIterator.traverse(topNode, secondVisitor);
    }
    // Test size of FollowPosTable
    @Test
    void followPosSizeTest(){
        assertEquals (5, secondVisitor.getFollowPosTableEntries().size());
    }

    // Test positions: Followpos, Symbol
    @Test
    void followPos1Test(){
        HashSet<Integer> testFollowPos = new HashSet<>();
        testFollowPos.add(2);
        testFollowPos.add(3);
        testFollowPos.add(4);
        assertEquals("a", secondVisitor.getFollowPosTableEntries().get(1).symbol);
        assertEquals(testFollowPos, secondVisitor.getFollowPosTableEntries().get(1).followpos);
    }

    @Test
    void followPos2Test(){
        HashSet<Integer> testFollowPos = new HashSet<>();
        testFollowPos.add(2);
        testFollowPos.add(3);
        testFollowPos.add(4);
        assertEquals("1", secondVisitor.getFollowPosTableEntries().get(2).symbol);
        assertEquals(testFollowPos, secondVisitor.getFollowPosTableEntries().get(2).followpos);
    }

    @Test
    void followPos3Test(){
        HashSet<Integer> testFollowPos = new HashSet<>();
        testFollowPos.add(2);
        testFollowPos.add(3);
        testFollowPos.add(4);
        assertEquals("2", secondVisitor.getFollowPosTableEntries().get(3).symbol);
        assertEquals(testFollowPos, secondVisitor.getFollowPosTableEntries().get(3).followpos);
    }

    @Test
    void followPos4Test(){
        HashSet<Integer> testFollowPos = new HashSet<>();
        testFollowPos.add(5);
        assertEquals("a", secondVisitor.getFollowPosTableEntries().get(4).symbol);
        assertEquals(testFollowPos, secondVisitor.getFollowPosTableEntries().get(4).followpos);
    }

    @Test
    void followPos5Test(){
        HashSet<Integer> testFollowPos = new HashSet<>();
        assertEquals("#", secondVisitor.getFollowPosTableEntries().get(5).symbol);
        assertEquals(testFollowPos, secondVisitor.getFollowPosTableEntries().get(5).followpos);
    }
}