<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.masterDegree.administrativeOffice.marksConfirmation" /></h2>
<logic:present name="infoSiteEnrolmentEvaluation">
	<table width="100%">
		<logic:iterate id="enrollmentEvaluationElem" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation" length="1">	
			<tr>
				<td class="infoselected">
					<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
					<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
					<br />
					<b><bean:message key="label.curricularPlan" />:</b>
					<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.name" />
					<br />
					<b><bean:message key="label.curricularCourse"/>:</b>
					<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.name" />
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<bean:define id="teacher" name="infoSiteEnrolmentEvaluation" property="infoTeacher"/>
	<bean:define id="availableEvaluationDate" name="infoSiteEnrolmentEvaluation" property="lastEvaluationDate"/>
    <table>        
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.number" /> 
		   	</th>
			<th class="listClasses-header">
				<bean:message key="label.name" />
		   	</th>
			<th class="listClasses-header">
				<bean:message key="label.mark" />
			</th>
		</tr>    				
		<bean:size id="size" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" />	
    	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation">
    		
    		<tr>
				<td class="listClasses">
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number"/>&nbsp;
				</td>
				<td class="listClasses">
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
				</td>											
				<td class="listClasses">
					<bean:write name="enrolmentEvaluation" property="grade" />
				</td>
			</tr>
    	</logic:iterate>
    </table>
    <br />
    <table>
		<tr>
			<td colspan="2">
				<b><bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" /></b>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.number" />:
				<bean:write name="teacher" property="teacherNumber"/>
				
			</td>
			<td> 
				&nbsp;-&nbsp;<bean:message key="label.masterDegree.administrativeOffice.name" />:
				<bean:write name="teacher" property="infoPerson.nome"/>
			</td>
		</tr>
    </table>
	<br />
	<html:link target="_blank" page="<%="/marksConfirmation.do?method=prepareMarksConfirmation&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + pageContext.findAttribute("courseId") + "&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;useCase=print" %>">
		<bean:message key="link.masterDegree.administrativeOffice.print" />
	</html:link>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<html:link page="<%="/marksConfirmation.do?method=prepareMarksConfirmation&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + pageContext.findAttribute("courseId") + "&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;useCase=confirm" %>">
		<bean:message key="link.masterDegree.administrativeOffice.marksConfirmation" />
	</html:link>
</logic:present>   