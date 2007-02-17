<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.balances" /></h2>

<logic:present name="yearMonth">
	<div class="mvert1 invisible"><fr:form action="/viewAssiduousness.do?method=showBalances">
		<fr:edit id="yearMonth" name="yearMonth" schema="choose.date">
			<fr:layout>
			</fr:layout>
		</fr:edit>
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			styleClass="invisible">
			<bean:message key="button.submit" />
		</html:submit></p>
	</fr:form></div>

	<span class="error0 mtop0"><html:messages id="message" message="true" property="message">
		<bean:write name="message" />
		<br />
	</html:messages></span>


	<logic:present name="employeeWorkSheetList">
		<fr:view name="employeeWorkSheetList" schema="show.employeeWorkSheetMonthBalance" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder tdright" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:present>
	
</logic:present>