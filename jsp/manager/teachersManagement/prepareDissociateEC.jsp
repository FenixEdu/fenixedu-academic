<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<h2><bean:message key="link.manager.teachersManagement.removeECAssociation" /></h2>
<span class="error"><html:errors /></span>
<html:form action="/dissociateProfShipsAndRespFor">
	<input type="hidden" name="method" value="prepareDissociateECShowProfShipsAndRespFor"/>
	<input type="hidden" name="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.manager.teachersManagement.teacherNumber"/>
	</p>
	<bean:message key="label.teacher.number"/>:&nbsp;<html:text property="teacherNumber"	/>
	<p>
		<html:submit styleClass="inputbutton"><bean:message key="button.submit"/>                    		         	
		</html:submit> 
		<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  
	</p>
</html:form>