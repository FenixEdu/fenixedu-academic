<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></em>
<h2><bean:message key="add.functionality" bundle="CONTENT_RESOURCES"/></h2>

<bean:define id="cid" name="container" property="externalId"/>

<p>
<fr:view name="rootModule">
	<fr:layout name="content-tree">

	    	<fr:property name="expandable" value="true"/>
			<fr:property name="eachLayout" value="values"/>
			
		    <fr:property name="schemaFor(Module)" value="functionalities.module.tree"/>
	        <fr:property name="childrenFor(Module)" value="childrenAsContent"/>
	        <fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>	   			

			<fr:destination name="functionality.view" path="<%= "/contentManagement.do?method=addFunctionality&contentId=${externalId}&contentParentId=" + cid%>"/>
			<fr:destination name="module.view" path="<%= "/contentManagement.do?method=addModule&contentId=${externalId}&contentParentId=" + cid%>"/>
	</fr:layout>
</fr:view>
</p>