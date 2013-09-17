<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement.removeECAssociation" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/dissociateProfShipsAndRespFor">
	<input alt="input.method" type="hidden" name="method" value="prepareDissociateECShowProfShipsAndRespFor"/>
	<input alt="input.page" type="hidden" name="page" value="1"/>
	<p class="infoop">
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.teachersManagement.teacherId"/>
	</p>
	<bean:message bundle="MANAGER_RESOURCES" key="label.person.ist.id"/>:&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="teacherId"	/>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.submit"/>                    		         	
		</html:submit> 
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
		</html:reset>  
	</p>
</html:form>