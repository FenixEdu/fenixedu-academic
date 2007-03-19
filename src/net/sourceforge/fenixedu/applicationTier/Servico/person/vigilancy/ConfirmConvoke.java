package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ConfirmConvoke extends Service {

    public void run(OtherCourseVigilancy convoke) throws ExcepcaoPersistencia {
        convoke.setConfirmed(true);
    }

}
