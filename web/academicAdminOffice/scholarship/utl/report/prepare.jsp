<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE" >

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="title.academicAdminOffice.scholarship.utl.report" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>	

<fr:form action="/reportStudentsUTLCandidates.do?method=showReport" encoding="multipart/form-data" >
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-selection-year" name="bean" >
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report.ReportStudentsUTLCandidatesBean" 
			bundle="ACADEMIC_OFFICE_RESOURCES">
			
			<fr:slot name="executionYear" required="true" layout="menu-select" >
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
				<fr:property name="sortBy" value="year=desc" />
			</fr:slot>
			<fr:slot name="xlsFile" >
				<property name="fileNameSlot" value="fileName" />
				<property name="fileSizeSlot" value="fileSize" />
			</fr:slot>
			
		</fr:schema>
	</fr:edit>

	<p>
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>	
</fr:form>

</logic:present>
