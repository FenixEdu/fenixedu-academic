/*
 * Created on 10/Set/2003
 */
package DataBeans.comparators;

import java.util.Comparator;

import DataBeans.InfoDegree;

/**
 * @author lmac1
 */
public class ComparatorByNameForInfoDegree implements Comparator {

    public int compare(Object obj1, Object obj2) {
        InfoDegree infoDegree = (InfoDegree) obj1;
        InfoDegree infoDegree2 = (InfoDegree) obj2;

        String name = "" + infoDegree.getTipoCurso().getTipoCurso() + infoDegree.getNome();
        String name2 = "" + infoDegree2.getTipoCurso().getTipoCurso() + infoDegree2.getNome();
        return name.compareToIgnoreCase(name2);
    }

}

