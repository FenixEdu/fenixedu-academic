<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
	<p><bean:message key="message.phd.public.candidacy.referee.letter.submited" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="PHD_RESOURCES" />
	
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
	<p><bean:message key="message.phd.institution.public.candidacy.referee.letter.submited.with.success" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="PHD_RESOURCES" />
</logic:present>

<logic:present name="no-information">
	<p><bean:message key="message.phd.public.candidacy.referee.letter.no.information.found" bundle="PHD_RESOURCES" />
</logic:present>