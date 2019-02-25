/**@Autor:Oliver Bauer*/
package DFA;

import FPTable.*;

import java.util.*;

public class DFAStateBuilder {
    private SortedMap<Integer, FollowPosTableEntry> followPosTableEntries= new TreeMap<>();
    private int index;
    public DFAStateBuilder(){
    }

    public SortedMap<DFAState, Map<Character, DFAState>> createDFAStateMap(SortedMap<Integer, FollowPosTableEntry> followPosTableEntries){
        this.followPosTableEntries=followPosTableEntries;
        ArrayList<DFAState> dStates = new ArrayList<>();
        SortedMap<DFAState, Map<Character, DFAState>> stateTransitionTable = new TreeMap<>();
        ArrayList<Integer> unmarkedStatePos=new ArrayList<>();
        Set<String> alphabet=new HashSet<>();
        for(FollowPosTableEntry entry: followPosTableEntries.values()){//das Alphabet bestimmen
            alphabet.add(entry.symbol);
        }
        int unmarkedStates=0;
        int fistPos=1;
        Set<Integer> startList = new HashSet<>(followPosTableEntries.get(fistPos).followpos);
        dStates.add(getDFAState(isAcceptingState(new ArrayList<>(Collections.singletonList(followPosTableEntries.get(fistPos).symbol))),startList));
        unmarkedStates++;
        unmarkedStatePos.add(dStates.size()-1);
        while(unmarkedStates!=0){
            Map<Character,DFAState> tempMap=new TreeMap<>();
            int markedStatePos=unmarkedStatePos.get(0);//S markieren
            unmarkedStates--;
            unmarkedStatePos.remove(0);
            for(String symbol: alphabet) {
                ArrayList<String> u = new ArrayList<>();//indexe(in FollowPos), der followpos die einbezogen werden
                int countFollowPosU = 0;//Anzahl der insgesammten followPos von u
                Set<Integer> compareFollowPos=new HashSet<>();//Die followPos die zu U gehören
                for (int pos : dStates.get(markedStatePos).positionsSet) {//menge U bilden
                    if(symbol.equals(followPosTableEntries.get(pos).symbol)){//symbole abgleichen und Elemente zu U hinzufügen
                        u.add(followPosTableEntries.get(pos).symbol);
                        countFollowPosU+=followPosTableEntries.get(pos).followpos.size();
                        compareFollowPos.addAll(followPosTableEntries.get(pos).followpos);
                    }
                }
                boolean exists=false;
                DFAState theState=null;//Platzhalter für den schon bestehenden bzw. neu erstellten Zustand
                for(DFAState state:dStates){
                    if(state.positionsSet.size()==countFollowPosU){//wenn nicht gleich viele followPos kann das Element nicht gleich sein
                        if(state.positionsSet.equals(compareFollowPos)){//wenn sowohl anzahl als auch inhalte der followPos übereinstimmen existiert der Zustand schon
                            exists=true;
                            theState=state;
                        }
                    }
                }
                if(!exists){//nicht in dStates also füge U zu dStates hinzu
                    theState=getDFAState(isAcceptingState(u),compareFollowPos);
                    dStates.add(theState);
                    unmarkedStates++;
                    unmarkedStatePos.add(dStates.size()-1);
                }
                tempMap.put(symbol.charAt(0),theState);//Dtran[S,a]=u
            }
            stateTransitionTable.put(dStates.get(markedStatePos),tempMap);
        }
        return stateTransitionTable;
    }

    private boolean isAcceptingState(ArrayList<String> symbols){
        boolean isAcceptingState = false;
        for(String symbol: symbols){
            if(symbol.equals("#")){
                isAcceptingState=true;
                break;
            }
        }
        return isAcceptingState;
    }

    private DFAState getDFAState(boolean isAccepting,Set<Integer> pos){
        return new DFAState(index++,isAccepting,pos);
    }

}
