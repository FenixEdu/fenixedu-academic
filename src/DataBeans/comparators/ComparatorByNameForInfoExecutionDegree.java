/*
 * Created on Mar 14, 2003
 *
 */
package DataBeans.comparators;

import java.util.Comparator;

import DataBeans.InfoExecutionDegree;

/**
 * @author Luis Cruz e Sara Ribeiro
 *  
 */
public class ComparatorByNameForInfoExecutionDegree implements Comparator {

    public int compare(Object obj1, Object obj2) {
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) obj1;
        InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) obj2;

        String name = ""
                + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                        .getTipoCurso()
                + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
        String name2 = ""
                + infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                        .getTipoCurso()
                + infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
        return name.compareToIgnoreCase(name2);
    }

}