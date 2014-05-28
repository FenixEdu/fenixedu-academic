<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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