/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ListGuidesByState extends Service {

    public List run(Integer guideYear, GuideState situationOfGuide) throws Exception {
	List guides = Guide.readByYearAndState(guideYear, situationOfGuide);

	Iterator iterator = guides.iterator();

	List result = new ArrayList();
	while (iterator.hasNext()) {
	    result.add(InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain((Guide) iterator.next()));
	}

	return result;
    }

}