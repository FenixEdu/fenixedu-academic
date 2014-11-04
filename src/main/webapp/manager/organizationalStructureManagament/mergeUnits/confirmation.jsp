<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/units" prefix="un" %>
<html:xhtml/>

<h2><bean:message key="title.units.merge.confirmation" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="role(MANAGER)">

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
	
			<bean:define id="chooseNewDestinationUnitID">/unitsMerge.do?method=seeChoosedUnit&amp;unitID=<bean:write name="fromUnit" property="externalId"/></bean:define>		
			<ul class="list5 mtop2 mbottom3">
				<li>						
					<html:link page="<%= chooseNewDestinationUnitID %>">
						<bean:message key="label.choose.new.destination.unit" bundle="MANAGER_RESOURCES"/>
					</html:link>		
				</li>
			</ul>	
			
			<logic:equal name="official" value="true">
				
				<p class="mbottom05"><strong><bean:message key="label.no.official.from.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="fromUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
				
				<p style="font-size: 1.5em; margin-left: 5em;" class="mbottom15"><b>+</b></p>
				
				<p class="mtop15 mbottom05"><strong><bean:message key="label.official.detination.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="destinationUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
						
				<bean:define id="fromUnitID" name="fromUnit" property="externalId" />
				<bean:define id="destinationUnitID" name="destinationUnit" property="externalId" />
				<html:form action="/unitsMerge.do">
					<html:hidden property="method" value="mergeUnits"/>
					<html:hidden property="fromUnitID" value="<%= fromUnitID.toString() %>"/>
					<html:hidden property="destinationUnitID" value="<%= destinationUnitID.toString() %>"/>	
					<html:submit><bean:message key="label.proceed" bundle="MANAGER_RESOURCES"/></html:submit>
					<html:cancel onclick="this.form.method.value='chooseUnitToStart';this.form.submit();"><bean:message key="button.cancel" bundle="MANAGER_RESOURCES"/></html:cancel>
				</html:form>
												
			</logic:equal>
			
			<logic:equal name="official" value="false">
				
				<p><strong><bean:message key="label.no.official.from.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="fromUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
				
				<p style="font-size: 1.5em; margin-left: 5em;" class="mbottom15"><b>+</b></p>
				
				<p class="mtop05 mbottom05"><strong><bean:message key="label.no.official.destination.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
				<fr:view name="destinationUnit" schema="ViewUnitInfoToMergeUnits">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thright"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
				</fr:view>
				
				<bean:define id="mergeUnitsID">/unitsMerge.do?method=mergeUnits&amp;fromUnitID=<bean:write name="fromUnit" property="externalId"/>&amp;unitID=<bean:write name="destinationUnit" property="externalId"/></bean:define>		
				<fr:edit id="noOfficialMerge" name="destinationUnit" schema="ChangeNoOfficialName" action="<%= mergeUnitsID %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="mtop3 tstyle3 thlight thright"/>
						<fr:property name="columnClasses" value=",bold"/>
					</fr:layout>
					<fr:destination name="cancel" path="/unitsMerge.do?method=chooseUnitToStart"/>
				</fr:edit>
											
			</logic:equal>			
	
		</logic:notEmpty>		
	</logic:notEmpty>

</logic:present>