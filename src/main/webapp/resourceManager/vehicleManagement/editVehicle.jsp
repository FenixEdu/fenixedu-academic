<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
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

	<%-- Create Vehicle --%>

	<logic:empty name="vehicle">
		<logic:notEmpty name="vehicleBean">
					
			<h2><bean:message key="label.create.vehicle" bundle="RESOURCE_MANAGER_RESOURCES"/></h2>		
			
			<ul class="mtop2 list5">
				<li>
					<html:link page="/vehicleManagement.do?method=prepareVehicleManage">
						<bean:message key="label.back" bundle="RESOURCE_MANAGER_RESOURCES"/>
					</html:link>
				</li>
			</ul>
										
			<fr:edit id="createVehicleBeanID" name="vehicleBean" action="/vehicleManagement.do?method=createVehicle" schema="CreateVehicleSchema">
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle4 thlight"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>			        
				</fr:layout>
				<fr:destination name="invalid" path="/vehicleManagement.do?method=prepareCreateVehicle"/>	
				<fr:destination name="cancel" path="/vehicleManagement.do?method=prepareVehicleManage"/>						
			</fr:edit>
						
		</logic:notEmpty>
	</logic:empty>

	<%-- Edit Material --%>
		
	<logic:notEmpty name="vehicle">

		<h2><bean:message key="label.edit.vehicle" bundle="RESOURCE_MANAGER_RESOURCES"/></h2>
		
		<fr:edit id="vehicleEditID" name="vehicle" schema="SeeVehicleDetails" action="/vehicleManagement.do?method=listVehicles">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight"/>	
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>		        
			</fr:layout>
			<fr:destination name="cancel" path="/vehicleManagement.do?method=prepareVehicleManage"/>			
		</fr:edit>
	
	</logic:notEmpty>

</logic:present>