<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="Util.Data" %>
<logic:present name="infoSiteEnrolmentEvaluation">
	<br />
	<table width="100%">
		<tr>
			<td colspan="2" align="center">
				<h2><bean:message key="label.masterDegree.administrativeOffice.gradesList"/></h2><br />
			</td>	
		</tr> 
		<tr>
			<td width="20%">&nbsp;</td>	
			<td width="80%">
				<table cellpadding="3">
					<tr>
						<td width="20%" align="right">
							<b><bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>:</b>
						</td>	
						<td width="60%" align="left">
							<bean:write name="degreeName" />
						</td>	
					</tr> 
					<tr>
						<td width="20%" align="right">
							<b><bean:message key="label.masterDegree.administrativeOffice.course"/>:</b>
						</td>	
						<td width="60%" align="left">
							<bean:write name="curricularCourse" />
						</td>	
					</tr> 
					<tr>
						<td width="20%" align="right">
							<b><bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:</b>
						</td>	
						<td width="60%" align="left">
							<bean:write name="executionYear" />
						</td>	
					</tr>
				</table>		
			</td>
		</tr>
	</table>		
	<br /><br /><br /><br />

	<bean:define id="teacher" name="infoSiteEnrolmentEvaluation" property="infoTeacher"/>
	<bean:define id="availableEvaluationDate" name="infoSiteEnrolmentEvaluation" property="lastEvaluationDate"/>
    <table border="1" width="100%" cellpadding="2">        
		<tr>
			<th>
				<bean:message key="label.number" />
			</th>
			<th>
				<bean:message key="label.name" />
		   	</th>
			<th>
				<bean:message key="labe.exam.date" />
			</th>
			<th>
				<bean:message key="label.mark" />
			</th>
			<th>
				<bean:message key="label.masterDegree.administrativeOffice.rubrica" />
			</th>
		</tr>    				
    	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation">    		
    		<tr>
				<td>
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number"/>&nbsp;
				</td>
				<td>
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
				</td>											
				<td>
					<%= Data.format2DayMonthYear(enrolmentEvaluation.getExamDate()) %>
				</td>											
				<td>
					<bean:write name="enrolmentEvaluation" property="grade" />
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