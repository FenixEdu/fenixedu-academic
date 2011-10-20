<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<html:xhtml/>

<logic:present role="COORDINATOR">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="title.phd.referee.letters" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="process" name="process" />

<html:link action="/phdIndividualProgramProcess.do?method=viewRefereeLetters" paramId="processId" paramName="process" paramProperty="externalId">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<fr:view name="referee" property="letter">
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter" >
		<fr:slot name="howLongKnownApplicant" layout="null-as-label" />
		<fr:slot name="capacity" layout="null-as-label" />
		<fr:slot name="comparisonGroup" layout="null-as-label" />
		<fr:slot name="rankInClass" layout="null-as-label" />
		<fr:slot name="academicPerformance.localizedName" layout="null-as-label" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.academicPerformance" />
		<fr:slot name="socialAndCommunicationSkills.localizedName" layout="null-as-label" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.socialAndCommunicationSkills"/>
		<fr:slot name="potencialToExcelPhd.localizedName" layout="null-as-label" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.potencialToExcelPhd" />
		<fr:slot name="comments" layout="null-as-label" />
		<fr:slot name="refereeName" layout="null-as-label" />
		<fr:slot name="refereePosition" layout="null-as-label" />
		<fr:slot name="refereeInstitution" layout="null-as-label" />
		<fr:slot name="refereeAddress" layout="null-as-label" />
		<fr:slot name="refereeCity" layout="null-as-label" />
		<fr:slot name="refereeZipCode" layout="null-as-label" />
		<fr:slot name="refereeCountry.localizedName" layout="null-as-label" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereeCountry" />
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
	</fr:layout>
</fr:view>

</logic:present>