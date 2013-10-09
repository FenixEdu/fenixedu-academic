/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ListGuidesByState {

    @Atomic
    public static List run(Integer guideYear, GuideState situationOfGuide) throws Exception {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        List guides = Guide.readByYearAndState(guideYear, situationOfGuide);

        Iterator iterator = guides.iterator();

        List result = new ArrayList();
        while (iterator.hasNext()) {
            result.add(InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain((Guide) iterator.next()));
        }

        return result;
    }

}