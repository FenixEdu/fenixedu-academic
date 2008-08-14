/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class ReadCurriculumByOIdService extends Service {

    public SiteView run(Integer curriculumId) throws FenixServiceException {
	Curriculum curriculum = rootDomainObject.readCurriculumByOID(curriculumId);
	InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
	SiteView siteView = new SiteView(infoCurriculum);

	return siteView;

    }

}