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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="unit" name="site" property="unit" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<jsp:include page="/commons/renderers/treeRendererHeader.jsp" />

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<div class="infoop2 mbottom2">
    <p class="mvert0">
    	<bean:message key="message.site.manage.person.functions" bundle="SITE_RESOURCES"/>
   	</p>
</div>

<fr:view name="personBean" schema="site.functions.view.PersonFunctionsBean">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright"/>
        <fr:property name="columnClasses" value="width12em,,"/>
	</fr:layout>
</fr:view>

<bean:define id="personId" name="personBean" property="person.externalId"/>

<div>
	<fr:view name="personBean" layout="person-unit-functions-tree">
	    <fr:layout>
	        <fr:property name="treeId" value="<%= "person" + personId + "FunctionsInUnit" + unit.getExternalId() %>"/>
	
	        <fr:property name="eachLayout" value="values"/>
	
	        <fr:property name="schemaFor(Unit)" value="site.functions.tree.unit"/>
	        <fr:property name="schemaFor(Function)" value="site.functions.tree.function"/>
	        <fr:property name="layoutFor(PersonFunction)" value="values-dash"/>
	        <fr:property name="schemaFor(PersonFunction)" value="site.functions.tree.personFunction"/>
			
	        <fr:property name="addPersonFunctionLink">
	        	<html:link page="<%= String.format("%s?method=addPersonFunction&amp;%s&amp;personID=%s&amp;functionID=${externalId}", actionName, context, personId) %>">
	        		<bean:message key="link.site.manage.person.functions.add" bundle="SITE_RESOURCES"/>
	        	</html:link>
			</fr:property>
	        <fr:property name="editPersonFunctionLink">
	        	<html:link page="<%= String.format("%s?method=editPersonFunction&amp;%s&amp;personFunctionID=${externalId}", actionName, context) %>">
	        		<bean:message key="link.site.manage.person.functions.edit" bundle="SITE_RESOURCES"/>
	        	</html:link>
			</fr:property>
	        <fr:property name="deletePersonFunctionLink">
	        	<html:link page="<%= String.format("%s?method=removePersonFunction&amp;%s&amp;personID=%s&amp;personFunctionID=${externalId}", actionName, context, personId) %>">
	        		<bean:message key="link.site.manage.person.functions.remove" bundle="SITE_RESOURCES"/>
	        	</html:link>
			</fr:property>
	    </fr:layout>
	</fr:view>
</div>
