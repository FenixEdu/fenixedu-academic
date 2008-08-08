<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
		   	    <fr:property name="rowClasses" value="bold,,"/>
		    </fr:layout>
		</fr:view>
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

