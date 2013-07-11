/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideState;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ListGuidesByState {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static List run(Integer guideYear, GuideState situationOfGuide) throws Exception {
        List guides = Guide.readByYearAndState(guideYear, situationOfGuide);

        Iterator iterator = guides.iterator();

        List result = new ArrayList();
        while (iterator.hasNext()) {
            result.add(InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain((Guide) iterator.next()));
        }

        return result;
    }

}