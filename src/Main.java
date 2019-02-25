import AST.ASTGenerator;
import AST.FirstVisitor;
import DFA.DFAState;
import DFA.DFAStateBuilder;
import FPTable.*;

import java.util.Map;
import java.util.SortedMap;

public class Main {

    public static void main(String[] args) {
        SortedMap<Integer, FollowPosTableEntry> followPosTableEntries;
        if(args.length != 1){
            throw new IllegalArgumentException("Only one argument (Regular Expression allowed)");
        }else{
            var topNode = new ASTGenerator(args[0]).getTreeFromRegex();
            System.out.println("AST:\n" + topNode);
            FirstVisitor.execute(topNode);
            SecondVisitor secondVisitor = new SecondVisitor();
            DepthFirstIterator.traverse(topNode, secondVisitor);
            followPosTableEntries = secondVisitor.getFollowPosTableEntries();
            DFAStateBuilder stateBuilder = new DFAStateBuilder();
            SortedMap<DFAState, Map<Character, DFAState>> stateMap = stateBuilder.createDFAStateMap(followPosTableEntries);
            System.out.println("");
        }
    }
}
