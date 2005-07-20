/*
 * Created on 29/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class EditDegree implements IService {

	
    public EditDegree() {
    }


    public void run(InfoDegree newInfoDegree) throws FenixServiceException {

        try {

			ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
			ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();
			IDegree oldDegree = (IDegree)persistentDegree.readByOID(Degree.class,newInfoDegree.getIdInternal());

            if (oldDegree == null) {
                throw new NonExistingServiceException();
            }
			
			oldDegree.edit(newInfoDegree.getNome(), newInfoDegree.getNameEn(),
					newInfoDegree.getSigla(), newInfoDegree.getTipoCurso());

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
			
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}