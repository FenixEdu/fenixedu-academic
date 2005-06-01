package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Branch extends Branch_Base {
    /**
     * @author Nuno Correia & Ricardo Rodrigues
     */

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IBranch) {
            IBranch branch = (IBranch) obj;
            result = this.getCode().equals(branch.getCode())
                    && this.getDegreeCurricularPlan().equals(branch.getDegreeCurricularPlan());
        }
        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "name = " + this.getName() + "; ";
        result += "code = " + this.getCode() + "; ";
        result += "acronym = " + this.getAcronym() + "]\n";
        return result;
    }

    /**
     * @author Fernanda Quit�rio
     */
    public Boolean representsCommonBranch() {

        if (this.getBranchType().equals(net.sourceforge.fenixedu.domain.branch.BranchType.valueOf("COMMON"))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List getAreaCurricularCourseGroups(final AreaType areaType) {

        return (List) CollectionUtils.select(getAreaCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) arg0;
                return curricularCourseGroup.getAreaType().equals(areaType);
            }
        });
    }

}