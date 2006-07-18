<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<br />
<logic:present name="infoCostCenterList">
	<ul>
		<li class="navheader"><bean:message key="title.costCenter" /></li>
		<logic:iterate id="costCenter" name="infoCostCenterList">
			<bean:define id="code" name="costCenter" property="code"/>
			<li><html:link page="<%="/index.do?costCenter=" + code%>">
				<bean:write name="costCenter" property="description" />
			</html:link></li>
		</logic:iterate>
	</ul>
	<br />
</logic:present>
