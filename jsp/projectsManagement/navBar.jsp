<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="code" value="" />
<logic:present name="infoCostCenter" scope="request">
	<bean:define id="cc" name="infoCostCenter" property="code" scope="request" />
	<bean:define id="code" value="<%="&amp;costCenter="+cc.toString()%>" />
</logic:present>
<ul>
	<li class="navheader"><bean:message key="label.listByProject" /></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=expensesReport"+code%>">
		<bean:message key="link.expenses" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=completeExpensesReport"+code%>">
		<bean:message key="link.completeExpenses" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=revenueReport"+code%>">
		<bean:message key="link.revenue" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=cabimentosReport"+code%>">
		<bean:message key="label.cabimentos" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=adiantamentosReport"+code%>">
		<bean:message key="label.adiantamentos" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=projectBudgetaryBalanceReport"+code%>">
		<bean:message key="link.budgetaryBalance" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=openingProjectFileReport"+code%>">
		<bean:message key="link.openingProjectFile" />
	</html:link></li>

<logic:present name="infoCostCenter" scope="request">
	<li class="navheader"><bean:message key="label.listByCostCenter" /></li>
</logic:present>
<logic:notPresent name="infoCostCenter" scope="request">
	<li class="navheader"><bean:message key="label.listByCoordinator" /></li>
</logic:notPresent>
	<li><html:link page="<%="/projectReport.do?method=prepareShowReport&amp;reportType=summaryReport"+code%>">
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
		<li><html:link page="<%="/overheadReport.do?method=getReport&amp;reportType=generatedOverheadsReport"+code%>">
			<bean:message key="link.generatedOverheads" />
		</html:link></li>
		<li><html:link page="<%="/overheadReport.do?method=getReport&amp;reportType=transferedOverheadsReport"+code%>">
			<bean:message key="link.transferedOverheads" />
		</html:link></li>
		<li><html:link page="<%="/overheadReport.do?method=getReport&amp;reportType=overheadsSummaryReport"+code%>">
			<bean:message key="link.summary" />
		</html:link></li>
</logic:present>


	<li class="navheader"><bean:message key="label.help" /></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=listHelp"+code%>">
		<bean:message key="link.listHelp" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=explorationUnitsRubric"+code%>">
		<bean:message key="link.explorationUnitsRubric" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=expensesRubric"+code%>">
		<bean:message key="link.expensesRubric" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=revenueRubric"+code%>">
		<bean:message key="link.revenueRubric" />
	</html:link></li>
	<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=projectTypesRubric"+code%>">
		<bean:message key="link.projectTypesRubric" />
	</html:link></li>
	<logic:present name="infoCostCenter" scope="request">
		<li><html:link page="<%="/projectReport.do?method=showHelp&amp;helpPage=overheadTypesRubric"+code%>">
			<bean:message key="link.overheadTypesRubric" />
		</html:link></li>
	</logic:present>

	<li class="navheader"><bean:message key="title.accessDelegation" /></li>
	<li><html:link page="<%="/projectAccess.do?method=choosePerson"+code%>">
		<bean:message key="link.delegateAccess" />
	</html:link></li>
	<li><html:link page="<%="/projectAccess.do?method=showProjectsAccesses"+code%>">
		<bean:message key="link.showAccesses" />
	</html:link></li>
</ul>