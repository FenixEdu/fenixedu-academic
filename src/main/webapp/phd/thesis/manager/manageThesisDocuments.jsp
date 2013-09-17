<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="MANAGER">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageThesisDocuments" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
  </tr>
</table>
<%--  ### End Of Context Information  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<br/>

<%--  ### Documents  ### --%>
<logic:empty name="process" property="latestDocumentVersions">
	<strong><bean:message  key="label.phd.documents" bundle="PHD_RESOURCES"/></strong>
	<br/>
	<bean:message  key="label.phd.noDocuments" bundle="PHD_RESOURCES"/>
</logic:empty>

<logic:notEmpty name="process" property="latestDocumentVersions">	
	
	<strong><bean:message  key="label.phd.documents" bundle="PHD_RESOURCES"/></strong>
	<p>
	<span class="compress">
	<html:link action="/phdThesisProcess.do?method=downloadThesisDocuments" paramId="processId" paramName="processId">
		<bean:message key="label.phd.documents.download.all" bundle="PHD_RESOURCES" />
	</html:link>
	</span>
	</p>
	

	<fr:view schema="PhdProgramProcessDocument.view" name="process" property="latestDocumentVersions">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:link 	name="view" link="${downloadUrl}" label="label.view,PHD_RESOURCES"
						order="0" contextRelative="false" hasContext="false" />
			
			<fr:property name="sortBy" value="documentType=asc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<%--  ### End Of Documents  ### --%>
<br/><br/>

<%--  ### End of Operation Area  ### --%>

</logic:present>
