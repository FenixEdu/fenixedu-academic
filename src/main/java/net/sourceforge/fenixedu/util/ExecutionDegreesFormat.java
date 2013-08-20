/*
 * Created on 10/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.struts.Globals;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Tânia Pousão
 * 
 */
public class ExecutionDegreesFormat extends FenixUtil {
    public static List buildExecutionDegreeLabelValueBean(List executionDegreeList, MessageResources messageResources,
            HttpServletRequest request) {
        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();

            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            String degreeType = null;

            if (messageResources != null) {
                final Locale locale = Language.getLocale();
                degreeType =
                        messageResources.getMessage(locale, infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                                .getDegreeType().name());
            }

            if (degreeType == null) {
                degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString();
            }

            name = degreeType + " em " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? " - "
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, infoExecutionDegree.getExternalId().toString()));
        }
        return executionDegreeLabels;
    }

    public static List buildExecutionDegreeLabelValueWithNameBean(List executionDegreeList) {
        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? " - "
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, name + "~" + infoExecutionDegree.getExternalId().toString()));
        }
        return executionDegreeLabels;
    }

    private static boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2))) {
                return true;
            }

        }
        return false;
    }

    public static List<LabelValueBean> buildLabelValueBeansForExecutionDegree(List<ExecutionDegree> executionDegrees,
            MessageResources messageResources, HttpServletRequest request) {

        final Locale locale = (Locale) request.getAttribute(Globals.LOCALE_KEY);

        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (final ExecutionDegree executionDegree : executionDegrees) {

            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            final String degreeName = executionDegree.getDegree().getNameFor(executionYear).getContent();
            final String degreeType =
                    messageResources.getMessage(locale, executionDegree.getDegreeCurricularPlan().getDegree().getDegreeType()
                            .name());

            String name = degreeType + " em " + degreeName;
            name +=
                    (addDegreeCurricularPlanName(executionDegree, executionDegrees)) ? " - "
                            + executionDegree.getDegreeCurricularPlan().getName() : "";

            result.add(new LabelValueBean(name, executionDegree.getExternalId().toString()));
        }

        return result;
    }

    private static boolean addDegreeCurricularPlanName(final ExecutionDegree selectedExecutionDegree,
            final List<ExecutionDegree> executionDegrees) {

        for (final ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegree() == selectedExecutionDegree.getDegree() && executionDegree != selectedExecutionDegree) {
                return true;
            }
        }
        return false;
    }
}