/*
 * Created on 2004/02/25
 *  
 */
package net.sourceforge.fenixedu.framework;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixRemoteServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz
 *  
 */
public class JBossIntegrationTest {

    public static void main(String[] args) {
        try {
            Object AuthenticationArgs[] = { "l45498", "pass", Autenticacao.EXTRANET };
            IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", AuthenticationArgs);

            InfoStudent infoStudent = new InfoStudent();
            infoStudent.setIdInternal(new Integer(1742));

            Object[] serviceArgs = { infoStudent, new Integer(36412) };
            ServiceManagerServiceFactory.executeService(userView, "DeleteStudentAttendingCourse",
                    serviceArgs);
        } catch (Exception ex) {
            //ex.printStackTrace();

            System.out.println("\n\n\n\n\nJBossIntegrationTest ex= " + ex.getClass().getName());
            System.out.println("JBossIntegrationTest ex= " + ex.getMessage());
            ex.printStackTrace();
            if (ex instanceof FenixRemoteServiceException) {
                System.out.println("JBossIntegrationTest ex= "
                        + ((FenixRemoteServiceException) ex).getCauseClassName());
                System.out.println("JBossIntegrationTest ex= "
                        + ((FenixRemoteServiceException) ex).getCausePackageName());
            }
        }

    }

}