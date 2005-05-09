/*
 * Created on 25/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public interface IPersistentCurricularCourseGroup extends IPersistentObject {

    public List readByBranchAndAreaType(IBranch branch, AreaType areaType) throws ExcepcaoPersistencia;

    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia;

    public ICurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(IBranch branch,
            ICurricularCourse curricularCourse, AreaType areaType) throws ExcepcaoPersistencia;

    public ICurricularCourseGroup readByBranchAndScientificAreaAndAreaType(IBranch branch,
            IScientificArea scientificArea, AreaType areaType) throws ExcepcaoPersistencia;

    public List readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

    public List readOptionalCurricularCourseGroupsFromArea(IBranch area) throws ExcepcaoPersistencia;
}