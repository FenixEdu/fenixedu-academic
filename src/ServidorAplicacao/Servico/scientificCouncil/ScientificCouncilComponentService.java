/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.scientificCouncil;

import DataBeans.ISiteComponent;
import DataBeans.SiteView;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.ScientificCouncilComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

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