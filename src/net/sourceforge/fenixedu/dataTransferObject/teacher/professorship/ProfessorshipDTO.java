/**
* Nov 30, 2005
*/
package net.sourceforge.fenixedu.dataTransferObject.teacher.professorship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ProfessorshipDTO {
    
    public ProfessorshipDTO(IProfessorship professorship) {
        setProfessorship(professorship);
    }

    IProfessorship professorship;

    public IProfessorship getProfessorship() {
        return professorship;
    }

    public void setProfessorship(IProfessorship professorship) {
        this.professorship = professorship;
    }

    public List<String> getDegreeSiglas() {
        List<String> degreeSiglas = new ArrayList();
        for (Iterator iter = getProfessorship().getExecutionCourse()
                .getAssociatedCurricularCourses().iterator(); iter.hasNext();) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            String degreeSigla = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
            if (!degreeSiglas.contains(degreeSigla)) {
                degreeSiglas.add(degreeSigla);
            }
        }
        return degreeSiglas;
    }
}


