<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>

<html:form action="/rulesManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertNewRule"/>

	<p><h2><bean:message bundle="MANAGER_RESOURCES" key="title.rules.management"/></h2></p>		
	<br/>
	<p><h3><bean:message bundle="MANAGER_RESOURCES" key="link.new.rule"/></h3></p>	
	<br/>
	
	<fr:create id="create" type="org.fenixedu.academic.domain.organizationalStructure.ConnectionRule" schema="connectionRule.bean">
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:create>
	
	<br/>
	<html:link module="/manager" page="/rulesManagement.do?method=listRules">
		<bean:message bundle="MANAGER_RESOURCES" key="label.return" />
	</html:link>
	
</html:form>