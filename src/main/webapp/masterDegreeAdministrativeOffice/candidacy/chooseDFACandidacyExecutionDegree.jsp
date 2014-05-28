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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<h2><strong><bean:message key="label.dfaCandidacy.create" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>

<fr:form action="/createDfaCandidacy.do?method=createCandidacy">
	<fr:edit id="executionDegree"
			 name="candidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.executionDegree.toCreate">
			 
		<fr:destination name="degreeTypePostback" path="/createDfaCandidacy.do?method=chooseDegreeTypePostBack"/>
		<fr:destination name="degreePostBack" path="/createDfaCandidacy.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/createDfaCandidacy.do?method=chooseDegreeCurricularPlanPostBack"/>		
		<fr:destination name="invalid" path="/createDfaCandidacy.do?method=chooseExecutionDegreeInvalid"/>		
		<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<logic:present name="candidacyBean" property="executionDegree">
	<fr:form action="/createDfaCandidacy.do?method=createCandidacy">
			<fr:edit id="executionDegree-invisible" name="candidacyBean" visible="false"/> 
			<fr:edit id="person"
				 name="candidacyBean"
				 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
				 schema="candidacy.create.choose.person">
				<fr:destination name="invalid" path="/createDfaCandidacy.do?method=chooseExecutionDegreeInvalid"/>					 
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value="listClasses,,"/>
				</fr:layout>
			</fr:edit>
			<br/>
			<html:submit><bean:message key="button.submit" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>		
	</fr:form>
</logic:present>