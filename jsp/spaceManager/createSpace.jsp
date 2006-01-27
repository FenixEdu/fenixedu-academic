<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:message bundle="SPACE_RESOURCES" key="link.create.space"/>
<br/>
<br/>
<br/>

<html:form action="/createSpace.do">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="createSpace"/>

	<html:submit styleClass="inputbutton">
		<bean:message bundle="SPACE_RESOURCES" key="label.button.create.space"/>
	</html:submit>
</html:form>