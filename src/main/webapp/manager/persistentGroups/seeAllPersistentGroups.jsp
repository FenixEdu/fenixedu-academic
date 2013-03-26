<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.access.control.persistent.groups.management" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	

	<p class="mtop15 mbottom15">
		<html:link page="/accessControlPersistentGroupsManagement.do?method=prepareCreateNewGroup">
			<bean:message key="label.create.new.persistent.group" bundle="MANAGER_RESOURCES"/>			
		</html:link>
	</p>
	
	<logic:notEmpty name="persistentGroups">
		
		<fr:view schema="ViewPersistentGroup" name="persistentGroups" >
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
	   			<fr:property name="columnClasses" value="aleft,,,,"/>
	   			
	   			<fr:property name="link(edit)" value="<%= "/accessControlPersistentGroupsManagement.do?method=prepareEditPersistentGroup" %>"/>
	            <fr:property name="param(edit)" value="idInternal/persistentGroupID"/>
		        <fr:property name="key(edit)" value="link.edit"/>
	            <fr:property name="bundle(edit)" value="MANAGER_RESOURCES"/>
	            <fr:property name="order(edit)" value="0"/>
	            
	            <fr:property name="link(delete)" value="<%= "/accessControlPersistentGroupsManagement.do?method=deletePersistentGroup" %>"/>
	            <fr:property name="param(delete)" value="idInternal/persistentGroupID"/>
		        <fr:property name="key(delete)" value="link.delete"/>
	            <fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
	            <fr:property name="order(delete)" value="0"/>                                           	           
	    	</fr:layout>
		</fr:view>
	
	</logic:notEmpty>
	
</logic:present>