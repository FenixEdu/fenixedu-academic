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
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadPrecedencesFromDegreeCurricularPlan {

    @Atomic
    public static Map run(String degreeCurricularPlanID) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        Map finalListOfInfoPrecedences = new HashMap();

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        Collection<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

        for (CurricularCourse curricularCourse : curricularCourses) {
            Collection precedences = curricularCourse.getPrecedences();
            putInMap(finalListOfInfoPrecedences, curricularCourse, precedences);
        }

        return finalListOfInfoPrecedences;
    }

    private static void putInMap(Map finalListOfInfoPrecedences, CurricularCourse curricularCourse, Collection precedences) {

        if (!precedences.isEmpty()) {
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);

            List<InfoPrecedence> infoPrecedences = clone(precedences);

            finalListOfInfoPrecedences.put(infoCurricularCourse, infoPrecedences);
        }
    }

    private static List<InfoPrecedence> clone(Collection<Precedence> precedences) {

        List<InfoPrecedence> result = new ArrayList<InfoPrecedence>();

        for (Precedence precedence : precedences) {
            InfoPrecedence infoPrecedence = InfoPrecedenceWithRestrictions.newInfoFromDomain(precedence);
            result.add(infoPrecedence);
        }

        return result;
    }
}