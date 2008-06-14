<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></em>
<h2><bean:message key="add.functionality" bundle="CONTENT_RESOURCES"/></h2>

<bean:define id="cid" name="container" property="idInternal"/>

<p>
<fr:view name="rootModule">
	<fr:layout name="content-tree">

	    	<fr:property name="expandable" value="true"/>
			<fr:property name="eachLayout" value="values"/>
			
		    <fr:property name="schemaFor(Module)" value="functionalities.module.tree"/>
	        <fr:property name="childrenFor(Module)" value="childrenAsContent"/>
	        <fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>	   			

			<fr:destination name="functionality.view" path="<%= "/contentManagement.do?method=addFunctionality&contentId=${idInternal}&contentParentId=" + cid%>"/>
			<fr:destination name="module.view" path="<%= "/contentManagement.do?method=addModule&contentId=${idInternal}&contentParentId=" + cid%>"/>
	</fr:layout>
</fr:view>
</p>