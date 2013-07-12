package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.precedences.InfoPrecedence;
import net.sourceforge.fenixedu.dataTransferObject.precedences.InfoPrecedenceWithRestrictions;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadPrecedencesFromDegreeCurricularPlan {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static Map run(String degreeCurricularPlanID) throws FenixServiceException {

        Map finalListOfInfoPrecedences = new HashMap();

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        List curricularCourses = degreeCurricularPlan.getCurricularCourses();

        int size = curricularCourses.size();

        for (int i = 0; i < size; i++) {
            CurricularCourse curricularCourse = (CurricularCourse) curricularCourses.get(i);
            Collection precedences = curricularCourse.getPrecedences();
            putInMap(finalListOfInfoPrecedences, curricularCourse, precedences);
        }

        return finalListOfInfoPrecedences;
    }

    private static void putInMap(Map finalListOfInfoPrecedences, CurricularCourse curricularCourse, Collection precedences) {

        if (!precedences.isEmpty()) {
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);

            List infoPrecedences = clone(precedences);

            finalListOfInfoPrecedences.put(infoCurricularCourse, infoPrecedences);
        }
    }

    private static List clone(Collection precedences) {

        List result = new ArrayList();

        int size = precedences.size();

        for (int i = 0; i < size; i++) {
            Precedence precedence = (Precedence) precedences.get(i);
            InfoPrecedence infoPrecedence = InfoPrecedenceWithRestrictions.newInfoFromDomain(precedence);
            result.add(infoPrecedence);
        }

        return result;
    }
}