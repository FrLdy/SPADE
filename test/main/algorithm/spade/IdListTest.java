package main.algorithm.spade;

import main.algorithm.spade.structure.IdList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdListTest {
    IdList idList1, idList2;

    public IdListTest() {
        this.idList1 = new IdList();
        idList1.add(1, 20);
        idList1.add(1, 30);
        idList1.add(1, 40);
        idList1.add(8, 10);
        idList1.add(8, 30);
        idList1.add(8, 50);
        idList1.add(8, 80);
        idList1.add(13, 50);
        idList1.add(13, 70);
        idList1.add(4, 60);
        idList1.add(7, 40);
        idList1.add(15, 60);
        idList1.add(17, 20);
        idList1.add(20, 10);

        this.idList2 = new IdList();
        idList2.add(1, 70);
        idList2.add(1, 80);
        idList2.add(8,30);
        idList2.add(8,40);
        idList2.add(8,50);
        idList2.add(8,80);
        idList2.add(13,10);
        idList2.add(3,10);
        idList2.add(5,70);
        idList2.add(11,30);
        idList2.add(16,80);
        idList2.add(20,20);
    }

    @Test
    void equalityJoin() {
        IdList verif = new IdList();
        verif.add(8, 30);
        verif.add(8, 50);
        verif.add(8, 80);

        IdList res = idList1.equalityJoin(idList2);
        if (!res.equals(verif)){
            fail("Error equality join");
            System.out.println(res);
        }
    }

    @Test
    void temporalJoin() {
        IdList verif = new IdList();
        verif.add(1, 70);
        verif.add(1, 80);
        verif.add(8, 30);
        verif.add(8, 40);
        verif.add(8, 50);
        verif.add(8, 80);
        verif.add(20, 20);
        System.out.println(idList1.temporalJoin(idList2));
        if (!idList1.temporalJoin(idList2).equals(verif)){
            fail("Error temporal join 1");
        }

        verif = new IdList();
        verif.add(8, 50);
        verif.add(8, 80);
        verif.add(13, 50);
        verif.add(13, 70);
        if (!idList2.temporalJoin(idList1).equals(verif)){
            fail("Error temporal join 2");
        }

    }

    @Test
    void temporalJoinMaxGap() {
        int maxGap = 10;
        IdList idList = idList1.temporalJoin(idList2, maxGap);
        IdList verif = new IdList();
        verif.add(20, 20);
        verif.add(8, 40);
        if (!idList.equals(verif)){
            fail();
        }
    }

    @Test
    void temporalJoinMinGap() {
        int minGap = 30;
        IdList idList = idList1.temporalJoin(minGap, idList2);
        System.out.println(idList);
        IdList verif = new IdList();
        verif.add(1, 80);
        verif.add(1, 70);
        verif.add(8, 40);
        verif.add(8, 50);
        verif.add(8, 80);
        if (!idList.equals(verif)){
            fail();
        }
    }


}