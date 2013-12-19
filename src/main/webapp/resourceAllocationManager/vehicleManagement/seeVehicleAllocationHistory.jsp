<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SOP_RESOURCES" key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="label.vehicle.allocation.history" bundle="SOP_RESOURCES"/></h2>

<logic:present role="role(RESOURCE_ALLOCATION_MANAGER)">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	
	<ul class="mtop15">
		<li>
			<html:link page="/vehicleManagement.do?method=prepare">		
				<bean:message bundle="SOP_RESOURCES" key="label.back"/>
			</html:link>
		</li>		
	</ul>
	
	<fr:form action="/vehicleManagement.do?method=seeVehicleAllocationHistory">
		<fr:edit id="vehicleAllocationHistoryWithYearAndMonth" name="vehicleAllocationHistoryBean" schema="VehicleAllocationHistory">
			<fr:destination name="postback" path="/vehicleManagement.do?method=seeVehicleAllocationHistory"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />			
			</fr:layout>		
		</fr:edit>
	</fr:form>
	
	<p class="mtop15">
		<logic:notEmpty name="pastVehicleAllocations">
			<fr:view schema="SeeVehicleAllocation" name="pastVehicleAllocations">	
				<fr:layout name="tabular">			      			
		   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>	   			
					<fr:property name="link(view)" value="/vehicleManagement.do?method=seeVehicleAllocation"/>
		            <fr:property name="param(view)" value="externalId/allocationID"/>
			        <fr:property name="key(view)" value="link.see"/>
		            <fr:property name="bundle(view)" value="SOP_RESOURCES"/>
		            <fr:property name="order(view)" value="0"/>	           	 	   				  
		    	</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</p>
	
	<logic:empty name="pastVehicleAllocations">
		<em><bean:message key="label.empty.past.vehicle.allocations" bundle="SOP_RESOURCES"/></em>	
	</logic:empty>		
		
</logic:present>	