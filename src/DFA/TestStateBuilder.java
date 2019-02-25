/**@Autor:Oliver Bauer*/
package DFA;

import AST.ASTGenerator;
import AST.FirstVisitor;
import FPTable.DepthFirstIterator;
import FPTable.FollowPosTableEntry;
import FPTable.SecondVisitor;
import org.testng.annotations.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TestStateBuilder {
    @Test
    public void testDFAStateBuilder(){
        SortedMap<Integer, FollowPosTableEntry> followPosTableEntries = new TreeMap<>();
        var topNode = new ASTGenerator("((A|B)+)#").getTreeFromRegex();
        System.out.println("AST:\n" + topNode);
        FirstVisitor.execute(topNode);
        SecondVisitor secondVisitor = new SecondVisitor();
        DepthFirstIterator.traverse(topNode, secondVisitor);
        followPosTableEntries = secondVisitor.getFollowPosTableEntries();
        DFAStateBuilder stateBuilder = new DFAStateBuilder();
        SortedMap<DFAState, Map<Character, DFAState>> stateMap = stateBuilder.createDFAStateMap(followPosTableEntries);
        boolean equalTrees=false;
        DFAState theTestState = new DFAState(0,false, followPosTableEntries.get(1).followpos);
        if(stateMap.get(theTestState).size()==3&&stateMap.get(theTestState).get('#')!=null&&stateMap.get(theTestState).get('A')!=null&&stateMap.get(theTestState).get('B')!=null) {
            equalTrees=true;
        }
        Assertions.assertTrue(equalTrees);//testen ob alle Elemente vorhanden sind
        equalTrees=false;
        if(stateMap.get(theTestState).size()==3&&stateMap.get(theTestState).get('#').index==1&&stateMap.get(theTestState).get('A').index==0&&stateMap.get(theTestState).get('B').index==0) {
            equalTrees=true;
            if(!(stateMap.get(theTestState).get('#').isAcceptingState==true)||!(stateMap.get(theTestState).get('#').positionsSet.size()==0)){
                equalTrees=false;
            }
            if(!(stateMap.get(theTestState).get('A').isAcceptingState==false)||!(stateMap.get(theTestState).get('A').positionsSet.size()==3)||
                    !((int)stateMap.get(theTestState).get('A').positionsSet.toArray()[0]==1&&(int)stateMap.get(theTestState).get('A').positionsSet.toArray()[1]==2&&
                            (int)stateMap.get(theTestState).get('A').positionsSet.toArray()[2]==3)){
                equalTrees=false;
            }
            if(!(stateMap.get(theTestState).get('B').isAcceptingState==false)||!(stateMap.get(theTestState).get('B').positionsSet.size()==3)||
                    !((int)stateMap.get(theTestState).get('B').positionsSet.toArray()[0]==1&&(int)stateMap.get(theTestState).get('B').positionsSet.toArray()[1]==2&&
                            (int)stateMap.get(theTestState).get('B').positionsSet.toArray()[2]==3)){
                equalTrees=false;
            }
        }
        Assertions.assertTrue(equalTrees);//testen ob die inhalte der Elemente gleich ist
        //Da diese Stichprobe stimmt kann davon ausgegangen werden dass das umschreiben von followPos zu DFAState funktioniert.
    }
}
