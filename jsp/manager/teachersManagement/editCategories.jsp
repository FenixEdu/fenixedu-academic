<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement.teacherCategoryManagement"/></em>
	
<h2><bean:message bundle="MANAGER_RESOURCES" key="edit.categories"/></h2>
 	
<logic:notEmpty name="categories">
	<logic:iterate name="categories" id="category">
		<bean:define id="code" name="category" property="idInternal"/>
		<fr:edit id="<%= "category." + code %>" name="category" schema="teacherCategory.edit">
		    <fr:layout>
	    	    <fr:property name="classes" value="style1"/>
	        	<fr:property name="columnClasses" value="listClasses"/>
		    </fr:layout>
		</fr:edit>
		<br/>
	</logic:iterate>
</logic:notEmpty>
