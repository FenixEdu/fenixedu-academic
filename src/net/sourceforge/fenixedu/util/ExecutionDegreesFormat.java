/*
 * Created on 10/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

/**
 * @author Tânia Pousão
 *  
 */
public class ExecutionDegreesFormat extends FenixUtil {
    public static List buildExecutionDegreeLabelValueBean(List executionDegreeList, MessageResources messageResources) {
        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();

            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            String degreeType = null;
            
            if(messageResources != null) {
                degreeType = messageResources.getMessage(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                        .toString());
                
            }
            
            if(degreeType == null)
                degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString();

            
            name = degreeType
                    + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? " - "
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, infoExecutionDegree.getIdInternal()
                    .toString()));
        }
        return executionDegreeLabels;
    }

    public static List buildExecutionDegreeLabelValueWithNameBean(List executionDegreeList) {
        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? " - "
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, name + "~"
                    + infoExecutionDegree.getIdInternal().toString()));
        }
        return executionDegreeLabels;
    }

    private static boolean duplicateInfoDegree(List executionDegreeList,
            InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }
}