<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES" /></a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<br/>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2>Reference Letter</h2>

<%-- ### End of Title ### --%>

<logic:present name="has-letter">
	<p>You have already submitted the recommendation shown below. Thank you for the time and effort you have taken in responding to this request. We greatly appreciate your opinion on this application. If you have any questions or require further information, please contact the <a href="http://ist.utl.pt"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES" /></a> doctoral program at ist-epfl@ist.utl.pt.</p>
	
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
	<p>Thank you for the time and effort you have taken in responding to this request. We greatly appreciate your opinion on this application. If you have any questions or require further information, please contact the <a href="http://ist.utl.pt"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES" /></a> doctoral program at ist-epfl@ist.utl.pt.</p>
</logic:present>

<logic:present name="no-information">
	<p>No information found</p>
</logic:present>