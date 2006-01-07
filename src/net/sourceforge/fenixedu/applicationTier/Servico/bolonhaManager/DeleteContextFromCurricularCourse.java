/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteContextFromCurricularCourse implements IService {

    public void run(CurricularCourse curricularCourse, Context context) throws ExcepcaoPersistencia {       
        curricularCourse.deleteContext(context);
    }
}
