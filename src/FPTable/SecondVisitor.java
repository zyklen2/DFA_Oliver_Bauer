package FPTable;

import AST.*;

import java.util.SortedMap;
import java.util.TreeMap;
/**
 * @author: Maximilian Linsler
 */
public class SecondVisitor implements IVisitor {
    private SortedMap<Integer, FollowPosTableEntry> followPosTableEntries = new TreeMap<>();

    public SortedMap<Integer, FollowPosTableEntry> getFollowPosTableEntries() {
        return followPosTableEntries;
    }

    @Override
    public void visit(OperandNode node) {
        FollowPosTableEntry entry = new FollowPosTableEntry(node.position,node.symbol);
        followPosTableEntries.put(node.position,entry);
    }

    @Override
    public void visit(BinOpNode node) {
        if (node.operator.equals("°")){
            for (int last : ((SyntaxNode)node.left).lastpos) {
                for (int first: ((SyntaxNode)node.right).firstpos){
                    followPosTableEntries.get(last).followpos.add(first);
                }
            }
        } else if (!node.operator.equals("|")){
            System.out.println("Operator is unknown");
        }
    }

    @Override
    public void visit(UnaryOpNode node) {
        if (node.operator.equals("*") || node.operator.equals("+")){
            for (int last : node.lastpos) {
                for (int first: node.firstpos){
                    followPosTableEntries.get(last).followpos.add(first);
                }
            }
        } else if (!node.operator.equals("?")){
            System.out.println("Operator is unknown");
        }
    }
}