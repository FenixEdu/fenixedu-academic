/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Factory.ScientificCouncilComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class ScientificCouncilComponentService implements IServico {

    private static ScientificCouncilComponentService _servico = new ScientificCouncilComponentService();

    /**
     * The actor of this class.
     */

    private ScientificCouncilComponentService() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ScientificCouncilComponentService";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static ScientificCouncilComponentService getService() {
        return _servico;
    }

    public SiteView run(ISiteComponent bodyComponent, Integer degreeId, Integer curricularYear,
            Integer degreeCurricularPlanId) throws FenixServiceException {

        ScientificCouncilComponentBuilder componentBuilder = ScientificCouncilComponentBuilder
                .getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, degreeId, curricularYear,
                degreeCurricularPlanId);
        SiteView siteView = new SiteView();
        siteView.setComponent(bodyComponent);

        return siteView;
    }

}