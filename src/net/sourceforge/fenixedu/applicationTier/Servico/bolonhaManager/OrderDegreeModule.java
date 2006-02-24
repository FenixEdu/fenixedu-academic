package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class OrderDegreeModule extends Service {

    public void run(final Integer contextID, final Integer position) throws ExcepcaoPersistencia, FenixServiceException {
        if (contextID == null) {
            throw new FenixServiceException();
        }
        
        final Context context = (Context) persistentObject.readByOID(Context.class, contextID);
        if (context == null) {
            throw new FenixServiceException("error.noContext");
        }
        
        context.getCourseGroup().orderChild(context, position);
    }
    
}
