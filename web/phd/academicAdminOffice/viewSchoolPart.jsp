<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present name="registrationConclusionBean">
	<br/>
	<strong><bean:message  key="label.phd.school.part" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view.registration.conclusion.bean" name="registrationConclusionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
		</fr:layout>
	</fr:view>
	<br/>
</logic:present>
