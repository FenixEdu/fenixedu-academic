<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:present name="<%= SessionConstants.LIST_ADVISORY %>" scope="request">
	<bean:size id="numberAdvisories" name="<%= SessionConstants.LIST_ADVISORY %>" scope="request"/>
	<bean:message key="label.have"/>
	<bean:write name="numberAdvisories"/>
	<bean:message key="label.advisories"/>
	<br />
	<logic:iterate id="advisory" name="<%= SessionConstants.LIST_ADVISORY %>" scope="request">
		<bean:define id="activeAdvisory" name="activeAdvisory"/>
		<logic:equal name="advisory" property="idInternal" value="<%= activeAdvisory.toString() %>">
			<bean:message key="label.from"/> <bean:write name="advisory" property="sender"/><br />
			<bean:message key="label.sendDate"/> <date:format pattern="yyyy.MM.dd"><bean:write name="advisory" property="created.time"/></date:format><br />
			<bean:message key="label.subject"/> <bean:write name="advisory" property="subject"/><br />
			<bean:write name="advisory" property="message"/><br />
		</logic:equal>
		<logic:notEqual name="advisory" property="idInternal" value="<%= activeAdvisory.toString() %>">
			<html:link page="/index.do" paramId="activeAdvisory" paramName="advisory" paramProperty="idInternal">
				<bean:write name="advisory" property="sender"/> |
				<bean:message key="label.subject"/> <bean:write name="advisory" property="subject"/> 
				<bean:message key="label.sendDate"/> <date:format pattern="yyyy.MM.dd"><bean:write name="advisory" property="created.time"/></date:format><br />
			</html:link>
		</logic:notEqual>
	</logic:iterate>
	<br />
</logic:present>
Bem vindo à área pessoal <br>
Aqui poderá visualizar e alterar a sua informação pessoal.<br>
Poderá também proceder à alteração da sua password.<br>
