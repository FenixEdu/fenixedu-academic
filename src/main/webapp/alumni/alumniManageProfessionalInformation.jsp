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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumniManageProfessionalInformation.jsp -->
<h2>
	<bean:message key="label.create.professional.information" bundle="ALUMNI_RESOURCES" />
</h2>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p class="mbottom05 mtop15"><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<logic:present name="jobCreateBean">
	<bean:define id="schema" name="jobCreateBean" property="schema" type="java.lang.String" />
	<fr:edit id="jobCreateBean" name="jobCreateBean" schema="<%= schema.toString() %>" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thmiddle thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="updateBusinessAreaPostback" path="/professionalInformation.do?method=createBusinessAreaPostback"/>
		<fr:destination name="success" path="/professionalInformation.do?method=createProfessionalInformation"/>
		<fr:destination name="invalid" path="/professionalInformation.do?method=prepareProfessionalInformationCreation"/>
		<fr:destination name="cancel" path="/professionalInformation.do?method=viewProfessionalInformation"/>
	</fr:edit>
</logic:present>

<logic:present name="jobUpdateBean">
	<bean:define id="schema" name="jobUpdateBean" property="schema" type="java.lang.String" />
	<fr:edit id="jobUpdateBean" name="jobUpdateBean" schema="<%= schema %>" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thmiddle thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="updateBusinessAreaPostback" path="/professionalInformation.do?method=updateBusinessAreaPostback"/>
		<fr:destination name="success" path="/professionalInformation.do?method=updateProfessionalInformation"/>
		<fr:destination name="invalid" path="/professionalInformation.do?method=updateProfessionalInformationError"/>
		<fr:destination name="cancel" path="/professionalInformation.do?method=viewProfessionalInformation"/>
	</fr:edit>	
</logic:present>

<p class="mbottom0">
	<em>Nota: <bean:message key="date.note" bundle="ALUMNI_RESOURCES" /></em>
</p>