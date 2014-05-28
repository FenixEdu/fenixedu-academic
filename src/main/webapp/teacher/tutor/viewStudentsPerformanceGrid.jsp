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

<h2><bean:message key="label.teacher.tutor.viewStudentsPerformanceGrid"/></h2>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<fr:view name="tutor" schema="teacher.tutor.name">
	<fr:layout name="tabular">
   	    <fr:property name="classes" value="tstyle2 thright thlight"/>
   	    <fr:property name="rowClasses" value="bold,,"/>
    </fr:layout>
</fr:view>

<logic:equal name="tutor" property="teacher.numberOfTutorships" value="0">
	<p class="mtop1">
		<em>
			<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.tutor.emptyStudentsList" />
		</em>
	</p>
</logic:equal>

<logic:notEqual name="tutor" property="teacher.numberOfTutorships" value="0">

	<logic:present name="performanceGridFiltersBean">
		<fr:form action="/viewStudentsPerformanceGrid.do?method=prepare">
			<fr:edit id="performanceGridFiltersBean" name="performanceGridFiltersBean" layout="tabular-editable" schema="teacher.tutor.performanceGrid.filters">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mbottom0"/>
					<fr:property name="columnClasses" value="width12em,width35em,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/viewStudentsPerformanceGrid.do?method=prepare&gridSet=" />
				<fr:destination name="post-back" path="/viewStudentsPerformanceGrid.do?method=prepare&gridSet=" />
				<fr:destination name="changeDegreePostBack" path="/viewStudentsPerformanceGrid.do?method=changeDegree&gridSet=" />
			</fr:edit>
		</fr:form>
	</logic:present>

	<logic:present name="performanceGridTable">
		
		<logic:notEmpty name="performanceGridTable" property="performanceGridTableLines">
			<bean:define id="degreeMaxYears" name="performanceGridFiltersBean" property="degreeCurricularPeriod"  />
			<%
				Integer numberOfFixedColumns = ((Integer)degreeMaxYears) * 2 + 2;
				
				String columnClasses = "acenter width2em,aleft width16em nowrap,acenter width2em,acenter width2em,acenter width2em,acenter,acenter";
				for(int i=1; i <= ((Integer)degreeMaxYears) * 2; i++){
					columnClasses += ",nowrap";
				}
			%>
			<fr:view name="performanceGridTable" property="performanceGridTableLines" layout="student-performance-table">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 pgrid"/>
					<fr:property name="columnClasses" value="<%= columnClasses %>" />
					<fr:property name="schema" value="teacher.tutor.performanceGrid.eachStudent" />
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

		<logic:present name="tutorStatistics">
			<logic:notEmpty name="tutorStatistics">
				<div style="width: 400px; float: left;">
					<p class="mtop2 mbottom025">
						<em>
							<bean:message bundle="APPLICATION_RESOURCES" key="label.executionYear" />
							<bean:write name="monitoringYear" property="year" /><br/>
						</em>
					</p>
					<h3 class="mtop025">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.tutorStatistics" />
					</h3>
					
					<table class="tstyle1 thlight tdcenter">
						<tr>
							<th><bean:message bundle="APPLICATION_RESOURCES" key="label.approvedEnrolments" /></th>
							<th><bean:message bundle="APPLICATION_RESOURCES" key="label.studentsNumber" /></th>
							<th colspan="2"><bean:message bundle="APPLICATION_RESOURCES" key="label.studentsPercentage" /></th>
						</tr>
		
						<logic:iterate id="statistics" name="tutorStatistics" type="net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.TutorStatisticsBean">
							<tr>
								<td width="75px"><%= statistics.getApprovedEnrolmentsNumber()  %></td>
								<td width="75px"><%= statistics.getStudentsNumber() %></td>
								<td><%= statistics.getStudentsRatio() %>%</td>
								<td width="100px"><div style="background: #369; width: <%= statistics.getStudentsRatio() %>px;">&nbsp;</div></td>
							</tr>
						</logic:iterate>
			 			 
					</table>
					<p class="mtop05">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.totalTutorStudents" />:
						<bean:write name="totalStudents" />
					</p>
				</div>
			</logic:notEmpty>		
		</logic:present>
	
		<logic:present name="performanceGridFiltersBean">
			<bean:define id="filtersBean" name="performanceGridFiltersBean" type="net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean"/>
			<bean:define id="degreeOID" value="<%= filtersBean.getDegree().getExternalId().toString() %>" />
			<bean:define id="entryYearOID" value="<%= filtersBean.getStudentsEntryYear().getExternalId().toString() %>" />
			<bean:define id="monitoringYearOID" value="<%= filtersBean.getCurrentMonitoringYear().getExternalId().toString() %>" />
			<bean:define id="parameters" value="<%="degreeOID=" + degreeOID + "&entryYearOID=" + entryYearOID + "&monitoringYearOID=" + monitoringYearOID %>" />
			
			<div style="width: 400px; float: left;">
	
			<logic:notPresent name="allStudentsStatistics">
					<p class="mtop2 mbottom025">
						<em>
							<bean:message bundle="APPLICATION_RESOURCES" key="label.executionYear" />
							<bean:write name="monitoringYear" property="year" /><br/>
						</em>
					</p>
					<h3 class="mtop025">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.allStudentsStatistics" />
					</h3>
					<html:link page="<%="/viewStudentsPerformanceGrid.do?method=prepareAllStudentsStatistics&" + parameters%>">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.allStudentsStatistics.link" /> <%= filtersBean.getStudentsEntryYear().getYear()  %>
					</html:link> 
			</logic:notPresent>
				
			<logic:present name="allStudentsStatistics">
				<logic:notEmpty name="allStudentsStatistics">
					<p class="mtop2 mbottom025">
						<em>
							<bean:message bundle="APPLICATION_RESOURCES" key="label.executionYear" />
							<bean:write name="monitoringYear" property="year" /><br/>
						</em>
					</p>
					<h3 class="mtop025">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.allStudentsStatistics" />
					</h3>
					
					<table class="tstyle1 thlight tdcenter">
						<tr>
							<th><bean:message bundle="APPLICATION_RESOURCES" key="label.approvedEnrolments" /></th>
							<th><bean:message bundle="APPLICATION_RESOURCES" key="label.studentsNumber" /></th>
							<th colspan="2"><bean:message bundle="APPLICATION_RESOURCES" key="label.studentsPercentage" /></th>
						</tr>
						<logic:iterate id="studentStatistics" name="allStudentsStatistics" type="net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.TutorStatisticsBean">
							<tr>
								<td width="75px"><%= studentStatistics.getApprovedEnrolmentsNumber()  %></td>
								<td width="75px"><%= studentStatistics.getStudentsNumber() %></td>
								<td><%= studentStatistics.getStudentsRatio() %>%</td>
								<td width="100px"><div style="background: #369; width: <%= studentStatistics.getStudentsRatio() %>px;">&nbsp;</div></td>
							</tr>
						</logic:iterate>
					</table>
			
					<p class="mtop05">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.totalEntryStudents" />
						<bean:write name="entryYear" property="year" />: 
						<bean:write name="totalEntryStudents" />
					</p>
				</logic:notEmpty>
			</logic:present>
		</logic:present>
		</div>
	</logic:present>
	<logic:notPresent name="performanceGridTable">
		<p class="mtop1">
			<em>
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.tutor.no.results" />
			</em>
		</p>
	</logic:notPresent>

</logic:notEqual>

<div class="cboth"></div>