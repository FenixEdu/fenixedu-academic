<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="pid" name="portal" property="idInternal"/>

<p>
<fr:view name="rootModule">
	<fr:layout name="content-tree">

	    	<fr:property name="expandable" value="true"/>
			<fr:property name="eachLayout" value="values"/>
			
		    <fr:property name="schemaFor(Module)" value="content.in.tree"/>
	        <fr:property name="childrenFor(Module)" value="childrenAsContent"/>
	        <fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>	   			

			<fr:destination name="functionality.view" path="<%= "/portalManagement.do?method=addToPool&elementId=${idInternal}&pid=" + pid%>"/>
	</fr:layout>
</fr:view>
</p>
