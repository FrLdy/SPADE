package main.algorithm.spade.structure;

import java.util.*;

/**
 * Implementation of a Idlist.
 * This IdList is based on a hash map of entries <Integer, List<Integer>> (sid,[eid]),
 * and it makes a correspondence between a sid of a database transaction,
 * denoted by the Integer, with the appearances of the pattern in that sequence,
 * denoted by the eid of the transaction itemset which contains the last item of the sequence.
 * In order to make the join operation, we will do it entry by entry, for each
 * entries shared by two sequences.
 * see "SPADE: An efficient Algorithm for Mining Frequent Sequence" by M. ZAKI.
 */
public class IdList extends HashMap<Integer, List<Integer>>{

    /**
     * General constructor
     */
    public IdList() {

    }

    /**
     * Add a eid in eid list of a sid.
     * @param sid is the id of a sequence of the dataset.
     * @param eid is the id of an event of the sid sequence.
     */
    public void add(int sid, int eid){
        if (!containsKey(sid)) {
            this.addSid(sid);
        }
        if (!get(sid).contains(eid)){
            this.addEid(sid,eid);
        }
    }

    /**
     * Add a eid in eid list of a sid.
     * @param sid is the id of a sequence of the dataset.
     * @param eid is the id of an event of the sid sequence.
     */
    private void addEid(int sid, int eid){
        this.get(sid).add(eid);
        Collections.sort(this.get(sid));
    }

    /**
     * Initialize the List of a sid entry. It will contain eid(s) linked with it.
     * @param sid is the id of a sequence of the dataset.
     */
    private void addSid(int sid){
        if (!this.containsKey(sid)){
            this.put(sid, new ArrayList<>());
        }
    }

    /**
     * Get the string retpresentation of IdList.
     * @return String representation.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer sid : this.keySet()){
            stringBuilder.append(sid.toString()).append(this.get(sid)).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Join two IdList together by the presence of pair (sid,eid) in both IdList.
     * @param otherIdList the other IdList to join with this IdList.
     * @return The egality join of these both IdList.
     */
    public IdList equalityJoin(IdList otherIdList){
        IdList res = new IdList();
        for (Integer sid : otherIdList.keySet()){
            if (this.containsKey(sid)){
                for (Integer eid : otherIdList.get(sid)){
                    if (this.get(sid).contains(eid)){
                        res.add(sid, eid);
                    }
                }
            }
        }
        return res;
    }

    /**
     * Join two IdList by the temporal factor, idList2 is after idList1.
     * Only couples (sid2, eid2) of idList2 great than at least one (sid1, eid1), where sid2 == sid1, are retained.
     * @param otherIdList the other IdList to join with this IdList.
     * @return The IdList result of temporal join.
     */
    public IdList temporalJoin(IdList otherIdList){
        IdList res = new IdList();
        for (Integer oSid : otherIdList.keySet()){
            if (this.containsKey(oSid)){
                for (Integer oEid : otherIdList.get(oSid)){
                    if (oEid < this.get(oSid).get(0)){
                        continue;
                    }
                    for (Integer eid : this.get(oSid)){
                        if (oEid > eid){
                            res.add(oSid, oEid);
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }
}
