<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.UploadCandidacyReview"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.RequestCandidacyReview"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.UploadCandidacyFeedback"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicPresentationSeminarComission"%>

<html:xhtml/>

<logic:equal name="isTeacher" value="true">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.teacher.breadcrumb" bundle="PHD_RESOURCES"/></em>
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
	<html:link action="<%="/phdIndividualProgramProcess.do?method=" + backMethod %>">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:present>

<logic:notPresent name="backMethod">
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:notPresent>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
		<fr:view schema="PhdIndividualProgramProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10" />
			</fr:layout>
		</fr:view>
	</td>
    <td style="vertical-align: top; padding-top: 1em;">
    	<bean:define id="process" name="process" />
    	<ul class="operations">
			<li>
				<jsp:include page="/phd/alertMessagesNotifier.jsp?global=false" />
			</li>
			<phd:activityAvailable process="<%= process %>" activity="<%= RequestPublicPresentationSeminarComission.class %>">
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=prepareRequestPublicPresentationSeminarComission" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.request.public.presentation.seminar.comission"/>
				</html:link>
			</li>
			</phd:activityAvailable>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=manageGuidanceDocuments" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message key="link.phd.guidance.documents.management" bundle="PHD_RESOURCES" />
				</html:link>
			</li>			
		</ul>
    </td>
  </tr>
</table>

<%--Thesis --%>
<jsp:include page="/phd/thesis/teacher/viewThesisProcess.jsp" />

<%-- CAT --%>
<jsp:include page="/phd/seminar/teacher/viewSeminarProcess.jsp" />

<%-- Candidacy --%>
<br/>
<strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
		<fr:view schema="PhdProgramCandidacyProcess.view.simple" name="process" property="candidacyProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10" />
			</fr:layout>
		</fr:view>
	</td>
  </tr>
  <tr>
    <td>
    	<bean:define id="candidacyProcess" name="process" property="candidacyProcess" />
    	<ul class="operations">
    		<li style="display: inline;">
				<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyDocuments" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manageCandidacyDocuments"/>
				</html:link>
			</li>
			<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= UploadCandidacyReview.class %>">
				<li style="display: inline;">
					<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyReview" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.manageCandidacyReview"/>
					</html:link>
				</li>
			</phd:activityAvailable>
			<logic:notEmpty name="candidacyProcess" property="feedbackRequest">
				<bean:define id="feedbackRequest" name="candidacyProcess" property="feedbackRequest" />
				<phd:activityAvailable process="<%= feedbackRequest %>" activity="<%= UploadCandidacyFeedback.class %>">
					<li style="display: inline;">
						<html:link action="/phdCandidacyFeedbackRequest.do?method=prepareUploadCandidacyFeedback" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.feedback.teacher"/>
						</html:link>
					</li>
				</phd:activityAvailable>
			</logic:notEmpty>
		</ul>
    </td>
  </tr>
</table>

<%--  ### End Of Context Information  ### --%>

<%-- ### Guiding ### --%>
<br/>
<strong>
	<bean:message key="label.phd.guiding" bundle="PHD_RESOURCES"/>
</strong>
<logic:empty name="guidingsList">
	<p><em><bean:message key="message.no.guiding" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>

<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdIndividualProgramProcess.do?processId=" + processId %>">
	<input type="hidden" name="method" value="" />
	<logic:notEmpty name="guidingsList">
		<fr:view name="guidingsList">
			<fr:schema type="net.sourceforge.fenixedu.domain.PersonInformationLog" bundle="PHD_RESOURCES">
			   	<fr:slot name="name" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.name"/>
			   	<fr:slot name="qualification" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.qualification"/>
				<fr:slot name="category" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.category"/>
				<fr:slot name="workLocation" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.workLocation"/>
				<fr:slot name="institution" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.institution"/>
				<fr:slot name="address" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.address"/>
				<fr:slot name="email" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.email"/>
				<fr:slot name="phone" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.phone"/>
				<fr:slot name="acceptanceLetter" layout="link" />		
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop15 center" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</fr:form>

<br/>
<strong>
	<bean:message key="label.phd.assistant.guiding" bundle="PHD_RESOURCES"/>
</strong>

<logic:empty name="assistantGuidingsList">
	<p><em><bean:message key="message.no.assistant.guiding" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>

<fr:form action="<%= "/phdIndividualProgramProcess.do?processId=" + processId %>">
	<input type="hidden" name="method" value="" />
	<logic:notEmpty name="assistantGuidingsList">
		<fr:view name="assistantGuidingsList">
			<fr:schema type="net.sourceforge.fenixedu.domain.PersonInformationLog" bundle="PHD_RESOURCES">
				<fr:slot name="name" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.name"/>
			   	<fr:slot name="qualification" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.qualification"/>
				<fr:slot name="category" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.category"/>
				<fr:slot name="workLocation" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.workLocation"/>
				<fr:slot name="institution" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.institution"/>
				<fr:slot name="address" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.address"/>
				<fr:slot name="email" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.email"/>
				<fr:slot name="phone" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.phone"/>
				<fr:slot name="acceptanceLetter" layout="link" />		
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop15 center" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</fr:form>

	<%-- ### End of Guiding ### --%>


<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<%--  ### End of Operation Area  ### --%>


<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</logic:equal>
