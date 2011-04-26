<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.phd.candidacy.referee.letter" bundle="CANDIDATE_RESOURCES"/>
</div>

<br/>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2><bean:message key="title.phd.candidacy.referee.letter" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>

<logic:present name="has-letter">
	<p><bean:message key="message.phd.public.candidacy.referee.letter.submited" bundle="PHD_RESOURCES" />
	
	<fr:view name="letter">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter">
			<fr:slot name="howLongKnownApplicant" />
			<fr:slot name="capacity" />
			<fr:slot name="comparisonGroup" />
			<fr:slot name="rankInClass" />
			<fr:slot name="academicPerformance" />
			<fr:slot name="socialAndCommunicationSkills" />
			<fr:slot name="potencialToExcelPhd" />
			<fr:slot name="file" layout="link" />
			<fr:slot name="comments" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>		
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="created-with-success">
	<p><bean:message key="message.phd.public.candidacy.referee.letter.submited.with.success" bundle="PHD_RESOURCES" />
</logic:present>

<logic:present name="no-information">
	<p><bean:message key="message.phd.public.candidacy.referee.letter.no.information.found" bundle="PHD_RESOURCES" />
</logic:present>