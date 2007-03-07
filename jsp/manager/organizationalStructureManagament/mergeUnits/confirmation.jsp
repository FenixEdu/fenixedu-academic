<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>
<html:xhtml/>

<h2><bean:message key="title.units.merge.confirmation" bundle="MANAGER_RESOURCES"/></h2>

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

	<logic:notEmpty name="fromUnit">	
		<logic:notEmpty name="destinationUnit">
	
			<logic:equal name="official" value="true">
				
				<p class="mtop15 mbottom05"><strong><bean:message key="label.no.official.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="fromUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
				
				<p class="mtop15 mbottom05"><strong><bean:message key="label.official.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="destinationUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
						
				<bean:define id="fromUnitID" name="fromUnit" property="idInternal" />
				<bean:define id="destinationUnitID" name="destinationUnit" property="idInternal" />
				<html:form action="/unitsMerge.do?method=mergeUnits">
					<html:hidden property="fromUnitID" value="<%= fromUnitID.toString() %>"/>
					<html:hidden property="fromUnitID" value="<%= destinationUnitID.toString() %>"/>	
					<html:submit><bean:message key="label.proceed" bundle="MANAGER_RESOURCES"/></html:submit>
				</html:form>
												
			</logic:equal>
			
			<logic:equal name="official" value="false">
				
				<p class="mtop15 mbottom05"><strong><bean:message key="label.no.official.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="fromUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
				
				<p class="mtop15 mbottom05"><strong><bean:message key="label.no.official.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="destinationUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
				
				<bean:define id="mergeUnitsID">/unitsMerge.do?method=mergeUnits&amp;fromUnitID=<bean:write name="fromUnit" property="idInternal"/>&amp;unitID=<bean:write name="destinationUnit" property="idInternal"/></bean:define>		
				<fr:edit id="noOfficialMerge" name="destinationUnit" schema="ChangeNoOfficialName" action="<%= mergeUnitsID %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
					<fr:destination name="cancel" path="/unitsMerge.do?method=chooseUnitToStart"/>
				</fr:edit>
											
			</logic:equal>			
	
		</logic:notEmpty>		
	</logic:notEmpty>

</logic:present>