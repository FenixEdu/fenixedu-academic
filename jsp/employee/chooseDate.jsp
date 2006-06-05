<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<bean:define id="nextAction" value="showWorkSheet" />
<logic:present name="action" scope="request">
	<bean:define id="action" name="action" />
	<bean:define id="nextAction" value="<%=action.toString()%>" />
</logic:present>

<h2><bean:message key="<%="title."+nextAction.toString()%>" /></h2>
<br />
<br />


<logic:present name="yearMonth">
	<fr:edit name="yearMonth" schema="choose.date"
		action="<%="/assiduousnessRecords.do?method=" +nextAction.toString()%>">
	</fr:edit>
</logic:present>
