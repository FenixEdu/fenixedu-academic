package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.InfoAttendsSummary;
import DataBeans.InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment;
import DataBeans.InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoDegreeCurricularPlanWithDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriod;
import DataBeans.InfoForReadStudentsWithAttendsByExecutionCourse;
import DataBeans.InfoFrequenta;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoShift;
import DataBeans.InfoShiftWithInfoExecutionCourseAndInfoLessons;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentGroup;
import DataBeans.InfoStudentGroupWithInfoShift;
import DataBeans.InfoStudentWithInfoPerson;
import DataBeans.TeacherAdministrationSiteView;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.ISite;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Turno;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AttendacyStateSelectionType;
import Util.EnrolmentEvaluationType;
import Util.TipoAula;

/**
 * @author André Fernandes / João Brito
 */
public class ReadStudentsWithAttendsByExecutionCourse implements IService {

    public ReadStudentsWithAttendsByExecutionCourse() {

    }
    
    
    //doesnt allow an empty list 
    private IStudentCurricularPlan GetLastCurricularPlan(List cps){
        Iterator i = cps.iterator();
        IStudentCurricularPlan lastCP = (IStudentCurricularPlan)cps.get(0);
        
        while (i.hasNext()){
            IStudentCurricularPlan cp = (IStudentCurricularPlan)i.next(); 
            if(cp.getStartDate().after(lastCP.getStartDate()))
                lastCP = cp;
        }
        return lastCP;
    }
    

    public Object run(Integer executionCourseCode, List curricularPlansIds, List enrollmentTypeFilters, List shiftIds) throws ExcepcaoInexistente,
            FenixServiceException{
        ISite site = null;
        try {
            final ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            final IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();

            final IExecutionCourse executionCourse = (IExecutionCourse)sp.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,executionCourseCode);
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(executionCourse);
            
            IPersistentSite persistentSite = sp.getIPersistentSite();
            site = persistentSite.readByExecutionCourse(executionCourse);

            List attends = executionCourse.getAttends();
            
            List allDegreeCurricularPlans = getDegreeCurricularPlansFromAttends(attends);
            List allShifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
            List groupProperties = executionCourse.getGroupProperties();
            
            Map studentGroupsMap = getStudentGroupsMapFromGroupPropertiesList(groupProperties,sp);
            
            InfoAttendsSummary infoAttendsSummary = new InfoAttendsSummary();
            Map enrollmentDistribution = new HashMap();
            List enrollmentDistributionKeys = new ArrayList();

            // filter by Course
            if (curricularPlansIds != null)
            {
                final List dcpIds = new ArrayList();
                dcpIds.addAll(curricularPlansIds);
                
                Predicate pCourses = new Predicate()
                {
                    public boolean evaluate (Object o)
                    {
                        IFrequenta attendance = (IFrequenta)o;
                        
                        List scps = attendance.getAluno().getStudentCurricularPlans();
                        
                        if(scps != null && !scps.isEmpty())
                        {
                            IStudentCurricularPlan lastSCP = getStudentCurricularPlanFromAttends(attendance);

	                        final Integer lastDCPId = lastSCP.getDegreeCurricularPlan().getIdInternal();
	                        
	                        return dcpIds.contains(lastDCPId);
                        }
                        else
                            return false;
                    }
                };
                
                attends=(List)CollectionUtils.select(attends,pCourses);
            }
            
            // filter by Enrollment type
            if (enrollmentTypeFilters != null) 
            {
                boolean enrolledFilter = enrollmentTypeFilters.contains(AttendacyStateSelectionType.ENROLLED);
                boolean notEnrolledFilter = enrollmentTypeFilters.contains(AttendacyStateSelectionType.NOT_ENROLLED);
                boolean improvementFilter = enrollmentTypeFilters.contains(AttendacyStateSelectionType.IMPROVEMENT);
                
                List newAttends = new ArrayList();
                Iterator attendsIterator = attends.iterator();
                while(attendsIterator.hasNext()){
                    IFrequenta attendacy = (IFrequenta)attendsIterator.next();
                    
                    // improvement student (he/she is enrolled)
                    if (improvementFilter && attendacy.getEnrolment() != null &&
                            (!attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod()))){
                        newAttends.add(attendacy);
                    
                    // normal student (cannot be an improvement student)
                    } else if (enrolledFilter && attendacy.getEnrolment() != null &&
                            (attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod()))){
                        newAttends.add(attendacy);
                    // not enrolled student
                    } else if (notEnrolledFilter && attendacy.getEnrolment() == null){
                        newAttends.add(attendacy);
                    }
                }
                attends = newAttends;
            }
            
            
            // filter by Shift
            if (shiftIds != null){ 
                Iterator shiftIterator = shiftIds.iterator();
                List collectedAttends = new ArrayList();
                
                while(shiftIterator.hasNext()) {
                 
                    Integer shiftId = (Integer)shiftIterator.next();
	                final ITurno turno = (ITurno) sp.getITurnoPersistente().readByOID(Turno.class,shiftId);
	                                
	                Iterator attendsIterator = attends.iterator();
	                
	                while(attendsIterator.hasNext()){
	                    IFrequenta attendance = (IFrequenta)attendsIterator.next();
	                
	                    //	if an attendance is related to a Shift
	                    IStudent student = attendance.getAluno();
	                
	                    try {
	                        ITurnoAluno ta = (ITurnoAluno)sp.getITurnoAlunoPersistente().readByTurnoAndAluno(turno, student);
	                        
	                        if (ta != null)
	                            collectedAttends.add(attendance);
	                    }
	                
	                    catch(ExcepcaoPersistencia ep) {
	                        FenixServiceException newEx = new FenixServiceException("Persistence layer error");
	                        newEx.fillInStackTrace();
	                        throw newEx;
	                    }
	                }
                }
                attends = collectedAttends;
            }

            // building the info
            InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = new InfoForReadStudentsWithAttendsByExecutionCourse();
            List shifts = sp.getITurnoPersistente().readByExecutionCourseID(executionCourse.getIdInternal());
            
            List infoCompositions = new ArrayList();
            Iterator it = attends.iterator();
            while(it.hasNext()){
                IFrequenta iFrequenta = (IFrequenta)it.next();
                
                InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups infoComposition = new InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups();
                
                InfoFrequenta infoFrequenta = InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment.newInfoFromDomain(iFrequenta);
                infoComposition.setInfoAttends(infoFrequenta);

                // determining the EnrolmentEvaluationType
                if (iFrequenta.getEnrolment() != null){
                    EnrolmentEvaluationType enrollmentEvaluationType = null;
                    if (iFrequenta.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod())){
                        enrollmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
                    }
                    else{
                        enrollmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;                        
                    }
                    infoFrequenta.getInfoEnrolment().setEnrolmentEvaluationType(enrollmentEvaluationType);                    
                }
                
                IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlanFromAttends(iFrequenta);
                IDegreeCurricularPlan degreeCP = studentCurricularPlan.getDegreeCurricularPlan();
                InfoDegreeCurricularPlan infoDCP = InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(degreeCP);

                infoComposition.setAttendingStudentInfoDCP(infoDCP);
                
                Map infoShifts = getShiftsByAttends(shifts,iFrequenta,sp);
                infoComposition.setInfoShifts(infoShifts);
                
                Map infoStudentGroups = getInfoStudentGroupsByAttends(studentGroupsMap,iFrequenta);
                infoComposition.setInfoStudentGroups(infoStudentGroups);
                
                IStudent student = iFrequenta.getAluno();
                IEnrollment enrollment = iFrequenta.getEnrolment();
                Integer numberOfEnrollments = null;
                
                if (enrollment != null){
                    List studentEnrollments = persistentEnrollment.readEnrollmentsByStudentAndCurricularCourseNameAndDegree(student,enrollment.getCurricularCourse(),studentCurricularPlan.getDegreeCurricularPlan().getDegree());
                    numberOfEnrollments = new Integer(studentEnrollments.size());                    
                }
                else{
                    numberOfEnrollments = new Integer(0);                    
                }
                
                Integer num = (Integer)enrollmentDistribution.get(numberOfEnrollments);
                if (num == null)
                    num = new Integer(0);
                enrollmentDistribution.put(numberOfEnrollments,new Integer(num.intValue()+1));
                
                if (!enrollmentDistributionKeys.contains(numberOfEnrollments)){
                    enrollmentDistributionKeys.add(numberOfEnrollments);
                }
                
                infoComposition.setNumberOfEnrollments(numberOfEnrollments);
                
                infoCompositions.add(infoComposition);
            }
            infoDTO.setInfoAttends(infoCompositions);
            infoDTO.setInfoExecutionCourse(infoExecutionCourse);
            
            List tipoAulas = getClassTypesFromExecutionCourse(executionCourse);
            infoDTO.setClassTypes(tipoAulas);
            
            infoDTO.setInfoShifts(getInfoShiftsFromList(allShifts));
            infoDTO.setInfoDegreeCurricularPlans(getInfoDegreeCurricularPlansFromList(allDegreeCurricularPlans));
            
            List infoGroupProperties = getInfoGroupPropertiesFromList(groupProperties);
            infoDTO.setInfoGroupProperties(infoGroupProperties);
            
            Collections.sort(enrollmentDistributionKeys);
            infoAttendsSummary.setEnrollmentDistribution(enrollmentDistribution);
            infoAttendsSummary.setNumberOfEnrollments(enrollmentDistributionKeys);
            infoDTO.setInfoAttendsSummary(infoAttendsSummary);
            
            TeacherAdministrationSiteView siteView = createSiteView(infoDTO, site);
            return siteView;

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }

    private List getCurricularCourseStudents(ICurricularCourse curricularCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        List infoStudentList;
        IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();

        List enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse);

        infoStudentList = (List) CollectionUtils.collect(enrolments, new Transformer() {
            public Object transform(Object input) {
                IEnrollment enrolment = (IEnrollment) input;
                IStudent student = enrolment.getStudentCurricularPlan().getStudent();
                //CLONER
                //InfoStudent infoStudent =
                // Cloner.copyIStudent2InfoStudent(student);
                InfoStudent infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private List getAllAttendingStudents(ISuportePersistente sp, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        List infoStudentList;
        //	all students that attend this execution course
        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
        List attendList = frequentaPersistente.readByExecutionCourse(executionCourse);

        infoStudentList = (List) CollectionUtils.collect(attendList, new Transformer() {

            public Object transform(Object input) {
                IFrequenta attend = (IFrequenta) input;
                IStudent student = attend.getAluno();
                //CLONER
                //InfoStudent infoStudent =
                // Cloner.copyIStudent2InfoStudent(student);
                InfoStudent infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private TeacherAdministrationSiteView createSiteView(InfoForReadStudentsWithAttendsByExecutionCourse infoSiteStudents, ISite site)
    throws FenixServiceException {
        
        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteStudents);
        return siteView;
    }
    
    IStudentCurricularPlan getStudentCurricularPlanFromAttends(IFrequenta attendance)
    {
        if (attendance.getEnrolment() == null)
            return GetLastCurricularPlan(attendance.getAluno().getStudentCurricularPlans());
        else
            return attendance.getEnrolment().getStudentCurricularPlan();
    }
    
    List getDegreeCurricularPlansFromAttends(List attends)
    {
        List degreeCurricularPlans = new ArrayList();
        
        Iterator attendsIterator = attends.iterator();
        
        while (attendsIterator.hasNext())
        {
            IFrequenta attendance = (IFrequenta)attendsIterator.next();
            IDegreeCurricularPlan dcp = getStudentCurricularPlanFromAttends(attendance).getDegreeCurricularPlan();
            
            if (!degreeCurricularPlans.contains(dcp))
                degreeCurricularPlans.add(dcp);
        }
        return degreeCurricularPlans;
    }
    
    private Map getShiftsByAttends(List shifts, IFrequenta attend, ISuportePersistente sp)
    throws FenixServiceException{
        Map result = new HashMap();
        
        Iterator it = shifts.iterator();
        while(it.hasNext()){
            ITurno sh = (ITurno)it.next();
            try {
                ITurnoAluno ta = (ITurnoAluno)sp.getITurnoAlunoPersistente().readByTurnoAndAluno(sh, attend.getAluno());
                
                if (ta != null)
                    result.put(sh.getTipo().getSiglaTipoAula(),InfoShift.newInfoFromDomain(sh));//result.get(sh.getTipo())
            }
        
            catch(ExcepcaoPersistencia ep) {
                FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                newEx.fillInStackTrace();
                throw newEx;
            }
        }
        return result;
    }
    
    private List getClassTypesFromExecutionCourse(IExecutionCourse executionCourse){
        List classTypes = new ArrayList();
        if (executionCourse.getTheoreticalHours().doubleValue() > 0)
            classTypes.add(new TipoAula(TipoAula.TEORICA));
        
        if (executionCourse.getLabHours().doubleValue() > 0)
            classTypes.add(new TipoAula(TipoAula.LABORATORIAL));

        if (executionCourse.getPraticalHours().doubleValue() > 0)
            classTypes.add(new TipoAula(TipoAula.PRATICA));
        
        if (executionCourse.getTheoPratHours().doubleValue() > 0)
            classTypes.add(new TipoAula(TipoAula.TEORICO_PRATICA));
        
        return classTypes;
    }
    
    private List getInfoShiftsFromList(List shifts){
        List result = new ArrayList();
        
        for(Iterator shIterator = shifts.iterator();shIterator.hasNext();){
            ITurno sh = (ITurno)shIterator.next();
            result.add(InfoShiftWithInfoExecutionCourseAndInfoLessons.newInfoFromDomain(sh));
        }
        
        return result;
    }
    
    private List getInfoDegreeCurricularPlansFromList(List degreeCPs){
        List result = new ArrayList();
        
        for(Iterator dcpIterator = degreeCPs.iterator();dcpIterator.hasNext();){
            IDegreeCurricularPlan dcp = (IDegreeCurricularPlan)dcpIterator.next();
            
            result.add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(dcp));
        }
        
        return result;        
    }
    
    private List getInfoGroupPropertiesFromList(List groupProperties){
        List result = new ArrayList();
        
        for (Iterator gpIterator = groupProperties.iterator(); gpIterator.hasNext();){
            IGroupProperties gp = (IGroupProperties)gpIterator.next();
            InfoGroupProperties infoGP = InfoGroupProperties.newInfoFromDomain(gp);
            result.add(infoGP);
        }
        
        return result;
    }
    
    private Map getStudentGroupsMapFromGroupPropertiesList(List groupPropertiesList, ISuportePersistente sp)
    	throws ExcepcaoPersistencia{
    
        Map result = new HashMap();
        List allStudentsGroups = new ArrayList();
        IPersistentStudentGroupAttend pga = sp.getIPersistentStudentGroupAttend();
        
        Iterator gpIt = groupPropertiesList.iterator();
        while(gpIt.hasNext()){
            allStudentsGroups.addAll(((IGroupProperties)gpIt.next()).getAttendsSet().getStudentGroups());
        }
        
        for (Iterator sgIterator = allStudentsGroups.iterator(); sgIterator.hasNext();){
            IStudentGroup sg = (IStudentGroup)sgIterator.next();
            List groupAttends = pga.readAllByStudentGroup(sg);
            List attendsList = (List) CollectionUtils.collect(groupAttends, new Transformer()
                    {

                        public Object transform( Object input )
                        {
                            IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) input;
                            IFrequenta attendacy = studentGroupAttend.getAttend();
                            return attendacy;
                        }
                    });
        
            result.put(sg,attendsList);
        }
        
        return result;
    }
    
    private Map getInfoStudentGroupsByAttends(Map studentsGroupsAttendsListMap, IFrequenta attends){
        Map result = new HashMap();
        
        Collection studentsGroups = studentsGroupsAttendsListMap.keySet();
        
        for (Iterator groupsIterator = studentsGroups.iterator(); groupsIterator.hasNext();){
            IStudentGroup sg = (IStudentGroup)groupsIterator.next();
            
            List attendsList = (List) studentsGroupsAttendsListMap.get(sg);
            
            if (attendsList.contains(attends)){
                String groupPropertiesName = sg.getAttendsSet().getGroupProperties().getName();
                InfoStudentGroup infoSG = InfoStudentGroupWithInfoShift.newInfoFromDomain(sg);
                result.put(groupPropertiesName,infoSG);
            }
        }
        
        return result;
    }
}










