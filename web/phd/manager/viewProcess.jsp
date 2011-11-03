<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="MANAGER">

<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddStudyPlan"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicPresentationSeminarComission"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicThesisPresentation"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ExemptPublicPresentationSeminarComission"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ConfigurePhdIndividualProgramProcess" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant"  %>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DissociateRegistration" %>
<%@page import="net.sourceforge.fenixedu.domain.accessControl.PermissionType" %>

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>

<logic:present name="backMethod">
	<bean:define id="backMethod" name="backMethod"/>
	<p>
		<html:link action="<%="/phdIndividualProgramProcess.do?method=" + backMethod %>">
			« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
		</html:link>
	</p>
</logic:present>

<logic:notPresent name="backMethod">
<p>
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>
</logic:notPresent>


<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<div style="float: right;">
	<bean:define id="personID" name="process" property="person.idInternal"/>
	<html:img align="middle" src="<%= request.getContextPath() + "/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>
 
<p><strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong></p>

<logic:equal name="process" property="candidacyProcess.publicCandidacy" value="true">
<logic:equal name="process" property="candidacyProcess.validatedByCandidate" value="false">
	<div class="warning1">
		<p><bean:message key="message.phd.candidacy.not.submited.by.candidate" bundle="PHD_RESOURCES" /></p>
	</div>
</logic:equal>
</logic:equal>

<table>
  <tr>
    <td>
		<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
			</fr:layout>
		</fr:view>
	</td>
    <td style="vertical-align: top; padding-top: 1em;">
    	<bean:define id="process" name="process" />
		
    	<ul class="operations">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=manageStudyPlan" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.studyPlanAndQualificationExams"/>
					</html:link>
				</li>
			<logic:equal name="process" property="activeState.active" value="true">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manageGuidingInformation"/>
					</html:link>
				</li>
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=manageAlerts" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manageAlerts"/>
					</html:link>
				</li>
				<li>
					<jsp:include page="/phd/alertMessagesNotifier.jsp?global=false" />
				</li>
			</logic:equal>
			
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=managePhdIndividualProgramProcessState" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states"/>
					</html:link>
				</li>
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=viewPhdParticipants" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message key="link.phd.participants.view" bundle="PHD_RESOURCES" />
					</html:link>
				</li>
				
				<br />
				
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=preparePhdEmailsManagement" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.emails" />
					</html:link>
				</li> 
				
				<br/>
			
			<logic:equal name="process" property="migratedProcess" value="true">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=viewAssociatedMigrationProcess" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.viewMigrationProcess" />
					</html:link>
				</li>
			</logic:equal>
			
			<br/>
			
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=preparePhdConfigurationManagement" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.configuration" />
					</html:link>
				</li>
			
		</ul>
    </td>
  </tr>
</table>

<logic:notEmpty name="process" property="student">
	<br/>
	<bean:define id="studentId" name="process" property="student.idInternal" />
	  
	<strong><bean:message  key="label.phd.student.information" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view.student.information" name="process">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10 thleft" />
		</fr:layout>
	</fr:view>
	<p>
	<html:link page="<%= "/student.do?method=visualizeStudent&studentID=" + studentId %>">
		<bean:message key="label.phd.student.page" bundle="PHD_RESOURCES" />
	</html:link>
	</p>
</logic:notEmpty>


<%--Thesis --%>
<jsp:include page="viewThesisProcess.jsp" />

<%--CAT --%>
<jsp:include page="viewSeminarProcess.jsp" />

<%-- School part --%>
<jsp:include page="viewSchoolPart.jsp" />

<%--Candidacy --%>
<jsp:include page="viewCandidacyProcess.jsp" />

<%-- Academic Service Requests --%>
<jsp:include page="viewAcademicServiceRequests.jsp" />

</logic:present>