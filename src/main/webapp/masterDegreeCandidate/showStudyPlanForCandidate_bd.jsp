<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="personalInfo" name="masterDegreeCandidate" scope="request" property="infoPerson"/>
<bean:define id="masterDegreeCandidate" name="masterDegreeCandidate" scope="request" type="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" />

<%--
<jsp:include page="../context.jsp"/>
--%>

<h2><bean:message key="label.masterDegree.studyPlan.studyPlanForCandidate"/></h2>
<br/>

<table width="100%" cellspacing="0">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.masterDegree.studyPlan.studyPlanForCandidate"/></strong>
		</td>
	</tr>
	<tr>
		<td>
			<logic:notEmpty name="masterDegreeCandidate" property="givenCredits">
				<bean:message key="label.masterDegree.studyPlan.givenCredits"/>
				<bean:write name="masterDegreeCandidate" property="givenCredits"/>
			</logic:notEmpty>
			<logic:empty name="masterDegreeCandidate" property="givenCredits">
				<em>Não existem créditos atríbuidos.</em>
			</logic:empty>
		</td>
	</tr>
	<logic:notEmpty name="masterDegreeCandidate" property="givenCreditsRemarks">
		<tr>
			<td>
				<bean:message key="label.masterDegree.studyPlan.givenCreditsRemarks"/>
			</td>
		</tr>
		<tr>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.givenCreditsRemarks" disabled="true" name="masterDegreeCandidate" property="givenCreditsRemarks"/>
			</td>
		</tr>
	</logic:notEmpty>
	<tr>
		<td>
			<br/>
			<h3><b><bean:message key="label.masterDegree.studyPlan.listOfCurricularCourses"/></b></h3>
			<table width="100%">
				<tr>
					<td class="infoop" width="70%">
						<b><bean:message key="label.masterDegree.studyPlan.curricularCourseName"/></b>
					</td>
					<td class="infoop" width="30%">
						<b><bean:message key="label.masterDegree.studyPlan.curricularCourseCredits"/></b>
					</td>
				</tr>
				<logic:notPresent name="candidateStudyPlan">
					<tr>
						<td>
							<em>O candidato não tem definido qualquer plano de estudos.</em>
						</td>
					</tr>			
				</logic:notPresent>
				<logic:present name="candidateStudyPlan">
					<logic:iterate id="StudyPlanItem" name="candidateStudyPlan">
						<bean:define id="infoCourse" name="StudyPlanItem" property="infoCurricularCourse"/>
						<tr>
							<td>
								<bean:write name="infoCourse" property="name"/>
							</td>
							<td>
								<bean:write name="infoCourse" property="credits"/>
							</td>
						</tr>
					</logic:iterate>
				</logic:present>
			</table>
		</td>
	</tr>
</table>


<br/>


<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoop">
    		<strong><bean:message key="label.masterDegree.studyPlan.isClassAssistantTitle"/></strong>
    	</td>
	</tr>
	<tr>
    	<td>
    		<logic:equal name="masterDegreeCandidate" property="courseAssistant" value="true">
				<bean:message key="label.masterDegree.studyPlan.isClassAssistant"/>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.courseAssistant" disabled="true" name="masterDegreeCandidate" property="courseAssistant"/>
    		</logic:equal>
    		<logic:equal name="masterDegreeCandidate" property="courseAssistant" value="false">
    			<em>O candidato não pretende dar apoio ao ensino.</em>
    		</logic:equal>
			<br/><br/>
		</td>
	</tr>
	<logic:notEmpty name="masterDegreeCandidate" property="coursesToAssist">
		<tr>
			<td>
				<bean:message key="label.masterDegree.studyPlan.executionCoursesToAssist"/>
			</td>
		</tr>
		<tr>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.coursesToAssist" disabled="true" name="masterDegreeCandidate" property="coursesToAssist" cols="40" rows="2"/>
			</td>
		</tr>
	</logic:notEmpty>
	<tr>
    	<td class="infoop">
    		<strong><bean:message key="label.masterDegree.studyPlan.advisorOrCoordinatorTitle"/></strong>
    	</td>
	</tr>

	<logic:empty name="masterDegreeCandidate" property="infoGuider" scope="request">
		<tr>
			<td>
				<em><bean:message key="label.masterDegree.studyPlan.NoAdvisorOrCoordinatorTitle"/></em>
			</td>
		</tr>
	</logic:empty>
	<logic:notEmpty name="masterDegreeCandidate" property="infoGuider" scope="request">
		<tr>
			<td>
				<bean:define id="infoTeacher" name="masterDegreeCandidate" property="infoGuider"/>
				<bean:define id="infoPersonTeacher" name="infoTeacher" property="infoPerson"/>
				<bean:message key="label.masterDegree.studyPlan.advisorOrCoordinatorName"/><bean:write name="infoPersonTeacher" property="nome"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.masterDegree.studyPlan.advisorOrCoordinatorRole"/>
			</td>
		</tr>
		<tr>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.hasGuider" disabled="true" name="masterDegreeCandidate" property="hasGuider" value="true"/>
				<bean:message key="label.masterDegree.studyPlan.advisor"/>
			</td>
		</tr>
		<tr>
			<td>
		    	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.hasGuider" disabled="true" name="masterDegreeCandidate" property="hasGuider" value="false"/>
				<bean:message key="label.masterDegree.studyPlan.coordinator"/>
			</td>
		</tr>
	</logic:notEmpty>
</table>

