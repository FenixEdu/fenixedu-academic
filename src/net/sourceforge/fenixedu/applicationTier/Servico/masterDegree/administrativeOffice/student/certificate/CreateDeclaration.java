package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CreateDeclaration extends Service {

    public List run(InfoStudent infoStudent, Specialization specialization) throws Exception {

        List infoStudentCurricularPlanList = new ArrayList();

        Registration student = Registration.readStudentByNumberAndDegreeType(infoStudent.getNumber(), infoStudent.getDegreeType());
        if(student == null) {
        	return null;
        }
        
        List<StudentCurricularPlan> studentCurricularPlanList = student.getStudentCurricularPlansBySpecialization(specialization);


        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
            return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

            if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
                infoStudentCurricularPlanList
                        .add(InfoStudentCurricularPlan
                                .newInfoFromDomain(studentCurricularPlan));
            }

        }

        return infoStudentCurricularPlanList;

    }

    public List run(InfoStudent infoStudent, Specialization specialization,
            StudentCurricularPlanState state) throws Exception {


        List infoStudentCurricularPlanList = new ArrayList();

        Registration student = Registration.readStudentByNumberAndDegreeType(infoStudent.getNumber(), infoStudent.getDegreeType());
        if(student == null) {
        	return null;
        }
        List studentCurricularPlanList = student.getStudentCurricularPlansBySpecializationAndState(specialization, state);   	

        if (studentCurricularPlanList == null || studentCurricularPlanList.isEmpty()) {
        	return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
        	StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

        	if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
        		infoStudentCurricularPlanList
        		.add(InfoStudentCurricularPlan
        				.newInfoFromDomain(studentCurricularPlan));
        	}

        }

        return infoStudentCurricularPlanList;
    }

    // FIXME change paraemter states to List type, when berserk's reflection bug
    // is fixed
    public List run(InfoStudent infoStudent, Specialization specialization, ArrayList states)
            throws Exception {

        List studentCurricularPlanList = new ArrayList();
        List infoStudentCurricularPlanList = new ArrayList();

        Registration student = Registration.readStudentByNumberAndDegreeType(infoStudent.getNumber(), infoStudent.getDegreeType());
        if(student == null) {
        	return null;
        }
        for (Iterator iter = states.iterator(); iter.hasNext();) {
        	StudentCurricularPlanState state = (StudentCurricularPlanState) iter.next();
        	List<StudentCurricularPlan> studentCurricularPlanListTmp = student.getStudentCurricularPlansBySpecializationAndState(specialization, state);
        	for (Iterator iterator = studentCurricularPlanListTmp.iterator(); iterator.hasNext();) {
        		StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();

        		studentCurricularPlanList.add(studentCurricularPlan);
        	}

        }

        if (studentCurricularPlanList.isEmpty()) {
        	return null;
        }

        for (Iterator iter = studentCurricularPlanList.iterator(); iter.hasNext();) {
        	StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iter.next();

        	if (studentCurricularPlan != null || studentCurricularPlan.getIdInternal() != null) {
        		infoStudentCurricularPlanList
        		.add(InfoStudentCurricularPlan
        				.newInfoFromDomain(studentCurricularPlan));
        	}

        }
        	
        return infoStudentCurricularPlanList;
    }
}