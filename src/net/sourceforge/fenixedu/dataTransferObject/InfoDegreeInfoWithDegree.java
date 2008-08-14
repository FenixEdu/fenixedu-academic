package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DegreeInfo;

/**
 * @author Fernanda Quitério Created on 27/Jul/2004
 * 
 */
public class InfoDegreeInfoWithDegree extends InfoDegreeInfo {

    public void copyFromDomain(DegreeInfo degreeInfo) {
	super.copyFromDomain(degreeInfo);
	if (degreeInfo != null) {
	    setInfoDegree(InfoDegree.newInfoFromDomain(degreeInfo.getDegree()));
	}
    }

    public static InfoDegreeInfo newInfoFromDomain(DegreeInfo degreeInfo) {
	InfoDegreeInfoWithDegree infoDegreeInfoWithDegree = null;
	if (degreeInfo != null) {
	    infoDegreeInfoWithDegree = new InfoDegreeInfoWithDegree();
	    infoDegreeInfoWithDegree.copyFromDomain(degreeInfo);
	}
	return infoDegreeInfoWithDegree;
    }

}