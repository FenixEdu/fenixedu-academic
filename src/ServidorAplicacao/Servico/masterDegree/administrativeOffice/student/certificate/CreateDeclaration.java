package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Specialization;
import Util.StudentCurricularPlanState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CreateDeclaration implements IService {

    public List run(InfoStudent infoStudent, Specialization specialization) throws Exception {

        List studentCurricularPlanList = null;
        List infoStudentCurricularPlanList = new ArrayList();

        ISuportePersistente sp = null;

        sp = SuportePersistenteOJB.getInstance();

        try {
            studentCurricularPlanList = sp.getIStudentCurricularPlanPersistente()
                    .readAllByStudentNumberAndSpecialization(infoStudent.getNumber(),
                            infoStudent.getDegreeType(), specialization);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {

            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);

            throw newEx;
        }

        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList.add(Cloner
                        .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }

    public List run(InfoStudent infoStudent, Specialization specialization,
            StudentCurricularPlanState state) throws Exception {

        List studentCurricularPlanList = null;
        List infoStudentCurricularPlanList = new ArrayList();

        ISuportePersistente sp = null;

        sp = SuportePersistenteOJB.getInstance();

        try {
            studentCurricularPlanList = sp.getIStudentCurricularPlanPersistente()
                    .readAllByStudentNumberAndSpecializationAndState(infoStudent.getNumber(),
                            infoStudent.getDegreeType(), specialization, state);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {

            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);

            throw newEx;
        }

        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList.add(Cloner
                        .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }

    //FIXME change paraemter states to List type, when berserk's reflection bug
    // is fixed
    public List run(InfoStudent infoStudent, Specialization specialization, ArrayList states)
            throws Exception {

        List studentCurricularPlanList = new ArrayList();
        List studentCurricularPlanListTmp = null;
        List infoStudentCurricularPlanList = new ArrayList();

        ISuportePersistente sp = null;

        sp = SuportePersistenteOJB.getInstance();

        for (Iterator iter = states.iterator(); iter.hasNext();) {
            StudentCurricularPlanState state = (StudentCurricularPlanState) iter.next();

            try {
                studentCurricularPlanListTmp = sp.getIStudentCurricularPlanPersistente()
                        .readAllByStudentNumberAndSpecializationAndState(infoStudent.getNumber(),
                                infoStudent.getDegreeType(), specialization, state);

            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            } catch (ExcepcaoPersistencia ex) {
                FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
                throw newEx;
            }

            for (Iterator iterator = studentCurricularPlanListTmp.iterator(); iterator.hasNext();) {
                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();

                studentCurricularPlanList.add(studentCurricularPlan);
            }

        }

        if (studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList.add(Cloner
                        .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }
}