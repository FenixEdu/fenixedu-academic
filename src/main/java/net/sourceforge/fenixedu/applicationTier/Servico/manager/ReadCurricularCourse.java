/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerOrSeminariesCoordinatorFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */
public class ReadCurricularCourse {

    /**
     * Executes the service. Returns the current InfoCurricularCourse.
     * 
     * @throws ExcepcaoPersistencia
     */
    protected InfoCurricularCourse run(Integer idInternal) throws FenixServiceException {
        CurricularCourse curricularCourse;
        curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(idInternal);

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
    }

    // Service Invokers migrated from Berserk

    private static final ReadCurricularCourse serviceInstance = new ReadCurricularCourse();

    @Service
    public static InfoCurricularCourse runReadCurricularCourse(Integer idInternal) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerOrSeminariesCoordinatorFilter.instance.execute(idInternal);
            return serviceInstance.run(idInternal);
        } catch (NotAuthorizedException ex1) {
            try {
                OperatorAuthorizationFilter.instance.execute();
                return serviceInstance.run(idInternal);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}