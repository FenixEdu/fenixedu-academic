<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><html:errors/></span>
<h2><bean:message key="title.student.print.registrationDeclaration"/></h2>
<html:form action="/generateDeclaration.do?method=generate" target="_blank">
<html:hidden property="page" value="1"/>
<br>
<bean:message key="label.choose.student"/> <html:text size="8" maxlength="8" property="studentNumber"/>
<br/><br/>
<html:submit styleClass="inputbutton">Continuar</html:submit>
</html:form>