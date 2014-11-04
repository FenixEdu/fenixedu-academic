<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="forwardTo" value="showCandidacyPeriods" />

<h2><bean:message key="label.sendEmailToStudents" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.currentExecutionYear" bundle="PEDAGOGICAL_COUNCIL" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<logic:present name="electionPeriodBean" >
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.showCandidacyPeriod.selectDegreeTypeAndExecutionYear" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	
	<fr:form action="/sendEmailToStudents.do?method=selectDegreeTypePostBack">
			<fr:edit id="electionPeriodBean" name="electionPeriodBean" layout="tabular-editable" schema="elections.selectDegreeTypeAndExecutionYear">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/sendEmailToStudents.do?method=prepare" />
				<fr:destination name="post-back" path="/sendEmailToStudents.do?method=selectDegreeTypePostBack" />
			</fr:edit>
		</fr:form>
</logic:present>

<logic:present name="degrees" >
	<bean:define id="degreeTypeName" name="electionPeriodBean" property="degreeType.name" type="java.lang.String"/>
	<bean:define id="degreeMaxYears" name="electionPeriodBean" property="degreeType.years" type="java.lang.Integer" />
	<bean:define id="executionYear" name="electionPeriodBean" property="executionYear.year" type="java.lang.String" />
	
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.showCandidacyPeriod.selectPeriods" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	
	<%
		String columnClasses = "";
		for(int i=1; i <= (Integer)degreeMaxYears; i++){
			columnClasses += ",width200px";
		}
	%>

	<table class="tstyle1 thlight tdcenter mtop0">
		<tr>
		
			<th>
				<bean:message key="label.degree" bundle="PEDAGOGICAL_COUNCIL"/>
			</th>

	<% 
		for(int i = 1 ; i <= degreeMaxYears ; i++){
	%>
			<th class="width200px">
				<bean:message key="label.years" arg0="<%= "" + i %>" bundle="PEDAGOGICAL_COUNCIL"/>
			</th>
	<%
		}
	%>
		</tr>
	<logic:iterate id="degree" name="degrees">
		<bean:define id="degreeId" name="degree" property="externalId"/>
		<tr>
			<td>
				<bean:write name="degree" property="sigla"/>
			</td>
				<% 
					for(int i = 1 ; i <= degreeMaxYears ; i++){
				%>
						<td class="width200px">
							<html:link action='<%= "/sendEmailToStudents.do?method=sendMail" + "&amp;degreeId=" + degreeId + "&amp;curricularYear=" + i +"&amp;executionYear=" + executionYear %>'><bean:message key="label.sendMail" bundle="PEDAGOGICAL_COUNCIL"/></html:link>
						</td>
				<%
					}
				%>
		</tr>		
	</logic:iterate>
	</table>

</logic:present>