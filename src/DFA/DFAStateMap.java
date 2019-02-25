package DFA;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DFAStateMap {
    private SortedMap<DFAState, Map<Character, DFAState>> stateTransitionTable = new TreeMap<>();
}
