package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;

public class InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos extends InfoDegree {
	
	public InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos() {		
		this.setInfoDegreeCurricularPlans(new ArrayList());
		this.setInfoDegreeInfos(new ArrayList());
	}
	
	public void copyFromDomain(Degree degree) {
        super.copyFromDomain(degree);
        if (degree != null) {
            for (DegreeInfo degreeInfo : degree.getDegreeInfos()) {
				InfoDegreeInfo infoDegreeInfo = InfoDegreeInfoWithDegree.newInfoFromDomain(degreeInfo);
				this.getInfoDegreeInfos().add(infoDegreeInfo);
            }
			
			for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
				if (!dcp.isBolonha()) {
                    InfoDegreeCurricularPlan infoDCP = InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(dcp);
                    this.getInfoDegreeCurricularPlans().add(infoDCP);
                }
			}
        }
    }

    public static InfoDegree newInfoFromDomain(
            Degree degree) {
		InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos infoDegree = null;
        if (degree != null) {
            infoDegree = new InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos();
            infoDegree.copyFromDomain(degree);
        }
        return infoDegree;
    }

}
