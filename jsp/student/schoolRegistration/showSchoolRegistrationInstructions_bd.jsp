<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/changePassword?method=start">
<html:hidden property="page" value="1"/>
<p align="center"><span class="error"><html:errors/></span></p>
<p align="center">
	bla bla bla<br/>
	<html:checkbox  property="instructionsAccepted" /> Li e percebi o bla bla bla<br/>
	<html:submit value="Continuar" styleClass="inputbutton"/>
</p>
</html:form>
