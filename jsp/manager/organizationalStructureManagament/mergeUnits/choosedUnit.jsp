<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>
<html:xhtml/>

<h2><bean:message key="title.units.merge" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">

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
		
		<p class="mtop15 mbottom05"><strong><bean:message key="label.choosed.no.official.unit.to.merge" bundle="MANAGER_RESOURCES"/></strong></p>	
		<fr:view name="externalUnit" schema="ViewUnitInfoToMergeUnits">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
				<fr:property name="columnClasses" value=",bold"/>
			</fr:layout>
		</fr:view>
		
		<logic:notEmpty name="earthUnit">		
					
			<p class="mtop15 mbottom05"><strong><bean:message key="label.choose.destination.unit.official" bundle="MANAGER_RESOURCES"/></strong></p>	
			<bean:define id="officialURL">/manager/unitsMerge.do?method=mergeWithOfficial&amp;fromUnitID=<bean:write name="externalUnit" property="idInternal"/></bean:define>
			<un:tree initialUnit="earthUnit" unitParamName="unitID" path="<%= officialURL %>" state="true"/>				

			<logic:notEmpty name="externalInstitutionUnit">
				<p class="mtop15 mbottom05"><strong><bean:message key="label.choose.destination.unit.not.official" bundle="MANAGER_RESOURCES"/></strong></p>					
				<bean:define id="noOfficialURL">/manager/unitsMerge.do?method=mergeWithNoOfficialUnits&amp;fromUnitID=<bean:write name="externalUnit" property="idInternal"/></bean:define>		
				<un:tree initialUnit="externalInstitutionUnit" unitParamName="unitID" path="<%= noOfficialURL %>" state="true"/>
			</logic:notEmpty>

		</logic:notEmpty>		
		
	</logic:notEmpty>
			
</logic:present>
