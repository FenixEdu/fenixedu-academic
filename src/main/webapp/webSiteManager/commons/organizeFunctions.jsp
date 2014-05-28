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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="targetId" name="target" property="externalId"/>

<jsp:include page="/commons/renderers/treeRendererHeader.jsp" />

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<div>
	<strong>
		<bean:message key="title.site.manage.functions.organizeFunctions" bundle="SITE_RESOURCES"/>
		<bean:message key="label.in" bundle="DEFAULT"/>
		<fr:view name="target" property="nameI18n"/>
	</strong>
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

<div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
	<fr:view name="functions">
	   <fr:layout name="unit-functions-tree">
	       <fr:property name="treeId" value="unitFunctionsOrder"/>
	       <fr:property name="fieldId" value="functions-order"/>
	       
	       <fr:property name="eachLayout" value="values"/>
	       <fr:property name="schemaFor(Function)" value="site.functions.tree.function"/>
           <fr:property name="noChildrenFor(Function)" value="true"/>
	   </fr:layout>
	</fr:view>

	<fr:form action="<%= String.format("%s?method=changeFunctionsOrder&amp;%s&amp;unitID=%s", actionName, context, targetId) %>">
	    <input alt="input.functionsOrder" id="functions-order" type="hidden" name="functionsOrder" value=""/>
	</fr:form>
	
	<p class="mtop15">
	    <fr:form action="<%= String.format("%s?method=manageExistingFunctions&amp;%s", actionName, context) %>">
		     <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('unitFunctionsOrder');" %>">
		         <bean:message key="button.site.functions.order.save" bundle="SITE_RESOURCES"/>
		     </html:button>
		     <html:submit>
		         <bean:message key="button.site.functions.order.reset" bundle="SITE_RESOURCES"/>
		     </html:submit>
	 	</fr:form>
	 </p>
</div>

<p style="color: #888;">
	<em><bean:message key="message.site.functions.reorder.tip" bundle="SITE_RESOURCES"/></em>
</p>
