<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="pid" name="portal" property="externalId"/>

<p>
<fr:view name="rootModule">
	<fr:layout name="content-tree">

	    	<fr:property name="expandable" value="true"/>
			<fr:property name="eachLayout" value="values"/>
			
		    <fr:property name="schemaFor(Module)" value="content.in.tree"/>
	        <fr:property name="childrenFor(Module)" value="childrenAsContent"/>
	        <fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>	   			

			<fr:destination name="functionality.view" path="<%= "/portalManagement.do?method=addToPool&elementId=${externalId}&pid=" + pid%>"/>
	</fr:layout>
</fr:view>
</p>
