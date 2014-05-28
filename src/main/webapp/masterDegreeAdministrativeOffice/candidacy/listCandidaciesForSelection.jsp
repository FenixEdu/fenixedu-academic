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

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.selectCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>

<fr:form action="/selectDFACandidacies.do?method=listCandidacies" >
	<fr:edit id="executionDegree"
			 name="candidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.executionDegree">
		<fr:destination name="degreePostBack" path="/selectDFACandidacies.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/selectDFACandidacies.do?method=chooseDegreeCurricularPlanPostBack"/>	
		<fr:destination name="invalid" path="/selectDFACandidacies.do?method=chooseExecutionDegreeInvalid"/>		
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,,"/>
		</fr:layout>
	</fr:edit>	
	<html:submit><bean:message key="button.submit" /></html:submit>
</fr:form>
<br/>
<br/>
<br/>
<logic:present name="candidacies">
	<logic:empty name="candidacies">
		<bean:message key="label.noCandidacies.found" bundle="ADMIN_OFFICE_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="candidacies">
		<fr:form action="/selectDFACandidacies.do?method=selectCandidacies" >
			<bean:size id="numberOfCandidacies" name="candidacies" />
			<bean:message key="label.numberOfFoundCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:write name="numberOfCandidacies" />
			<fr:edit name="candidacies" schema="candidacy.show.listForSelection" id="candidaciesListForSelection">
				<fr:layout name="tabular-editable">
			        <fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value=",,,acenter"/>
					<fr:property name="sortBy" value="candidacy.number"/>
			    </fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.proceed" bundle="ADMIN_OFFICE_RESOURCES" /></html:submit>	
		</fr:form>
	</logic:notEmpty>
</logic:present>