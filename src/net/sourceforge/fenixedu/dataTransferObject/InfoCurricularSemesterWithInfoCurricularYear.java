/*
 * Created on 29/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularSemester;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCurricularSemesterWithInfoCurricularYear extends InfoCurricularSemester {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoCurricularSemester#copyFromDomain(Dominio.CurricularSemester)
     */
    public void copyFromDomain(CurricularSemester curricularSemester) {
        super.copyFromDomain(curricularSemester);
        if (curricularSemester != null) {
            setInfoCurricularYear(InfoCurricularYear.newInfoFromDomain(curricularSemester
                    .getCurricularYear()));
        }
    }

    public static InfoCurricularSemester newInfoFromDomain(CurricularSemester curricularSemester) {
        InfoCurricularSemesterWithInfoCurricularYear infoCurricularSemester = null;
        if (curricularSemester != null) {
            infoCurricularSemester = new InfoCurricularSemesterWithInfoCurricularYear();
            infoCurricularSemester.copyFromDomain(curricularSemester);
        }
        return infoCurricularSemester;
    }
}