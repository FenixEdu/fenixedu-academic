/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.scientificCouncil;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseWithInfoDegree;
import DataBeans.SiteView;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class ReadCurricularCourseByOIdService implements IService {

    public ReadCurricularCourseByOIdService() {

    }

    public SiteView run(Integer curricularCourseId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentObject.readByOID(
                    CurricularCourse.class, curricularCourseId);

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);

            SiteView siteView = new SiteView(infoCurricularCourse);

            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}