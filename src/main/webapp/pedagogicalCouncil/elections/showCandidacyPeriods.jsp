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

<bean:define id="forwardTo" value="showCandidacyPeriods" />

<h2><bean:message key="label.showCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.currentExecutionYear" bundle="PEDAGOGICAL_COUNCIL" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<logic:present name="electionPeriodBean" >
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.showCandidacyPeriod.selectDegreeTypeAndExecutionYear" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.showCandidacyPeriod.selectDegreeTypeAndExecutionYear.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
	<fr:form action="<%="/electionsPeriodsManagement.do?method=selectDegreeType&forwardTo=" + forwardTo %>">
			<fr:edit id="electionPeriodBean" name="electionPeriodBean" layout="tabular-editable" schema="elections.selectDegreeTypeAndExecutionYear">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="<%="/electionsPeriodsManagement.do?method=prepare&forwardTo=" + forwardTo %>" />
				<fr:destination name="post-back" path="<%="/electionsPeriodsManagement.do?method=selectDegreeType&forwardTo=" + forwardTo %>" />
			</fr:edit>
		</fr:form>
</logic:present>

<logic:present name="electionsByDegreeBean" >
	<bean:define id="degreeTypeName" name="electionPeriodBean" property="degreeType.name" type="java.lang.String"/>
	<bean:define id="degreeMaxYears" name="electionPeriodBean" property="degreeType.years"  />
	
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.showCandidacyPeriod.selectPeriods" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.showCandidacyPeriod.selectPeriods.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
	<%
		String columnClasses = "";
		for(int i=1; i <= (Integer)degreeMaxYears; i++){
			columnClasses += ",width200px";
		}
	%>
	
		<fr:view name="electionsByDegreeBean" layout="tabular" schema="<%=  degreeTypeName + ".elections.showCandidacyPeriods" %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
				<fr:property name="columnClasses" value="<%= columnClasses %>"/>
			</fr:layout>
			<fr:destination name="invalid" path="<%="/electionsPeriodsManagement.do?method=prepare&forwardTo=" + forwardTo %>" />
			<fr:destination name="post-back" path="<%="/electionsPeriodsManagement.do?method=selectDegreeType&forwardTo=" + forwardTo %>" />
		</fr:view>
</logic:present>