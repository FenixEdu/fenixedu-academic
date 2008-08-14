/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.ScientificCouncilComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class ScientificCouncilComponentService extends Service {

    public SiteView run(ISiteComponent bodyComponent, Integer degreeId, Integer curricularYear, Integer degreeCurricularPlanId)
	    throws FenixServiceException {

	ScientificCouncilComponentBuilder componentBuilder = ScientificCouncilComponentBuilder.getInstance();
	bodyComponent = componentBuilder.getComponent(bodyComponent, degreeId, curricularYear, degreeCurricularPlanId);
	SiteView siteView = new SiteView();
	siteView.setComponent(bodyComponent);

	return siteView;
    }

}