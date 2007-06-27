<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean"%>
<html:xhtml/>

<em><bean:message bundle="RESOURCE_MANAGER_RESOURCES" key="title.resourceManager.management"/></em>

<logic:present role="RESOURCE_MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>

	<fr:hasMessages for="createMaterialBeanID" type="conversion">
		<p><span class="error0">			
			<fr:message for="createMaterialBeanID" show="message"/>
		</span></p>
	</fr:hasMessages>	
	
	<fr:hasMessages for="materialID" type="conversion">
		<p><span class="error0">			
			<fr:message for="materialID" show="message"/>
		</span></p>
	</fr:hasMessages>	

	<%-- Create Material --%>

	<logic:empty name="material">
		<logic:notEmpty name="materialBean">
					
			<h2><bean:message key="label.create.material" bundle="RESOURCE_MANAGER_RESOURCES"/></h2>		
			
			<ul class="mtop2 list5">
				<li>
					<html:link page="/materialManagement.do?method=prepareMaterialManage">
						<bean:message key="label.back" bundle="RESOURCE_MANAGER_RESOURCES"/>
					</html:link>
				</li>
			</ul>
			
			<fr:form action="/materialManagement.do?method=prepareCreateMaterial">
				<fr:edit id="prepareCreateMaterialBeanID" name="materialBean" schema="PrepareCreateMaterial">
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle1"/>		        
					</fr:layout>
					<fr:destination name="postBack" path="/materialManagement.do?method=prepareCreateMaterial"/>
				</fr:edit>
			</fr:form>
						
			<logic:notEmpty name="materialBean" property="materialType">						
				<bean:define id="schemaName">Create<bean:write name="materialBean" property="materialType.materialClass.simpleName"/>Material</bean:define>				
				<fr:edit id="createMaterialBeanID" name="materialBean" action="/materialManagement.do?method=createMaterial" schema="<%= schemaName %>">
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle1"/>		        
					</fr:layout>
					<fr:destination name="invalid" path="/materialManagement.do?method=prepareCreateMaterial"/>	
					<fr:destination name="cancel" path="/materialManagement.do?method=listMaterial"/>						
				</fr:edit>
			</logic:notEmpty>
					
		</logic:notEmpty>
	</logic:empty>

	<%-- Edit Material --%>
		
	<logic:notEmpty name="material">

		<h2><bean:message key="label.edit.material" bundle="RESOURCE_MANAGER_RESOURCES"/></h2>
			
		<bean:define id="material" name="material" type="net.sourceforge.fenixedu.domain.material.Material"/>
		<% MaterialBean.MaterialType materialType = MaterialBean.MaterialType.getMaterialTypeByMaterialClass(material.getClass());	%>
				
		<bean:define id="schemaName">Edit<bean:write name="material" property="class.simpleName"/>Material</bean:define>					
		<fr:edit id="materialEditID" name="material" schema="<%= schemaName %>" action="/materialManagement.do?method=listMaterial">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>		        
			</fr:layout>
			<fr:destination name="cancel" path="/materialManagement.do?method=listMaterial"/>			
		</fr:edit>
	
	</logic:notEmpty>

</logic:present>