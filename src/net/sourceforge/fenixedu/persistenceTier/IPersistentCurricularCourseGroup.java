/*
 * Created on 25/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public interface IPersistentCurricularCourseGroup extends IPersistentObject {

    public List readByBranchAndAreaType(Integer branchId, AreaType areaType) throws ExcepcaoPersistencia;

    public ICurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(Integer branchId,
           Integer curricularCourseId, AreaType areaType) throws ExcepcaoPersistencia;

    public ICurricularCourseGroup readByBranchAndScientificAreaAndAreaType(Integer branchId,
            Integer scientificAreaId, AreaType areaType) throws ExcepcaoPersistencia;

    public List readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

    public List readOptionalCurricularCourseGroupsFromArea(Integer areaId) throws ExcepcaoPersistencia;
}