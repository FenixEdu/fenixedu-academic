package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeInfo;

public class InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos extends InfoDegree {
	
	public InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos() {		
		this.setInfoDegreeCurricularPlans(new ArrayList());
		this.setInfoDegreeInfos(new ArrayList());
	}
	
	public void copyFromDomain(IDegree degree) {
        super.copyFromDomain(degree);
        if (degree != null) {
            for (IDegreeInfo degreeInfo : degree.getDegreeInfos()) {
				InfoDegreeInfo infoDegreeInfo = InfoDegreeInfoWithDegree.newInfoFromDomain(degreeInfo);
				this.getInfoDegreeInfos().add(infoDegreeInfo);
            }
			
			for (IDegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
				InfoDegreeCurricularPlan infoDCP = InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(dcp);
				this.getInfoDegreeCurricularPlans().add(infoDCP);
			}
        }
    }

    public static InfoDegree newInfoFromDomain(
            IDegree degree) {
		InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos infoDegree = null;
        if (degree != null) {
            infoDegree = new InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos();
            infoDegree.copyFromDomain(degree);
        }
        return infoDegree;
    }

}
