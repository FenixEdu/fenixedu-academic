<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:form action="/manageGrantOwner">

	<logic:messagesPresent>
    <span class="error">
    	<html:errors/>
    </span>
    </logic:messagesPresent>
	
	<logic:present name="infoGrantOwner">
		Nome: <bean:write name="infoGrantOwner" property="personInfo.nome"/>
	</logic:present>

	<logic:present name="duh">
		Nome: <bean:write name="duh"/>
	</logic:present>

</html:form>
