<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.studyPlan" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<%--  Study Plan Documents --%>
<strong><bean:message  key="label.phd.submited.study.plan.documents" bundle="PHD_RESOURCES"/></strong><br/>
<logic:empty name="process" property="studyPlanDocuments">
	<bean:message  key="label.phd.noDocuments" bundle="PHD_RESOURCES"/>
</logic:empty> 
<logic:notEmpty name="process" property="studyPlanDocuments">
	<fr:view schema="PhdProgramCandidacyProcessDocument.view.without.type" name="process" property="studyPlanDocuments">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:property name="linkFormat(view)" value="${downloadUrl}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
			<fr:property name="order(view)" value="0" />
			<fr:property name="module(view)" value="" />
			<fr:property name="hasContext(view)" value="true" />
			
			<fr:property name="sortBy" value="uploadTime=desc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<br/><br/>

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=uploadStudyPlanDocument&processId=" + processId.toString() %>" encoding="multipart/form-data">
	
	<% if (request.getAttribute("studyPlanDocumentUploadBean") == null) {
	    	request.setAttribute("studyPlanDocumentUploadBean",new PhdCandidacyDocumentUploadBean(PhdIndividualProgramDocumentType.STUDY_PLAN)); 	
		}
	%>
	
	<fr:edit id="studyPlanDocumentUploadBean"
		name="studyPlanDocumentUploadBean"
		schema="PhdCandidacyDocumentUploadBean.edit.without.documentType">
	
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=uploadStudyPlanDocumentInvalid&processId=" + processId.toString() %>" />
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" /></html:submit>	

</fr:form>

<br/><br/>

<%--  Study Plan --%>
<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong><br/>
<logic:empty name="process" property="studyPlan">
	<bean:message  key="label.phd.noStudyPlan" bundle="PHD_RESOURCES"/><br/><br/>
	<html:link action="/phdIndividualProgramProcess.do?method=prepareCreateStudyPlan" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.createStudyPlan"/>
	</html:link>
</logic:empty>

<logic:notEmpty name="process" property="studyPlan">
	
	<fr:view schema="PhdStudyPlan.view" name="process" property="studyPlan">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>
	</fr:view>
	<bean:define id="confirmationMessage"><bean:message  key="label.confirmation.delete.message" bundle="PHD_RESOURCES"/></bean:define>
	<html:link action="/phdIndividualProgramProcess.do?method=deleteStudyPlan" paramId="processId" paramName="process" paramProperty="externalId" onclick="<%= "return confirm('" + confirmationMessage.toString() + "')" %>"> 
		<bean:message bundle="PHD_RESOURCES" key="label.delete"/>
	</html:link>
	
	<br/><br/>
	<strong><bean:message  key="label.phd.normalCourses" bundle="PHD_RESOURCES"/></strong><br/>
	<bean:define id="studyPlan" name="process" property="studyPlan" />
	<logic:empty name="studyPlan" property="normalEntries">
		<bean:message  key="label.phd.noCourses" bundle="PHD_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="studyPlan" property="normalEntries">
		<fr:view schema="PhdStudyPlanEntry.view" name="studyPlan" property="normalEntries">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="linkFormat(delete)" value="/phdIndividualProgramProcess.do?method=deleteStudyPlanEntry&studyPlanEntryId=${externalId}&processId=${studyPlan.process.externalId}"/>
				<fr:property name="key(delete)" value="label.delete"/>
				<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
				<fr:property name="confirmationKey(delete)" value="label.confirmation.delete.message" />
				<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>	
	<br/><br/>
	<strong><bean:message  key="label.phd.propaedeuticsCourses" bundle="PHD_RESOURCES"/></strong><br/>
	<logic:empty name="studyPlan" property="propaedeuticEntries">
		<bean:message  key="label.phd.noCourses" bundle="PHD_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="studyPlan" property="propaedeuticEntries">
		<fr:view schema="PhdStudyPlanEntry.view" name="studyPlan" property="propaedeuticEntries">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="linkFormat(delete)" value="/phdIndividualProgramProcess.do?method=deleteStudyPlanEntry&studyPlanEntryId=${externalId}&processId=${studyPlan.process.externalId}"/>
				<fr:property name="key(delete)" value="label.delete"/>
				<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
				<fr:property name="confirmationKey(delete)" value="label.confirmation.delete.message" />
				<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
	<br/><br/>
	<p>
		<html:link action="/phdIndividualProgramProcess.do?method=prepareCreateStudyPlanEntry" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.createStudyPlanEntry"/>
		</html:link>
	</p>
	
</logic:notEmpty>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>