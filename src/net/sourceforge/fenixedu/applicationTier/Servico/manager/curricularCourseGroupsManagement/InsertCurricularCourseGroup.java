/*
 * Created on Jul 28, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.AreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IAreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.OptionalCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class InsertCurricularCourseGroup implements IService {

    public void run(Integer groupId, String name, Integer branchId, Integer minimumValue,
            Integer maximumValue, String areaType, String className) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
        IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                .getIPersistentCurricularCourseGroup();

        IBranch branch = (IBranch) persistentBranch.readByOID(Branch.class, branchId);
        if (branch == null) {
            throw new InvalidArgumentsServiceException();
        }

        ICurricularCourseGroup curricularCourseGroup = null;
        if (groupId != null) {
            curricularCourseGroup = (ICurricularCourseGroup) persistentCurricularCourseGroup.readByOID(
                    CurricularCourseGroup.class, groupId);
            if (curricularCourseGroup == null) {
                throw new InvalidArgumentsServiceException();
            }
        } else {
            curricularCourseGroup = getInstance(className);
        }

        persistentCurricularCourseGroup.simpleLockWrite(curricularCourseGroup);
        curricularCourseGroup.setBranch(branch);
        curricularCourseGroup.setName(name);

        if (curricularCourseGroup instanceof IAreaCurricularCourseGroup) {
            curricularCourseGroup.setAreaType(AreaType.valueOf(areaType));
            curricularCourseGroup.setMaximumCredits(maximumValue);
            curricularCourseGroup.setMinimumCredits(minimumValue);
        } else {
            curricularCourseGroup.setMaximumNumberOfOptionalCourses(maximumValue);
            curricularCourseGroup.setMinimumNumberOfOptionalCourses(minimumValue);
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
