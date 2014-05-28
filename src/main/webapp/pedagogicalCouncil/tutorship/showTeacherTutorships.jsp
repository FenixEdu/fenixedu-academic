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

<!-- showStudentPerformanceGrid.jsp -->

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.student.performance.grid" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:form action="/studentTutorship.do?method=showStudentPerformanceGrid">
	<fr:edit id="filterForm" name="tutorateBean" schema="tutorship.student.number">
		<fr:edit id="tutorateBean" name="tutorateBean" visible="false" />
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/studentTutorship.do?method=prepareStudentSearch" />
	</fr:edit>
	<html:submit>
		<bean:message key="label.submit" bundle="PEDAGOGICAL_COUNCIL" />
	</html:submit>
</fr:form>


<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
<br/><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>


<logic:present name="student">

	<br/>

	<logic:notPresent name="performanceGridTable">
		<em><bean:message key="label.teacher.tutor.emptyStudentsList" bundle="APPLICATION_RESOURCES" /></em>
	</logic:notPresent>
	
	<logic:present name="performanceGridTable">
		<fr:view name="student" schema="tutorship.tutorate.student">
			<fr:layout name="tabular">
		   	    <fr:property name="classes" value="tstyle2 thright thlight"/>
		   	    <fr:property name="rowClasses" value="bold,,,"/>
		    </fr:layout>
		</fr:view>
		<logic:notEmpty name="tutors">
			<table class="tstyle4 tdcenter mtop15">
				<tr>
					<th colspan="2">
						<bean:message key="label.tutors" bundle="PEDAGOGICAL_COUNCIL"/>
					</th>
				</tr>
				<tr>
					<th>
						<bean:message key="label.tutor.number" bundle="PEDAGOGICAL_COUNCIL"/>
					</th>
					<th>
						<bean:message key="label.tutor.name" bundle="PEDAGOGICAL_COUNCIL"/>
					</th>
				</tr>
				<logic:iterate id="tutor" name="tutors">
					<tr>
						<td>
							<bean:write name="tutor" property="teacherId"/>
						</td>
						<td>
							<bean:write name="tutor" property="person.name"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:notEmpty name="performanceGridTable" property="performanceGridTableLines">
		
		
			<bean:define id="degreeMaxYears" value="<%= request.getAttribute("degreeCurricularPeriods").toString() %>" />
			<%
				Integer numberOfFixedColumns = Integer.valueOf(degreeMaxYears) * 2 + 2;
				
				String columnClasses = "acenter width2em,aleft width16em nowrap,acenter width2em,acenter width2em,acenter width2em,acenter,acenter";
				for(int i=1; i <= (Integer.valueOf(degreeMaxYears) * 2); i++){
					columnClasses += ",nowrap";
				}
			%>
			<fr:view name="performanceGridTable" property="performanceGridTableLines" layout="student-performance-table">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 pgrid"/>
					<fr:property name="columnClasses" value="<%= columnClasses %>" />
					<fr:property name="schema" value="tutorship.tutorate.student.performanceGrid" />
					<fr:property name="numberOfFixedColumns" value="<%= numberOfFixedColumns.toString() %>" />
				</fr:layout>
			</fr:view>
			<ul class="nobullet list2">
				<li class="mvert05"><span class="approvedMonitoringYear performanceGridLegend">&nbsp;</span> Aprovado em <bean:write name="monitoringYear" property="year"/> </li>
				<li class="mvert05"><span class="approvedAnotherYear performanceGridLegend">&nbsp;</span> Aprovado noutro ano lectivo</li>
				<li class="mvert05"><span class="enroled performanceGridLegend">&nbsp;</span> Inscrito e não aprovado em <bean:write name="monitoringYear" property="year"/></li>
				<li class="mvert05"><span class="notApprovedMonitoringYear performanceGridLegend">&nbsp;</span> Reprovado em <bean:write name="monitoringYear" property="year"/></li>
				<li class="mvert05"><span class="notApprovedAnotherYear performanceGridLegend">&nbsp;</span> Inscrito e não aprovado noutro ano lectivo</li>
			</ul>
		</logic:notEmpty>
	</logic:present>

</logic:present>

