/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;

import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.GratuitySituation;
import Dominio.GratuityValues;
import Dominio.IEmployee;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class EditGratuitySituationById implements IServico {

    private static EditGratuitySituationById servico = new EditGratuitySituationById();

    /**
     * The singleton access method of this class.
     */
    public static EditGratuitySituationById getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private EditGratuitySituationById() {
    }

    /**
     * Returns The Service Name
     */
    public final String getNome() {
        return "EditGratuitySituationById";
    }

    public Object run(InfoGratuitySituation infoGratuitySituation)
            throws FenixServiceException {
        ISuportePersistente sp = null;
        try {
            if (infoGratuitySituation == null) {
                throw new FenixServiceException();
            }

            sp = SuportePersistenteOJB.getInstance();
            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            IGratuitySituation gratuitySituation = new GratuitySituation();
            gratuitySituation.setIdInternal(infoGratuitySituation
                    .getIdInternal());

            IGratuityValues gratuityValues = new GratuityValues();
            gratuityValues.setIdInternal(infoGratuitySituation
                    .getInfoGratuityValues().getIdInternal());
            gratuitySituation.setGratuityValues(gratuityValues);

            IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
            studentCurricularPlan.setIdInternal(infoGratuitySituation
                    .getInfoStudentCurricularPlan().getIdInternal());
            gratuitySituation.setStudentCurricularPlan(studentCurricularPlan);

            gratuitySituation = persistentGratuitySituation
                    .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                            studentCurricularPlan, gratuityValues);
            boolean isNew = false;
            if (gratuitySituation == null) //it doesn't exist in database, then
                                           // write it
            {
                gratuitySituation = new GratuitySituation();

                persistentGratuitySituation.simpleLockWrite(gratuitySituation);
                isNew = true;

                //gratuity values

                IPersistentGratuityValues persistentGratuityValues = sp
                        .getIPersistentGratuityValues();
                gratuityValues = (IGratuityValues) persistentGratuityValues
                        .readByOID(GratuityValues.class, infoGratuitySituation
                                .getInfoGratuityValues().getIdInternal());
                gratuitySituation.setGratuityValues(gratuityValues);

                //student curricular plan

                IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                        .getIStudentCurricularPlanPersistente();
                studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                        .readByOID(StudentCurricularPlan.class,
                                infoGratuitySituation
                                        .getInfoStudentCurricularPlan()
                                        .getIdInternal());
                gratuitySituation
                        .setStudentCurricularPlan(studentCurricularPlan);
            }
            if (!isNew) {
                persistentGratuitySituation.simpleLockWrite(gratuitySituation);
            }
            //employee who made register
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPessoa person = persistentPerson
                    .lerPessoaPorUsername(infoGratuitySituation
                            .getInfoEmployee().getPerson().getUsername());
            if (person != null) {
                IPersistentEmployee persistentEmployee = sp
                        .getIPersistentEmployee();
                IEmployee employee = persistentEmployee.readByPerson(person
                        .getIdInternal().intValue());
                gratuitySituation.setEmployee(employee);
            }

            Calendar now = Calendar.getInstance();
            gratuitySituation.setWhen(now.getTime());

            gratuitySituation.setExemptionDescription(infoGratuitySituation
                    .getExemptionDescription());
            gratuitySituation.setExemptionPercentage(infoGratuitySituation
                    .getExemptionPercentage());
            gratuitySituation.setExemptionType(infoGratuitySituation
                    .getExemptionType());

            infoGratuitySituation = Cloner
                    .copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }

        return infoGratuitySituation;
    }
}