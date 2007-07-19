<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="targetId" name="target" property="idInternal"/>


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
	<fr:view name="target" property="orderedFunctions">
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
