<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="forwardTo" value="showVotingPeriods" />

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="label.showVotingPeriods" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.executionYear" bundle="PEDAGOGICAL_COUNCIL" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<logic:present name="electionPeriodBean" >
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.elections.showVotingPeriod.selectDegreeTypeAndExecutionYear" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.showVotingPeriod.selectDegreeTypeAndExecutionYear.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
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
		<b><bean:message key="label.elections.showVotingPeriod.selectPeriods" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.showVotingPeriod.selectPeriods.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
	
	<%
		String columnClasses = "";
		for(int i=1; i <= (Integer)degreeMaxYears; i++){
			columnClasses += ",width200px";
		}
	%>
	
		<fr:view name="electionsByDegreeBean" layout="tabular" schema="<%=  degreeTypeName + ".elections.showVotingPeriods" %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
				<fr:property name="columnClasses" value="<%= columnClasses %>"/>
			</fr:layout>
			<fr:destination name="invalid" path="<%="/electionsPeriodsManagement.do?method=prepare&forwardTo=" + forwardTo %>" />
			<fr:destination name="post-back" path="<%="/electionsPeriodsManagement.do?method=selectDegreeType&forwardTo=" + forwardTo %>" />
		</fr:view>
</logic:present>