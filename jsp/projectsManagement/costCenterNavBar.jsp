<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<br />
<br />
<logic:present name="infoCostCenterList">
	<p><strong><bean:message key="title.costCenter" /></strong></p>
	<ul>
		<logic:iterate id="costCenter" name="infoCostCenterList">
			<bean:define id="code" name="costCenter" property="code"/>
			<li><html:link page="<%="/index.do?costCenter=" + code%>">
				<bean:write name="costCenter" property="description" />
			</html:link></li>
		</logic:iterate>
	</ul>
	<br />
	<br />
</logic:present>
