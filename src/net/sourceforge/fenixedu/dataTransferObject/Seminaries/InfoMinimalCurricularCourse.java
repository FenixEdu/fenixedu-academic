/**
 * @author Goncalo Luiz, 29/8/2005
 */

package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;

public class InfoMinimalCurricularCourse extends InfoCurricularCourse{

    String name;

    public InfoMinimalCurricularCourse(CurricularCourse curricularCourse) {
		super(curricularCourse);
	}

	public void copyFromDomain(CurricularCourse curricularCourse) {
        if (curricularCourse != null) {
            setName(curricularCourse.getName());            
        }
    }

    public static InfoMinimalCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoMinimalCurricularCourse infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoMinimalCurricularCourse(curricularCourse);
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        return infoCurricularCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
