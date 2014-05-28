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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>

<h2><strong><bean:message key="label.person.title.personal.info" /></strong></h2>
<fr:view name="candidacy" property="person" schema="candidate.personalData" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.person.title.filiation" /></strong></h2>
<fr:view name="candidacy" property="person" schema="candidate.filiation" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.person.title.addressInfo" /></strong></h2>
<fr:view name="candidacy" property="person" schema="candidate.address" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.person.title.contactInfo" /></strong></h2>
<fr:view name="candidacy" property="person" schema="candidate.contacts" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ADMIN_OFFICE_RESOURCES" /></strong></h2>
<fr:view name="candidacy" property="precedentDegreeInformation" schema="candidate.precedentDegreeInformation" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>	

<logic:equal name="candidacy" property="activeCandidacySituation.canCandidacyDataBeValidated" value="true">
	<html:form action="/dfaCandidacy.do">	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="validateData"/>
		<bean:define id="number" name="candidacy" property="number"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidacyNumber" property="candidacyNumber" value="<%= number.toString() %>"/>
		<br/><br/>
		<html:submit><bean:message key="button.validate" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>	
	</html:form>
</logic:equal>

<logic:notEqual name="candidacy" property="activeCandidacySituation.canCandidacyDataBeValidated" value="true">
	<bean:define id="link">
		/listDFACandidacy.do?method=viewCandidacy&candidacyID=<bean:write name="candidacy" property="externalId" />
	</bean:define>	
	<html:link action="<%= link %>" >
		<bean:message key="link.candidacy.back" bundle="ADMIN_OFFICE_RESOURCES" />
	</html:link>
</logic:notEqual>