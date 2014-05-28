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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="forwardTo" value="createEditCandidacyPeriods" />

<h2><bean:message key="label.createEditCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1">
		<b><bean:message key="label.executionYear" bundle="PEDAGOGICAL_COUNCIL" />:</b>
		<bean:write name="currentExecutionYear" property="year" />
	</p>
</logic:present>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="electionPeriodBean" >
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.createCandidacyPeriod.selectDegreeType" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.createCandidacyPeriod.selectDegreeType.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
		
	<fr:form action="<%="/electionsPeriodsManagement.do?method=selectDegreeType&forwardTo=" + forwardTo %>">
			<fr:edit id="electionPeriodBean" name="electionPeriodBean" layout="tabular-editable" schema="elections.selectDegreeType">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop0 mbottom0 thmiddle"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="<%="/electionsPeriodsManagement.do?method=prepare&forwardTo=" + forwardTo %>" />
				<fr:destination name="post-back" path="<%="/electionsPeriodsManagement.do?method=selectDegreeType&forwardTo=" + forwardTo %>" />
			</fr:edit>
		</fr:form>
</logic:present>

<!-- SELECT PAIR (DEGREE , CURRICULAR YEAR) TO CREATE CANDIDACY PERIOD  -->
<logic:present name="electionsByDegreeBean" >
	<bean:define id="degreeTypeName" name="electionPeriodBean" property="degreeType.name" type="java.lang.String"/>
	<bean:define id="degreeMaxYears" name="electionPeriodBean" property="degreeType.years"  />
	
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.createEditCandidacyPeriod.selectPeriods" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.createEditCandidacyPeriod.selectPeriods.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
	<%
		String columnClasses = ",";
		for(int i=1; i <= (Integer)degreeMaxYears; i++){
			columnClasses += ",width200px nowrap";
		}
	%>
	
	<fr:form action="/electionsPeriodsManagement.do?method=manageYearDelegateCandidacyPeriods">
		<fr:edit id="electionPeriodBean" name="electionPeriodBean" visible="false" />
		<fr:view name="electionsByDegreeBean" layout="tabular" schema="<%=  degreeTypeName + ".elections.createEditPeriods" %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop05 mbottom05"/>
				<fr:property name="columnClasses" value="<%= columnClasses %>"/>
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="selectedDegrees" />
				<fr:property name="checkboxValue" value="degree.externalId" />
				<fr:property name="selectAllShown" value="true" />
				<fr:property name="selectAllLocation" value="top" />
			</fr:layout>
			<fr:destination name="invalid" path="<%="/electionsPeriodsManagement.do?method=prepare&forwardTo=" + forwardTo %>" />
			<fr:destination name="post-back" path="<%="/electionsPeriodsManagement.do?method=selectDegreeType&forwardTo=" + forwardTo %>" />
		</fr:view>
		<p>
			<html:submit property="create"><bean:message key="button.elections.createPeriods" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
			<html:submit property="edit"><bean:message key="button.elections.editPeriods" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
			<html:submit property="delete"><bean:message key="button.elections.deletePeriods" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
		</p>
	</fr:form>
</logic:present>

<% String schema = ""; %>

<!-- CREATE ONE OR MORE THAN ONE CANDIDACY PERIOD  -->
<logic:present name="newElectionPeriodBean" >
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.createCandidacyPeriod.createPeriod" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.createCandidacyPeriod.createPeriod.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
	<fr:form action="/createElectionsPeriods.do?method=createYearDelegateCandidacyPeriods">
		<fr:edit id="electionPeriodBean" name="electionPeriodBean" visible="false" />
		
		<logic:present name="selectedDegrees" >
			<% schema += "elections.electionsPeriods"; %>
			<fr:edit id="selectedDegrees" name="selectedDegrees" visible="false" />
		</logic:present>
		<logic:notPresent name="selectedDegrees" >
			<% schema += "elections.singleElectionPeriod"; %>
		</logic:notPresent>
		
		<fr:edit id="newElectionPeriodBean" name="newElectionPeriodBean" layout="tabular-editable" schema="<%= schema %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight thleft thmiddle mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width100px,aleft width250px,tdclear tderror1"/>
			</fr:layout>
			
			<logic:present name="selectedDegrees" >
				<bean:define id="parameters" value="<%= "forwardTo=" + forwardTo %>" />
				<fr:destination name="invalid" path="<%= "/createElectionsPeriods.do?method=prepareCreateYearDelegateElectionsPeriods&" + parameters %>"/>
			</logic:present>
			<logic:notPresent name="selectedDegrees" >
				<bean:define id="selectedYear" name="newElectionPeriodBean" property="curricularYear.year"  />
				<bean:define id="degreeOID" name="newElectionPeriodBean" property="degree.externalId"  />
				<bean:define id="parameters" value="<%= "forwardTo=" + forwardTo + "&selectedYear=" + selectedYear + "&selectedDegree=" + degreeOID %>" />
				<fr:destination name="invalid" path="<%="/createElectionsPeriods.do?method=prepareCreateYearDelegateCandidacyPeriod&" + parameters %>" />
			</logic:notPresent>
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width250px">
					<html:submit><bean:message key="button.elections.createPeriod" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<!-- EDIT ONE OR MORE THAN ONE CANDIDACY PERIOD  -->
<logic:present name="editElectionBean" >
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.editCandidacyPeriod.editPeriod" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.editCandidacyPeriod.editPeriod.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
	<fr:form action="/editElectionsPeriods.do?method=editYearDelegateCandidacyPeriods">
		<fr:edit id="electionPeriodBean" name="electionPeriodBean" visible="false" />
		
		<logic:present name="selectedDegrees" >
			<% schema += "elections.electionsPeriods"; %>
			<fr:edit id="selectedDegrees" name="selectedDegrees" visible="false" />
		</logic:present>
		<logic:notPresent name="selectedDegrees" >
			<% schema += "elections.singleElectionPeriod"; %>
		</logic:notPresent>
		
		<fr:edit id="editElectionBean" name="editElectionBean" layout="tabular-editable" schema="<%= schema %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight thleft thmiddle mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width100px,aleft width250px,tdclear tderror1"/>
			</fr:layout>
			
			<logic:present name="selectedDegrees" >
				<bean:define id="parameters" value="<%= "forwardTo=" + forwardTo %>" />
				<fr:destination name="invalid" path="<%= "/editElectionsPeriods.do?method=prepareEditYearDelegateElectionsPeriods&" + parameters %>" />
			</logic:present>
			<logic:notPresent name="selectedDegrees" >
				<bean:define id="selectedPeriod" name="editElectionBean" property="election.externalId"  />
				<bean:define id="parameters" value="<%= "selectedPeriod=" + selectedPeriod %>" />
				<fr:destination name="invalid" path="<%="/editElectionsPeriods.do?method=prepareEditYearDelegateCandidacyPeriod&" + parameters %>" />
			</logic:notPresent>
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width250px">
					<html:submit><bean:message key="button.elections.editPeriod" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
					<logic:notPresent name="selectedDegrees" >
						<html:submit property="delete"><bean:message key="button.elections.deletePeriod" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
					</logic:notPresent>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<!-- DELETE MORE THAN ONE CANDIDACY PERIOD  -->
<logic:present name="deleteElectionBean" >
	<bean:define id="parameters" value="<%= "forwardTo=" + forwardTo %>" />
	
	<p class="mtop15 mbottom05">
		<span class="warning0"><b><bean:message key="label.elections.deleteCandidacyPeriod.deletePeriod" bundle="PEDAGOGICAL_COUNCIL" /></b></span></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.deleteCandidacyPeriod.deletePeriod.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
	<fr:form action="/editElectionsPeriods.do?method=editYearDelegateCandidacyPeriods">
		<fr:edit id="electionPeriodBean" name="electionPeriodBean" visible="false" />
		<fr:edit id="selectedDegrees" name="selectedDegrees" visible="false" />
		
		<fr:edit id="editElectionBean" name="deleteElectionBean" layout="tabular-editable" schema="elections.deleteElectionsPeriods">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight thleft thmiddle mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width100px,aleft width250px,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="<%= "/editElectionsPeriods.do?method=prepareDeleteYearDelegateElectionsPeriods&" + parameters  %>" />
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width250px">
					<html:submit property="delete"><bean:message key="button.elections.deletePeriods" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>