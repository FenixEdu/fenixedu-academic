/*
 * Created on Dec 14, 2003 by jpvl
 *
 */
package ServidorAplicacao.Servico.department.professorship;

import ServidorAplicacao.Servico.teacher.DeleteTeacher;

/**
 * @author jpvl
 */
public class RemoveProfessorship extends DeleteTeacher {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.teacher.DeleteTeacher#canDeleteResponsibleFor()
     */
    protected boolean canDeleteResponsibleFor() {
        return true;
    }
}