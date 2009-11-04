<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>

<h2>
	<bean:message key="title.finalDegreeWorkProposal"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>	

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
	<div class="infoop2">
		<p>
			<strong><bean:message key="message.final.degree.work.other.execution.degrees"/></strong>
		</p>
			<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
				<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID %>">
					<p class="mvvert05">
						<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
					</p>
				</logic:notEqual>
			</logic:iterate>
	</div>
</logic:notEqual>
</logic:present>

<%--
<div class="infoop2">
	<p>Info msg</p>
</div>
--%>

<p>
	<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="button.back"/></html:link>
</p>

<div id="wrap">
			<fr:form action="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>">
            
            <p class="mbottom05"><strong><bean:message key="finalDegreeWorkProposal.setProposalPeriod.header"/></strong></p>
	        <table class="tstyle5 thlight thleft tdright mtop05 mbottom05">
				<tr>
					<th><bean:message key="finalDegreeWorkProposal.setProposalPeriod.start"/></th>
					<td>
						<fr:edit name="executionDegree" slot="scheduling.startOfProposalPeriodDateYearMonthDay" validator="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
					</td>
					<td>
					<fr:edit name="executionDegree" slot="scheduling.startOfProposalPeriodTimeHourMinuteSecond"/>
						
					</td>
				</tr>
				<tr>
					<th><bean:message key="finalDegreeWorkProposal.setProposalPeriod.end"/></th>
					<td>
						<fr:edit name="executionDegree" slot="scheduling.endOfProposalPeriodDateYearMonthDay" validator="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
						
					</td>
					<td>
						<fr:edit name="executionDegree" slot="scheduling.endOfProposalPeriodTimeHourMinuteSecond"/>
						
					</td>
				</tr>

			</table>

			<p class="mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header"/></strong></p>
			<table class="tstyle5 thlight thleft tdright mtop05 mbottom05">
				<tr><th><bean:message key="finalDegreeWorkProposal.setProposalPeriod.start"/></th>
					<td>
						<fr:edit name="executionDegree" slot="scheduling.startOfCandidacyPeriodDateYearMonthDay" validator="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
						
					</td>
					<td>
						<fr:edit name="executionDegree" slot="scheduling.startOfCandidacyPeriodTimeHourMinuteSecond"/>
						
					</td>
				</tr>
				<tr>
					<th><bean:message key="finalDegreeWorkProposal.setProposalPeriod.end"/></th>
					<td>
						<fr:edit name="executionDegree" slot="scheduling.endOfCandidacyPeriodDateYearMonthDay" validator="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
						
					</td>
					<td>
						<fr:edit name="executionDegree" slot="scheduling.endOfCandidacyPeriodTimeHourMinuteSecond"/>
						
					</td>
				</tr>

			</table> 
			<p><input value="Submeter" type="submit"></p>
			</fr:form>
</div>