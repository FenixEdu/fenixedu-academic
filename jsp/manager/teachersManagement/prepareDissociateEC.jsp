<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement.removeECAssociation" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/dissociateProfShipsAndRespFor">
	<input alt="input.method" type="hidden" name="method" value="prepareDissociateECShowProfShipsAndRespFor"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<p class="infoop">
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.teachersManagement.teacherNumber"/>
	</p>
	<bean:message bundle="MANAGER_RESOURCES" key="label.teacher.number"/>:&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber"	/>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.submit"/>                    		         	
		</html:submit> 
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
		</html:reset>  
	</p>
</html:form>