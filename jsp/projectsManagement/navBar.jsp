<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<br />
<br />
<logic:present name="userView" name="<%= SessionConstants.U_VIEW %>" scope="session">
	<%
        ServidorAplicacao.IUserView userView = (ServidorAplicacao.IUserView) session.getAttribute(SessionConstants.U_VIEW);
        if (userView.hasRoleType(net.sourceforge.fenixedu.util.RoleType.PROJECTS_MANAGER)) {
        %>
	<p><strong><bean:message key="label.listByProject" /></strong></p>
	<ul>
		<li><html:link page="/projectReport.do?method=prepareShowReport&amp;reportType=expensesReport">
			<bean:message key="link.expenses" />
		</html:link></li>
		<li><html:link page="/projectReport.do?method=prepareShowReport&amp;reportType=revenueReport">
			<bean:message key="link.revenue" />
		</html:link></li>
		<li><html:link page="/projectReport.do?method=prepareShowReport&amp;reportType=cabimentosReport">
			<bean:message key="label.cabimentos" />
		</html:link></li>
		<li><html:link page="/projectReport.do?method=prepareShowReport&amp;reportType=adiantamentosReport">
			<bean:message key="label.adiantamentos" />
		</html:link></li>
	</ul>
	<br />
	<br />
	<p><strong><bean:message key="label.listByCoordinator" /></strong></p>
	<ul>
		<li><html:link page="/projectReport.do?method=getReport&amp;reportType=summaryReport">
			<bean:message key="link.summary" />
		</html:link></li>
	</ul>

	<br />
	<br />
	<p><strong><bean:message key="label.help" /></strong></p>
	<ul>
		<li><html:link page="/projectReport.do?method=showHelp&amp;helpPage=listHelp">
			<bean:message key="link.listHelp" />
		</html:link></li>
		<li><html:link page="/projectReport.do?method=showHelp&amp;helpPage=explorationUnitsRubric">
			<bean:message key="link.explorationUnitsRubric" />
		</html:link></li>
		<li><html:link page="/projectReport.do?method=showHelp&amp;helpPage=expensesRubric">
			<bean:message key="link.expensesRubric" />
		</html:link></li>
		<li><html:link page="/projectReport.do?method=showHelp&amp;helpPage=revenueRubric">
			<bean:message key="link.revenueRubric" />
		</html:link></li>
		<li><html:link page="/projectReport.do?method=showHelp&amp;helpPage=projectTypesRubric">
			<bean:message key="link.projectTypesRubric" />
		</html:link></li>
	</ul>
	<br />
	<br />
	<p><strong><bean:message key="title.accessDelegation" /></strong></p>
	<ul>
		<li><html:link page="/projectAccess.do?method=choosePerson">
			<bean:message key="link.delegateAccess" />
		</html:link></li>
		<li><html:link page="/projectAccess.do?method=showProjectsAccesses">
			<bean:message key="link.showAccesses" />
		</html:link></li>
	</ul>
	<%}%>
	<br />
	<br />
</logic:present>
