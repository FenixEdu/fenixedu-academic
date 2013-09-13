<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<bean:define id="backendInstance" name="backendInstance" type="net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance"/>
<bean:define id="backendInstanceUrl" type="java.lang.String">&amp;backendInstance=<%= backendInstance.name() %></bean:define>

<bean:define id="code" value="" />
<logic:present name="infoCostCenter" scope="request">
	<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
	<bean:define id="code" value="<%="&amp;costCenter="+cc.toString()%>" />
</logic:present>
<logic:present name="it" scope="request">
	<logic:equal name="it" value="true">
		<bean:define id="code" value="<%=code+"&amp;backendInstance=IST"%>" />
	</logic:equal>
</logic:present>
<ul>
	<li class="navheader"><bean:message key="label.listByProject" /></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=expensesReport"+code+backendInstanceUrl%>">
		<bean:message key="link.expenses" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=completeExpensesReport"+code+backendInstanceUrl%>">
		<bean:message key="link.completeExpenses" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=revenueReport"+code+backendInstanceUrl%>">
		<bean:message key="link.revenue" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=cabimentosReport"+code+backendInstanceUrl%>">
		<bean:message key="label.cabimentos" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=adiantamentosReport"+code+backendInstanceUrl%>">
		<bean:message key="label.adiantamentos" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=projectBudgetaryBalanceReport"+code+backendInstanceUrl%>">
		<bean:message key="link.budgetaryBalance" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=openingProjectFileReport"+code+backendInstanceUrl%>">
		<bean:message key="link.openingProjectFile" />
	</html:link></li>

<logic:present name="infoCostCenter" scope="request">
	<li class="navheader"><bean:message key="label.listByCostCenter" /></li>
</logic:present>
<logic:notPresent name="infoCostCenter" scope="request">
	<li class="navheader"><bean:message key="label.listByCoordinator" /></li>
</logic:notPresent>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=summaryReport"+code+backendInstanceUrl%>">
		<bean:message key="link.summary" />
	</html:link></li>
	<%--
		<li><html:link
			page="/projectReport.do?method=getReport&amp;reportType=coordinatorBudgetaryBalanceReport">
			<bean:message key="link.budgetaryBalance" />
		</html:link></li>
		--%>

<logic:present name="infoCostCenter" scope="request">
	<li class="navheader"><bean:message key="label.overheadsList" /></li>
		<li><html:link page="<%="/overheadReport.do?method=getReport&amp;reportType=generatedOverheadsReport"+code+backendInstanceUrl%>">
			<bean:message key="link.generatedOverheads" />
		</html:link></li>
		<li><html:link page="<%="/overheadReport.do?method=getReport&amp;reportType=transferedOverheadsReport"+code+backendInstanceUrl%>">
			<bean:message key="link.transferedOverheads" />
		</html:link></li>
		<li><html:link page="<%="/overheadReport.do?method=getReport&amp;reportType=overheadsSummaryReport"+code+backendInstanceUrl%>">
			<bean:message key="link.summary" />
		</html:link></li>
</logic:present>


	<li class="navheader"><bean:message key="label.help" /></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=listHelp"+code+backendInstanceUrl%>">
		<bean:message key="link.listHelp" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=explorationUnitsRubric"+code+backendInstanceUrl%>">
		<bean:message key="link.explorationUnitsRubric" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=expensesRubric"+code+backendInstanceUrl%>">
		<bean:message key="link.expensesRubric" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=revenueRubric"+code+backendInstanceUrl%>">
		<bean:message key="link.revenueRubric" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=projectTypesRubric"+code+backendInstanceUrl%>">
		<bean:message key="link.projectTypesRubric" />
	</html:link></li>
	<logic:present name="infoCostCenter" scope="request">
		<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=overheadTypesRubric"+code+backendInstanceUrl%>">
			<bean:message key="link.overheadTypesRubric" />
		</html:link></li>
	</logic:present>

	<li class="navheader"><bean:message key="title.accessDelegation" /></li>
	<li><html:link page="<%="/projectAccess.do?method=choosePerson"+code+backendInstanceUrl%>">
		<bean:message key="link.delegateAccess" />
	</html:link></li>
	<li><html:link page="<%="/projectAccess.do?method=showProjectsAccesses"+code+backendInstanceUrl%>">
		<bean:message key="link.showAccesses" />
	</html:link></li>
</ul>