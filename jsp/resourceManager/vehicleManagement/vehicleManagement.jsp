<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<html:xhtml/>

<em><bean:message bundle="RESOURCE_MANAGER_RESOURCES" key="title.resourceManager.management"/></em>
<h2><bean:message key="label.vehicle.management" bundle="RESOURCE_MANAGER_RESOURCES"/></h2>

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
		
	<ul class="mvert2 list5">
		<li>
			<html:link page="/vehicleManagement.do?method=prepareCreateVehicle">
				<bean:message key="label.create.new.vehicle" bundle="RESOURCE_MANAGER_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<logic:notEmpty name="vehicleBean">	
		<span class="mtop1"><strong><bean:message key="label.list.vehicle.numberPlate" bundle="RESOURCE_MANAGER_RESOURCES"/></strong></span>
		<fr:form action="/vehicleManagement.do?method=listVehicles">
			<fr:edit id="vehicleBeanID" name="vehicleBean" schema="SearchVehicleByNumberPlate">
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle1"/>		        
				</fr:layout>			
			</fr:edit>
			<html:submit><bean:message key="label.find" bundle="RESOURCE_MANAGER_RESOURCES"/></html:submit>
		</fr:form>
	</logic:notEmpty>
	
	<logic:notEmpty name="vehicles">	
		
		<p class="mtop2"><strong><bean:message key="label.found.vehicle" bundle="RESOURCE_MANAGER_RESOURCES"/></strong></p>
	
		<bean:message key="label.page" bundle="RESOURCE_MANAGER_RESOURCES"/>:
		<cp:collectionPages url="/vehicleManagement/vehicleManagement.do?method=listVehicles" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>			
							
		<fr:view name="vehicles" schema="SeeVehicleDetails">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1 tdcenter"/>
					
				<fr:property name="link(edit)" value="<%="/vehicleManagement.do?method=prepareEditVehicle"%>"/>
            	<fr:property name="param(edit)" value="idInternal/vehicleID"/>
		        <fr:property name="key(edit)" value="link.edit"/>
	            <fr:property name="bundle(edit)" value="RESOURCE_MANAGER_RESOURCES"/>
	            <fr:property name="order(edit)" value="0"/>
	            
	    		<fr:property name="link(delete)" value="<%="/vehicleManagement.do?method=deleteVehicle"%>"/>
            	<fr:property name="param(delete)" value="idInternal/vehicleID"/>
		        <fr:property name="key(delete)" value="link.delete"/>
	            <fr:property name="bundle(delete)" value="RESOURCE_MANAGER_RESOURCES"/>
	            <fr:property name="order(delete)" value="1"/>
	            										        
			</fr:layout>					
		</fr:view>
	</logic:notEmpty>	
	
</logic:present>	