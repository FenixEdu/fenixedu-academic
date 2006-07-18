<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<span class="error"><html:errors/></span>
<h2><bean:message key="title.student.print.registrationDeclaration"/></h2>
<html:form action="/generateDeclaration.do?method=generate" target="_blank">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<br>
<bean:message key="label.choose.student"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" size="8" maxlength="8" property="studentNumber"/>
<br/><br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">Continuar</html:submit>
</html:form>