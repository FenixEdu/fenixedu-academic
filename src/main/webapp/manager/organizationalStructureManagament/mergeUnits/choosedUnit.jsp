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

<h2><bean:message key="title.units.merge" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="role(MANAGER)">

	<script language="JavaScript">
		function check(e,v)
		{	
			var contextPath = '<%= request.getContextPath() %>';	
			if (e.style.display == "none")
			  {
			  e.style.display = "";
			  v.src = contextPath + '/images/toggle_minus10.gif';
			  }
			else
			  {
			  e.style.display = "none";
			  v.src = contextPath + '/images/toggle_plus10.gif';
			  }
		}
	</script>

	<logic:notEmpty name="externalUnit"> 		
		
		<p class="mtop3 mbottom05"><strong><bean:message key="label.choosed.no.official.unit.to.merge" bundle="MANAGER_RESOURCES"/></strong></p>	
		<fr:view name="externalUnit" schema="ViewUnitInfoToMergeUnits">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright"/>
				<fr:property name="columnClasses" value=",bold"/>
			</fr:layout>
		</fr:view>
		
		<p style="font-size: 1.5em; margin-left: 5em;" class="mbottom15"><b>+</b></p>
		
		<logic:notEmpty name="earthUnit">		
					
			<p class="mtop15 mbottom05"><strong><bean:message key="label.choose.destination.unit.official" bundle="MANAGER_RESOURCES"/></strong></p>	
			<bean:define id="officialURL">/manager/unitsMerge.do?method=mergeWithOfficial&amp;fromUnitID=<bean:write name="externalUnit" property="externalId"/></bean:define>
			<un:tree initialUnit="earthUnit" unitParamName="unitID" path="<%= officialURL %>" state="true"/>				

			<logic:notEmpty name="externalInstitutionUnit">
				<p class="mtop15 mbottom05"><strong><bean:message key="label.choose.destination.unit.not.official" bundle="MANAGER_RESOURCES"/></strong></p>					
				<bean:define id="noOfficialURL">/manager/unitsMerge.do?method=mergeWithNoOfficialUnits&amp;fromUnitID=<bean:write name="externalUnit" property="externalId"/></bean:define>		
				<un:tree initialUnit="externalInstitutionUnit" unitParamName="unitID" path="<%= noOfficialURL %>" state="true"/>
			</logic:notEmpty>

		</logic:notEmpty>		
		
	</logic:notEmpty>
			
</logic:present>
