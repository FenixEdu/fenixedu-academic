/*
 * Created on Jul 28, 2004
 *
 */
package ServidorAplicacao.Servico.manager.curricularCourseGroupsManagement;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.AreaCurricularCourseGroup;
import Dominio.Branch;
import Dominio.CurricularCourseGroup;
import Dominio.IBranch;
import Dominio.ICurricularCourseGroup;
import Dominio.OptionalCurricularCourseGroup;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;

/**
 * @author João Mota
 *  
 */
public class InsertCurricularCourseGroup implements IService {

    /**
     *  
     */
    public InsertCurricularCourseGroup() {
    }

    public void run(Integer groupId, String name, Integer branchId, Integer minimumValue,
            Integer maximumValue, Integer areaType, String className) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
            IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                    .getIPersistentCurricularCourseGroup();

            IBranch branch = (IBranch) persistentBranch.readByOID(Branch.class, branchId);
            if (branch == null) {
                throw new InvalidArgumentsServiceException();
            }
            ICurricularCourseGroup curricularCourseGroup = null;
            if (groupId != null) {
                curricularCourseGroup = (ICurricularCourseGroup) persistentCurricularCourseGroup
                        .readByOID(CurricularCourseGroup.class, groupId);
                if (curricularCourseGroup == null) {
                    throw new InvalidArgumentsServiceException();
                }
            } else {

                curricularCourseGroup = getInstance(className);
            }

            persistentCurricularCourseGroup.simpleLockWrite(curricularCourseGroup);
            curricularCourseGroup.setBranch(branch);
            curricularCourseGroup.setName(name);
            if (curricularCourseGroup instanceof AreaCurricularCourseGroup) {
                curricularCourseGroup.setAreaType(new AreaType(areaType));
                curricularCourseGroup.setMaximumCredits(maximumValue);
                curricularCourseGroup.setMinimumCredits(minimumValue);
            } else {
                curricularCourseGroup.setMaximumNumberOfOptionalCourses(maximumValue);
                curricularCourseGroup.setMinimumNumberOfOptionalCourses(minimumValue);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);

        }

    }

    /**
     * @param className
     * @return
     */
    private ICurricularCourseGroup getInstance(String className) {
        if (className.equals(AreaCurricularCourseGroup.class.getName())) {
            return new AreaCurricularCourseGroup();
        }
        return new OptionalCurricularCourseGroup();
    }

}