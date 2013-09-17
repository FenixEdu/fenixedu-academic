<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/units" prefix="un" %>
<html:xhtml/>

<h2><bean:message key="title.units.merge" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">
	
	<logic:notEmpty name="externalInstitutionUnit">
		
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
	
		<p class="mtop3 mbottom2"><strong><bean:message key="label.choose.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
		<un:tree initialUnit="externalInstitutionUnit" unitParamName="unitID" path="/manager/unitsMerge.do?method=seeChoosedUnit" state="true"/>
		
	</logic:notEmpty>
	
</logic:present>

