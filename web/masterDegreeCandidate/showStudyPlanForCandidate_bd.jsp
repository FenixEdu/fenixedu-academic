<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="personalInfo" name="masterDegreeCandidate" scope="request" property="infoPerson"/>
<bean:define id="masterDegreeCandidate" name="masterDegreeCandidate" scope="request" type="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate"/>
<bean:define id="studyPlan" name="candidateStudyPlan" scope="request"/>

<jsp:include page="../context.jsp"/>
<h2><bean:message key="label.masterDegree.studyPlan.studyPlanForCandidate"/></h2>
    <br />
   <table width="100%" cellspacing="0">
		<tr>
            <td class="infoop"><strong><bean:message key="label.masterDegree.studyPlan.studyPlanForCandidate"/></strong></td>
         </tr>
   </table>
   <table width="100%" cellspacing="0">
		<tr>
			<td>
			<bean:message key="label.masterDegree.studyPlan.givenCredits"/><bean:write name="masterDegreeCandidate" property="givenCredits"/>
			</td>
         </tr>
		<br/>
		<tr>
			<td>
			<bean:message key="label.masterDegree.studyPlan.givenCreditsRemarks"/><br/>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.givenCreditsRemarks" disabled="true" name="masterDegreeCandidate" property="givenCreditsRemarks"/>
			</td>
		</tr>
		<tr>
			<td>
			<br/>
			<h3>
<b>			<bean:message key="label.masterDegree.studyPlan.listOfCurricularCourses"/></b></h3><br/>
			<table width="100%">
				<tr>
					<td class="infoop" width="70%"><b><bean:message key="label.masterDegree.studyPlan.curricularCourseName"/></b></td>
					<td class="infoop" width="30%"><b><bean:message key="label.masterDegree.studyPlan.curricularCourseCredits"/></b></td>
				</tr>
			<logic:iterate id="StudyPlanItem" name="studyPlan">
				<bean:define id="infoCourse" name="StudyPlanItem" property="infoCurricularCourse"/>
				<tr>
					<td><bean:write name="infoCourse" property="name"/></td>
					<td><bean:write name="infoCourse" property="credits"/></td>
				</tr>
			</logic:iterate>
			</table>
			</td>
		</tr>

   </table>

   <br />

    <table width="100%" cellspacing="0">
		<tr>
            <td class="infoop"><strong><bean:message key="label.masterDegree.studyPlan.isClassAssistantTitle"/></strong></td>
         </tr>
    </table>
    <table>
        <tr>
            <td>
                <bean:message key="label.masterDegree.studyPlan.isClassAssistant"/><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.courseAssistant" disabled="true" name="masterDegreeCandidate" property="courseAssistant"/>
            </td>
        </tr>
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
    </table>

    <br />

    <table width="100%" cellspacing="0">
		<tr>
            <td class="infoop"><strong><bean:message key="label.masterDegree.studyPlan.advisorOrCoordinatorTitle"/></strong></td>
         </tr>
    </table>

    <logic:notEmpty name="masterDegreeCandidate" property="infoGuider" scope="request">

    <table>
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
                <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.hasGuider" disabled="true" name="masterDegreeCandidate" property="hasGuider" value="true"/><bean:message key="label.masterDegree.studyPlan.advisor"/>
            </td>
        </tr>
        <tr>
            <td>
                <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.hasGuider" disabled="true" name="masterDegreeCandidate" property="hasGuider" value="false"/><bean:message key="label.masterDegree.studyPlan.coordinator"/>
            </td>
        </tr>
   </table>

   </logic:notEmpty>

   <logic:empty name="masterDegreeCandidate" property="infoGuider" scope="request">
        <bean:message key="label.masterDegree.studyPlan.NoAdvisorOrCoordinatorTitle"/>
   </logic:empty>

