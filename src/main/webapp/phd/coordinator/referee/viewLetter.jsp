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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<html:xhtml/>

<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
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