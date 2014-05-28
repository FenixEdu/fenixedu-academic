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

<html:xhtml/>


<br />
<h2><bean:message key="label.externalUnits" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>



<fr:edit id="searchBean" name="searchBean" schema="ExternalUnitsSearchBean.search" action="/externalUnits.do?method=search" >
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="cancel" path="/externalUnits.do?method=prepareSearch" />
</fr:edit>


<logic:present name="searchBean" property="results">

	<logic:notEmpty name="searchBean" property="results">
		<fr:view name="searchBean" property="results" schema="AbstractExternalUnitResultBean.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight"/>
				<fr:property name="columnClasses" value=",,acenter,acenter,acenter,acenter"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="searchBean" property="results">
		<p>
			<em><bean:message key="label.externalUnits.noSearchResults" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>


	<bean:define id="earthUnitId">&amp;oid=<bean:write name="searchBean" property="earthUnit.externalId" /></bean:define>
	<ul>
		<li><html:link page="<%="/externalUnits.do?method=prepareCreateCountry" + earthUnitId %>"><bean:message key="label.externalUnits.createCountry" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	</ul>


</logic:present>

