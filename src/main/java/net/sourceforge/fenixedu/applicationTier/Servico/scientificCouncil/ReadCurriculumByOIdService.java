/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.Curriculum;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class ReadCurriculumByOIdService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Atomic
    public static SiteView run(String curriculumId) throws FenixServiceException {
        Curriculum curriculum = FenixFramework.getDomainObject(curriculumId);
        InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
        SiteView siteView = new SiteView(infoCurriculum);

        return siteView;

    }

}