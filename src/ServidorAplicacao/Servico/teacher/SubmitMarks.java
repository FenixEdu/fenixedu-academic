package ServidorAplicacao.Servico.teacher;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoFrequentaWithInfoStudentAndPerson;
import DataBeans.InfoMark;
import DataBeans.InfoMarkWithInfoAttendAndInfoStudent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSubmitMarks;
import DataBeans.TeacherAdministrationSiteView;
import Dominio.EnrolmentEvaluation;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IPessoa;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.Mark;
import Dominio.ResponsibleFor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.Ftp;
import Util.TipoCurso;
import Util.middleware.CreateFile;

/**
 * @author Tânia Pousão
 *  
 */
public class SubmitMarks implements IServico {
    /**
     * The actor of this class.
     */
    public SubmitMarks() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "SubmitMarks";
    }

    public Object run(Integer executionCourseCode, Integer evaluationCode,
            Date evaluationDate, UserView userView)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            List notEnrolledList = null;
            List mestradoList = null;
            List infoMarksList = null;

            //execution course and execution course's site
            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, executionCourseCode);

            IPersistentSite persistentSite = sp.getIPersistentSite();
            ISite site = persistentSite.readByExecutionCourse(executionCourse);

            //evaluation
            IPersistentEvaluation persistentEvaluation = sp
                    .getIPersistentEvaluation();
            IEvaluation evaluation = (IEvaluation) persistentEvaluation
                    .readByOID(Evaluation.class, evaluationCode);

            //attend list
            IFrequentaPersistente persistentAttend = sp
                    .getIFrequentaPersistente();
            List attendList = persistentAttend
                    .readByExecutionCourse(executionCourse);

            IPersistentResponsibleFor persistentResponsibleFor = sp
                    .getIPersistentResponsibleFor();
            List professors = persistentResponsibleFor
                    .readByExecutionCourse(executionCourse);

            //employee logged
            IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
            IPessoa pessoa = pessoaPersistente.lerPessoaPorUsername(userView
                    .getUtilizador());
            IEmployee employee = readEmployee(pessoa);
            ITeacher teacher = ((ResponsibleFor) professors.get(0))
                    .getTeacher();

            MultiHashMap enrolmentEvaluationTableByDegree = getEnrolmentEvaluationsByDegree(
                    userView, executionCourse, attendList, evaluation,
                    evaluationDate, employee, teacher);

            if (enrolmentEvaluationTableByDegree.containsKey(new String(
                    "notEnrolled"))) {
                notEnrolledList = (List) enrolmentEvaluationTableByDegree
                        .get(new String("notEnrolled"));
                enrolmentEvaluationTableByDegree.remove(new String(
                        "notEnrolled"));
            }

            if (enrolmentEvaluationTableByDegree.containsKey(new String(
                    "mestrado"))) {
                mestradoList = (List) enrolmentEvaluationTableByDegree
                        .get(new String("mestrado"));
                enrolmentEvaluationTableByDegree.remove(new String("mestrado"));
            }

            if (enrolmentEvaluationTableByDegree.containsKey(new String(
                    "infoMarks"))) {
                infoMarksList = (List) enrolmentEvaluationTableByDegree
                        .get(new String("infoMarks"));
                enrolmentEvaluationTableByDegree
                        .remove(new String("infoMarks"));
            }

            List fileList = submitMarksAndCreateFiles(enrolmentEvaluationTableByDegree);

            //Send the files via FPT
            Ftp.enviarFicheiros("/DegreeGradesFtpServerConfig.properties",
                    fileList, "notas/");

            return createSiteView(site, evaluation, infoMarksList,
                    notEnrolledList, mestradoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private MultiHashMap getEnrolmentEvaluationsByDegree(UserView userView,
            IExecutionCourse executionCourse, List attendList,
            IEvaluation evaluation, Date evaluationDate, IEmployee employee,
            ITeacher teacher) throws FenixServiceException {

        try {
            MultiHashMap enrolmentEvaluationsByDegree = new MultiHashMap();
            boolean allMarksPublished = true;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentMark persistentMark = sp.getIPersistentMark();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = sp
                    .getIPersistentEnrolmentEvaluation();
            Iterator iter = attendList.iterator();

            verifyAlreadySubmittedMarks(attendList, enrolmentEvaluationDAO);

            List markList = persistentMark.readBy(evaluation);
            
            //Check if there is any mark. If not, we can not submit
            if(markList.isEmpty())
            {
                throw new FenixServiceException("errors.submitMarks.noMarks");
            }

            while (iter.hasNext()) {
                IFrequenta attend = (IFrequenta) iter.next();
                IEnrollment enrolment = attend.getEnrolment();
                IEnrolmentEvaluation enrolmentEvaluation = null;

                //check student´s degree type
                if (attend.getAluno().getDegreeType().equals(
                        TipoCurso.MESTRADO_OBJ)) {
                    //CLONER
                    //enrolmentEvaluationsByDegree.put(new String("mestrado"),
                            //Cloner.copyIFrequenta2InfoFrequenta(attend));
                    enrolmentEvaluationsByDegree.put(new String("mestrado"), InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    continue;
                }

                //check if this student is enrolled
                if (enrolment == null) {
                    enrolmentEvaluationsByDegree.put(new String("notEnrolled"), InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    //enrolmentEvaluationsByDegree.put(new String("notEnrolled"),
                            //Cloner.copyIFrequenta2InfoFrequenta(attend));
                    continue;
                }

                IMark mark = getMark(evaluation, markList, attend);
                if ((mark == null) || (mark.getMark().length() == 0)) {

                    enrolmentEvaluation = getEnrolmentEvaluationByEnrolment(
                            userView, executionCourse, enrolment,
                            evaluationDate, "NA", employee, teacher);
                    InfoMark infoMark = new InfoMark();
                    //CLONER
                    //infoMark.setInfoEvaluation(Cloner
                            //.copyIEvaluation2InfoEvaluation(evaluation));
                    infoMark.setInfoEvaluation(InfoEvaluation.newInfoFromDomain(evaluation));
                    //CLONER
                    //infoMark.setInfoFrequenta(Cloner
                            //.copyIFrequenta2InfoFrequenta(attend));
                    infoMark.setInfoFrequenta(InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    infoMark.setMark("NA");
                    enrolmentEvaluationsByDegree.put(new String("infoMarks"),
                            infoMark);

                } else {

                    enrolmentEvaluation = getEnrolmentEvaluationByEnrolment(
                            userView, executionCourse, enrolment,
                            evaluationDate, mark.getMark().toUpperCase(),
                            employee, teacher);
                    //CLONER
                    //enrolmentEvaluationsByDegree.put(new String("infoMarks"),
                            //Cloner.copyIMark2InfoMark(mark));
                    enrolmentEvaluationsByDegree.put(new String("infoMarks"), InfoMarkWithInfoAttendAndInfoStudent.newInfoFromDomain(mark));

                }

                enrolmentEvaluationsByDegree.put(enrolment
                        .getStudentCurricularPlan().getDegreeCurricularPlan()
                        .getDegree().getIdInternal(), enrolmentEvaluation);

            }
            if (!allMarksPublished) {
                throw new FenixServiceException(
                        "errors.submitMarks.allMarksNotPublished");
            }

            return enrolmentEvaluationsByDegree;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private void verifyAlreadySubmittedMarks(List attendList,
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO)
            throws ExcepcaoPersistencia, FenixServiceException {
        List enrolmentListIds = (List) CollectionUtils.collect(attendList,
                new Transformer() {

                    public Object transform(Object input) {
                        IFrequenta attend = (IFrequenta) input;
                        IEnrollment enrolment = attend.getEnrolment();
                        return enrolment == null ? null : enrolment
                                .getIdInternal();
                    }
                });

        enrolmentListIds = (List) CollectionUtils.select(enrolmentListIds,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        return arg0 != null;
                    }
                });
        List alreadySubmiteMarks = enrolmentEvaluationDAO
                .readAlreadySubmitedMarks(enrolmentListIds);

        if (!alreadySubmiteMarks.isEmpty()) {
            throw new FenixServiceException("errors.submitMarks.yetSubmited");
        }
    }

    private IMark getMark(IEvaluation evaluation, List markList,
            IFrequenta attend) {
        //                IMark mark = persistentMark.readBy(evaluation, attend);
        IMark mark = new Mark();
        mark.setAttend(attend);
        mark.setEvaluation(evaluation);
        int indexOf = markList.indexOf(mark);
        if (indexOf != -1) {
            mark = (IMark) markList.get(indexOf);
        } else {
            mark = null;
        }
        return mark;
    }

    private IEnrolmentEvaluation getEnrolmentEvaluationByEnrolment(
            UserView userView, IExecutionCourse executionCourse,
            IEnrollment enrolment, Date evaluationDate, String publishedMark,
            IEmployee employee, ITeacher teacher) throws FenixServiceException {
        ISuportePersistente sp;
        IEnrolmentEvaluation enrolmentEvaluation = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();

            //Verify if this mark has been already submited
            //verifyYetSubmitMarks(enrolment);

            List enrolmentEvaluationListTemporary = persistentEnrolmentEvaluation
                    .readEnrolmentEvaluationByEnrolmentEvaluationState(
                            enrolment, EnrolmentEvaluationState.TEMPORARY_OBJ);

            //There can exist only one enrolmentEvaluation with Temporary State
            if (enrolmentEvaluationListTemporary != null
                    && enrolmentEvaluationListTemporary.size() > 0) {
                enrolmentEvaluation = (IEnrolmentEvaluation) enrolmentEvaluationListTemporary
                        .get(0);
            } else {
                enrolmentEvaluation = new EnrolmentEvaluation();
            }

            //teacher responsible for execution course

            enrolmentEvaluation.setEnrolment(enrolment);
            persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluation);

            enrolmentEvaluation.setGrade(publishedMark);

            enrolmentEvaluation.setEnrolmentEvaluationType(enrolment
                    .getEnrolmentEvaluationType());
            enrolmentEvaluation
                    .setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
            enrolmentEvaluation
                    .setObservation(new String("Submissão da Pauta"));

            enrolmentEvaluation.setPersonResponsibleForGrade(teacher
                    .getPerson());

            enrolmentEvaluation.setEmployee(employee);

            Calendar calendar = Calendar.getInstance();
            enrolmentEvaluation.setWhen(new Timestamp(calendar
                    .getTimeInMillis()));
            enrolmentEvaluation.setGradeAvailableDate(calendar.getTime());
            if (evaluationDate != null) {
                enrolmentEvaluation.setExamDate(evaluationDate);
            } else {
                enrolmentEvaluation.setExamDate(calendar.getTime());
            }

            enrolmentEvaluation.setCheckSum("");

            return enrolmentEvaluation;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private List submitMarksAndCreateFiles(
            MultiHashMap enrolmentEvaluationTableByDegree)
            throws FenixServiceException {
        Set degrees = enrolmentEvaluationTableByDegree.keySet();
        Iterator iter = degrees.iterator();
        List fileList = new ArrayList();
        try {
            //degrees
            while (iter.hasNext()) {
                List enrolmentEvaluationsByDegree = (List) enrolmentEvaluationTableByDegree
                        .get(iter.next());
                File file = CreateFile.buildFile(enrolmentEvaluationsByDegree);
                if (file != null) {
                    fileList.add(file);
                }
            }
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private IEmployee readEmployee(IPessoa person) {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try {
            persistentEmployee = SuportePersistenteOJB.getInstance()
                    .getIPersistentEmployee();
            employee = persistentEmployee.readByPerson(person.getIdInternal()
                    .intValue());
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return employee;
    }

    private Object createSiteView(ISite site, IEvaluation evaluation,
            List marksList, List notEnrolledList, List mestradoList)
            throws FenixServiceException {

        InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();

        //infoSiteSubmitMarks.setInfoEvaluation(Cloner
                //.copyIEvaluation2InfoEvaluation(evaluation));
        infoSiteSubmitMarks.setInfoEvaluation(InfoEvaluation.newInfoFromDomain(evaluation));
        infoSiteSubmitMarks.setMarksList(marksList);

        // order errorsNotEnrolmented list by student's number
        if (notEnrolledList != null) {
            Collections.sort(notEnrolledList,
                    new BeanComparator("aluno.number"));
            infoSiteSubmitMarks.setNotEnrolmented(notEnrolledList);
        }

        // order errorsMarkNotPublished list by student's number
        if (mestradoList != null) {
            Collections.sort(mestradoList, new BeanComparator("aluno.number"));
            infoSiteSubmitMarks.setMestrado(mestradoList);
        }

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(
                new InfoSiteCommon(), site, null, null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(
                commonComponent, infoSiteSubmitMarks);
        return siteView;
    }
}