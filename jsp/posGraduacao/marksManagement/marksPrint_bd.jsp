<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<logic:present name="infoSiteEnrolmentEvaluation">
	<br />
	<table width="100%">
		<tr>
			<td colspan="2" align="center">
				<h2><bean:message key="label.masterDegree.administrativeOffice.gradesList"/></h2><br />
			</td>	
		</tr> 
    	<logic:iterate id="enrollmentEvaluationElem" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation" length="1">    		
			<tr>
				<td width="20%">&nbsp;</td>	
				<td width="80%">
					<table cellpadding="3">
						<tr>
							<td width="20%" align="right">
								<b><bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>:</b>
							</td>	
							<td width="60%" align="left">
								<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
							</td>	
						</tr> 
						<tr>
							<td width="20%" align="right">
								<b><bean:message key="label.masterDegree.administrativeOffice.course"/>:</b>
							</td>	
							<td width="60%" align="left">
								<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.name" />
							</td>	
						</tr> 
						<tr>
							<td width="20%" align="right">
								<b><bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:</b>
							</td>	
							<td width="60%" align="left">
								<bean:write name="infoSiteEnrolmentEvaluation" property="infoExecutionPeriod.infoExecutionYear.year" />
							</td>	
						</tr>
					</table>		
				</td>
			</tr>
		</logic:iterate>
	</table>		
	<br /><br /><br /><br />
	<bean:define id="teacher" name="infoSiteEnrolmentEvaluation" property="infoTeacher"/>
	<bean:define id="availableEvaluationDate" name="infoSiteEnrolmentEvaluation" property="lastEvaluationDate"/>
    <table border="1" width="100%" cellpadding="2">        
		<tr>
			<th><bean:message key="label.number" /></th>
			<th><bean:message key="label.name" /></th>
			<th><bean:message key="labe.exam.date" /></th>
			<th><bean:message key="label.mark" /></th>
			<th><bean:message key="label.masterDegree.administrativeOffice.rubrica" /></th>
		</tr>    				
    	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation">    		
    		<tr>
				<td>
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number"/>&nbsp;
				</td>
				<td>
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
				</td>											
				<td>
					<dt:format pattern="dd-MM-yyyy">
						<bean:write name="enrolmentEvaluation" property="examDate.time"/>
					</dt:format>
				</td>											
				<td>
					<logic:notEmpty name="enrolmentEvaluation" property="grade">
						<bean:write name="enrolmentEvaluation" property="grade" />
					</logic:notEmpty>
					<logic:empty name="enrolmentEvaluation" property="grade">
						&nbsp;
					</logic:empty>
				</td>
				<td>
					&nbsp;
				</td>											
			</tr>
    	</logic:iterate>
	</table> 
	<table cellpadding="6">
		<tr>
			<td>
				<b><bean:message key="label.masterDegree.administrativeOffice.theResponsible"/>:</b>		
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.mecanographicNumber"/>:</b>		
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.masterDegree.administrativeOffice.signature"/>:</b>		
			</td>
		</tr>		
	</table>
</logic:present>   