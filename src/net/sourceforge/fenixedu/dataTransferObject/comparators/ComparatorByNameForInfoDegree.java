/*
 * Created on 10/Set/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;

/**
 * @author lmac1
 */
public class ComparatorByNameForInfoDegree implements Comparator {

    public int compare(Object obj1, Object obj2) {
        InfoDegree infoDegree = (InfoDegree) obj1;
        InfoDegree infoDegree2 = (InfoDegree) obj2;

        String name = "" + infoDegree.getTipoCurso().toString() + infoDegree.getNome();
        String name2 = "" + infoDegree2.getTipoCurso().toString() + infoDegree2.getNome();
        return name.compareToIgnoreCase(name2);
    }

}

