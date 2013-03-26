<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>
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

