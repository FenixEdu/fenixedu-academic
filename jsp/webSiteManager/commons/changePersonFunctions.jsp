<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="unit" name="site" property="unit" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

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

<bean:define id="personId" name="personBean" property="person.idInternal"/>

<div>
	<fr:view name="personBean" layout="person-unit-functions-tree">
	    <fr:layout>
	        <fr:property name="treeId" value="<%= "person" + personId + "FunctionsInUnit" + unit.getIdInternal() %>"/>
	
	        <fr:property name="eachLayout" value="values"/>
	
	        <fr:property name="schemaFor(Unit)" value="site.functions.tree.unit"/>
	        <fr:property name="schemaFor(Function)" value="site.functions.tree.function"/>
	        <fr:property name="layoutFor(PersonFunction)" value="values-dash"/>
	        <fr:property name="schemaFor(PersonFunction)" value="site.functions.tree.personFunction"/>
			
	        <fr:property name="addPersonFunctionLink">
	        	<html:link page="<%= String.format("%s?method=addPersonFunction&amp;%s&amp;personID=%s&amp;functionID=${idInternal}", actionName, context, personId) %>">
	        		<bean:message key="link.site.manage.person.functions.add" bundle="SITE_RESOURCES"/>
	        	</html:link>
			</fr:property>
	        <fr:property name="editPersonFunctionLink">
	        	<html:link page="<%= String.format("%s?method=editPersonFunction&amp;%s&amp;personFunctionID=${idInternal}", actionName, context) %>">
	        		<bean:message key="link.site.manage.person.functions.edit" bundle="SITE_RESOURCES"/>
	        	</html:link>
			</fr:property>
	        <fr:property name="deletePersonFunctionLink">
	        	<html:link page="<%= String.format("%s?method=removePersonFunction&amp;%s&amp;personID=%s&amp;personFunctionID=${idInternal}", actionName, context, personId) %>">
	        		<bean:message key="link.site.manage.person.functions.remove" bundle="SITE_RESOURCES"/>
	        	</html:link>
			</fr:property>
	    </fr:layout>
	</fr:view>
</div>
