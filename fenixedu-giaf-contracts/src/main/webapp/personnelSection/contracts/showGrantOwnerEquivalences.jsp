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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.grantOwnerEquivalences" bundle="CONTRACTS_RESOURCES"/></h2>

<logic:present name="grantOwnerEquivalences">
	<fr:edit name="grantOwnerEquivalences">
		<fr:schema type="org.fenixedu.academic.domain.personnelSection.contracts.GrantOwnerEquivalent" bundle="CONTRACTS_RESOURCES">
			<fr:slot readOnly="true" name="giafId" key="label.giafId" layout="null-as-label"/>
			<fr:slot readOnly="true" name="name" key="label.situation" layout="null-as-label"/>
			<fr:slot name="isSabaticalOrEquivalent" layout="null-as-label"/>
			<fr:slot name="hasMandatoryCredits" key="label.hasMandatoryCredits" layout="null-as-label"/>
			<fr:slot name="giveCredits" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="sortBy" value="giafId"/>
		</fr:layout>
	</fr:edit>
</logic:present>
