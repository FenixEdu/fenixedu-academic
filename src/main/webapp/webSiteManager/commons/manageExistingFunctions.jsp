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
    	<bean:message key="message.site.manage.existingFunctions" bundle="SITE_RESOURCES"/>
   	</p>
</div>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>

<fr:view name="tree" layout="unit-functions-tree">
    <fr:layout>
        <fr:property name="treeId" value="<%= "personFunctions" + unit.getExternalId() %>"/>

        <fr:property name="eachLayout" value="values"/>

        <fr:property name="schemaFor(Unit)" value="site.functions.tree.unit"/>
        <fr:property name="schemaFor(Function)" value="site.functions.tree.function"/>
		
        <fr:property name="addFunctionLink">
        	<html:link page="<%= String.format("%s?method=addFunction&amp;%s&amp;unitID=${externalId}", actionName, context) %>">
        		<bean:message key="link.site.manage.existingFunctions.addFunction" bundle="SITE_RESOURCES"/>
        	</html:link>
		</fr:property>
        <fr:property name="orderFunctionsLink">
        	<html:link page="<%= String.format("%s?method=organizeFunctions&amp;%s&amp;unitID=${externalId}", actionName, context) %>">
        		<bean:message key="link.site.manage.existingFunctions.organizeFunctions" bundle="SITE_RESOURCES"/>
        	</html:link>
		</fr:property>
        <fr:property name="editFunctionLink">
        	<html:link page="<%= String.format("%s?method=prepareEditFunction&amp;%s&amp;functionID=${externalId}", actionName, context) %>">
        		<bean:message key="link.site.manage.existingFunctions.editFunction" bundle="SITE_RESOURCES"/>
        	</html:link>
        </fr:property>
        <fr:property name="deleteFunctionLink">
        	<html:link page="<%= String.format("%s?method=deleteFunction&amp;%s&amp;functionID=${externalId}", actionName, context) %>">
        		<bean:message key="link.site.manage.existingFunctions.deleteFunction" bundle="SITE_RESOURCES"/>
        	</html:link>
        </fr:property>
    </fr:layout>
</fr:view>
