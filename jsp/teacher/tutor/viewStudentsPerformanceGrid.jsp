<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.teacher.tutor.operations" /></em>
<h2><bean:message key="label.teacher.tutor.viewStudentsPerformanceGrid"/></h2>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<fr:view name="tutor" schema="teacher.tutor.name">
	<fr:layout name="tabular">
   	    <fr:property name="classes" value="tstyle2 thright thlight"/>
   	    <fr:property name="rowClasses" value="bold,,"/>
    </fr:layout>
</fr:view>

<logic:notEmpty name="tutor" property="teacher.activeTutorships">
	<logic:present name="performanceGridFiltersBean">
		<fr:form action="/viewStudentsPerformanceGrid.do?method=prepare">
			<fr:edit id="performanceGridFiltersBean" name="performanceGridFiltersBean" layout="tabular-editable" schema="teacher.tutor.performanceGrid.filters">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/viewStudentsPerformanceGrid.do?method=prepare" />
				<fr:destination name="post-back" path="/viewStudentsPerformanceGrid.do?method=prepare" />
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
	</logic:present>
	
	
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
							<td width="100px"><div style="background: #369; width: <%= Float.parseFloat(statistics.getStudentsRatio()) %>px;">&nbsp;</div></td>
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
		<bean:define id="degreeOID" value="<%= filtersBean.getDegree().getIdInternal().toString() %>" />
		<bean:define id="entryYearOID" value="<%= filtersBean.getStudentsEntryYear().getIdInternal().toString() %>" />
		<bean:define id="monitoringYearOID" value="<%= filtersBean.getCurrentMonitoringYear().getIdInternal().toString() %>" />
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
								<td width="100px"><div style="background: #369; width: <%= Float.parseFloat(studentStatistics.getStudentsRatio()) %>px;">&nbsp;</div></td>
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
		</div>
	</logic:present>
</logic:notEmpty>
<logic:empty name="tutor" property="teacher.activeTutorships">
		<p class="mtop1">
		<em>
			<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.tutor.emptyStudentsList" />
		</em>
		</p>
</logic:empty>

<div class="cboth"></div>