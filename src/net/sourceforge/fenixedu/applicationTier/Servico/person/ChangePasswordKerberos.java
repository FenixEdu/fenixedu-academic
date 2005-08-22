package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.kerberos.UpdateKerberos;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangePasswordKerberos implements IService {

    public void run(IUserView userView, String oldPassword, String newPassword) throws ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPerson person = suportePersistente.getIPessoaPersistente().lerPessoaPorUsername(
                userView.getUtilizador());

        person.changePassword(oldPassword, newPassword);
        
        UpdateKerberos.changeKerberosPass(userView.getUtilizador(), newPassword);
    	person.setIsPassInKerberos(true);
    }
}