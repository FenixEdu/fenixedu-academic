<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CandidateOperationDispatchAction" %>
<%@ page import="net.sourceforge.fenixedu.domain.ApplicationDocumentType"%>
<bean:define id="personalInfo" name="masterDegreeCandidate" scope="request" property="infoPerson"/>
<bean:define id="masterDegreeCandidate" name="masterDegreeCandidate" scope="request" type="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate"/>
<bean:define id="studyPlan" name="candidateStudyPlan" scope="request"/>

<jsp:include page="../context.jsp"/>
<h2><bean:message key="title.candidate.info" /></h2>
	<table>
        <logic:present name="personalInfo">
          <!-- Name -->
          <tr>
	            <td><bean:message key="label.person.name" /></td>
	            <td><span class="greytxt"><bean:write name="personalInfo" property="nome"/></span></td>
          </tr>
          <!-- Candidate Number -->
          <tr>
	            <td><bean:message key="label.candidate.candidateNumber" />:</td>
	            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="candidateNumber"/></span></td>
          </tr>
          <!-- Specialization -->
          <tr>
	            <td><bean:message key="label.candidate.specialization" />:</td>
	            <td><span class="greytxt"><bean:message name="masterDegreeCandidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/></span></td>
          </tr>
          <!-- Specialization Area -->
          <tr>
	            <td><bean:message key="label.candidate.specializationArea" />:</td>
	            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="specializationArea"/></span></td>
          </tr>
    </table>
    <br />
          <!-- Contactos -->
   	<table width="100%" cellspacing="0">  
         <tr>
         	<td class="infoop" width="50px"><span class="emphasis-box">1</span>
            <td class="infoop"><strong><bean:message key="label.person.title.contactInfo" /></strong></td>
          </tr>
     </table>
     <br />
     <table>
          <!-- Telefone -->
          <tr>
            <td><bean:message key="label.person.telephone" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="telefone"/></span></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td><bean:message key="label.person.mobilePhone" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="telemovel"/></span></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="email"/></span></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="enderecoWeb"/></span></td>
          </tr>
	</table>
	<br />
   	      <!-- Informacao de Licenciatura -->
	<table width="100%" cellspacing="0">    
          <tr>
          	<td class="infoop" width="50px"><span class="emphasis-box">2</span>
            <td class="infoop"><strong><bean:message key="label.candidate.majorDegreeInfo" /></strong></td>
          </tr>
    </table>
    <br />
    <table>
          <!-- Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegree" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegree"/></span></td>
          </tr>
          <!-- Ano de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeYear" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegreeYear"/></span></td>
          </tr>
          <!-- Escola de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeSchool" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegreeSchool"/></span></td>
          </tr>
          <!-- Media -->
          <tr>
            <td><bean:message key="label.candidate.average" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="average"/> <bean:message key="label.candidate.values"/></span></td>
          </tr>
    </table>
    <br />
	<table width="100%" cellspacing="0">
		<tr>
			<td class="infoop" width="50px"><span class="emphasis-box">3</span>
            <td class="infoop"><strong><bean:message key="label.masterDegree.administrativeOffice.situationHistory" /></strong></td>
         </tr>
   </table>
   <br />
   <table>
         <logic:iterate id="situation" name="masterDegreeCandidate" property="situationList">
         	<% if (((InfoCandidateSituation) situation).getValidation().equals(new State(State.ACTIVE))) { 
%>
         		<td><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></td>
         	<% } 
%>
           <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situation" />:</td>
            <td><span class="greytxt"><bean:write name="situation" property="situation"/></span></td>
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situationDate" />:</td>
            <logic:present name="situation" property="date" >
	            <bean:define id="date" name="situation" property="date" />
				<td><%= Data.format2DayMonthYear((Date) date) 
%></td>             
			</logic:present>
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.remarks" />:</td>
            <td><span class="greytxt"><bean:write name="situation" property="remarks"/></span></td>
		   </tr>
         </logic:iterate>
        </logic:present>
     </table>
     <br/>

   <table width="100%" cellspacing="0">
		<tr>
			<td class="infoop" width="50px"><span class="emphasis-box">4</span>
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
	<logic:present name="studyPlan">
			<bean:message key="label.masterDegree.studyPlan.listOfCurricularCourses"/><br/>
			<table width="100%">
				<tr>
					<td width="70%"><b><bean:message key="label.masterDegree.studyPlan.curricularCourseName"/></b></td>
					<td width="30%"><b><bean:message key="label.masterDegree.studyPlan.curricularCourseCredits"/></b></td>
				</tr>
			<logic:iterate id="StudyPlanItem" name="studyPlan">
				<bean:define id="infoCourse" name="StudyPlanItem" property="infoCurricularCourse"/>
				<tr>
					<td><bean:write name="infoCourse" property="name"/></td>
					<td><bean:write name="infoCourse" property="credits"/></td>
				</tr>
			</logic:iterate>
			</table>
	</logic:present>
			<logic:notPresent name="studyPlan">
				<bean:message key="label.masterDegree.studyPlan.noStudyPlanForCandidate"/>
			</logic:notPresent>
			</td>
		</tr>

   </table>

   <br/>

   <table width="100%" cellspacing="0">
		<tr>
			<td class="infoop" width="50px"><span class="emphasis-box">5</span>
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
			<td class="infoop" width="50px"><span class="emphasis-box">6</span>
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


<bean:define id="candidateID" name="masterDegreeCandidate" property="idInternal" />
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />
	
<table width="100%" cellspacing="0">
	<tr>
		<td class="infoop" width="50px"><span class="emphasis-box">7</span>
        <td class="infoop"><strong><bean:message key="label.masterDegree.applicationDocuments"/></strong></td>
    </tr>
</table>
   	
<table width="100%">  
	<tr>
		<td>
			<html:link action='<%= "/candidateOperation.do?method=showApplicationDocuments&documentType=CURRICULUM_VITAE&candidateID=" + candidateID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>'><bean:message key="label.masterDegree.showCurriculumVitae"/></html:link>
		</td>
	</tr>
	<tr>
		<td>
			<html:link action='<%= "/candidateOperation.do?method=showApplicationDocuments&documentType=HABILITATION_CERTIFICATE&candidateID=" + candidateID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>' ><bean:message key="label.masterDegree.showHabilitationLetter"/></html:link>
		</td>
	</tr>
	<tr>
		<td>
			<html:link action='<%= "/candidateOperation.do?method=showApplicationDocuments&documentType=SECOND_HABILITATION_CERTIFICATE&candidateID=" + candidateID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>' ><bean:message key="label.masterDegree.showSecondHabilitationLetter"/></html:link>
		</td>
	</tr>
	<tr>
		<td>
			<html:link action='<%= "/candidateOperation.do?method=showApplicationDocuments&documentType=INTEREST_LETTER&candidateID=" + candidateID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>' ><bean:message key="label.masterDegree.showManifestationLetter"/></html:link>
		</td>
	</tr>
</table>
     