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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<html:xhtml/>

<h2><bean:message key="label.inviteExternalPerson" bundle="WEBSITEMANAGER_RESOURCES"/></h2>
<bean:define id="siteID" name="site" property="externalId"/>

<fr:view name="bean" schema="research.contract.information">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright"/>
	</fr:layout>
</fr:view>


<bean:define id="schema" value="research.contract.extraInformation.for.externalPerson"/>

<logic:present name="bean" property="documentIDNumber">
	<bean:define id="schema" value="research.contract.extraInformation.for.internalPerson"/>
</logic:present>

<fr:edit id="extraInfo" name="bean" schema="<%= schema %>" action="<%= "/manageResearchUnitSite.do?method=addNewPerson&oid=" + siteID %>" >
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		<fr:destination name="invalid" path="<%= "/manageResearchUnitSite.do?method=prepareAddNewPerson&oid=" + siteID %>"/>
	</fr:layout>
</fr:edit>

