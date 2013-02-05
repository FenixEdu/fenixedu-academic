/*
 * Created on Mar 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;

/**
 * @author Luis Cruz e Sara Ribeiro
 * 
 */
public class ComparatorByNameForInfoExecutionDegree implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) obj1;
        InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) obj2;

        String name =
                "" + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString()
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
        String name2 =
                "" + infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString()
                        + infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
        return name.compareToIgnoreCase(name2);
    }

}