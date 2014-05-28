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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><strong><bean:message key="label.candidacy.dfa.periodsManagement" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<br/>

<fr:view name="executionDegrees" schema="DFAPeriodsManagement.ExecutionDegree.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="linkFormat(editCandidacyPeriod)" value="/dfaPeriodsManagement.do?method=prepareEditCandidacyPeriod&executionDegreeId=${externalId}"/>
		<fr:property name="key(editCandidacyPeriod)" value="link.candidacy.dfa.editCandidacyPeriod"/>
		<fr:property name="bundle(editCandidacyPeriod)" value="ADMIN_OFFICE_RESOURCES" />
		<fr:property name="linkFormat(editRegistrationPeriod)" value="/dfaPeriodsManagement.do?method=prepareEditRegistrationPeriod&executionDegreeId=${externalId}"/>
		<fr:property name="key(editRegistrationPeriod)" value="link.candidacy.dfa.editRegistrationPeriod"/>
		<fr:property name="bundle(editRegistrationPeriod)" value="ADMIN_OFFICE_RESOURCES" />
	</fr:layout>
</fr:view>
