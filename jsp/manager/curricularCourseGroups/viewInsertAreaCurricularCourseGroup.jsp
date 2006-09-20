<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoBranch" %>

<html:form action="/manageCurricularCourseGroups">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertCurricularCourseGroup" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.type" property="type" value="area"/>
	<bean:define id="dcpId" name="infoDegreeCurricularPlan" property="idInternal"/>
    <bean:define id="degreeId" name="degreeId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= dcpId.toString() %>"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= degreeId.toString() %>"/>
	
<table>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.name"/></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name"/></td>
</tr>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.branch"/></td>
<td>	<html:select bundle="HTMLALT_RESOURCES" altKey="select.branchId" property="branchId">
		<html:options collection="branches" property="idInternal" labelProperty="name"/>
	</html:select>
</td>	
</tr>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.areaType"/></td>
<td>	
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.areaType" property="areaType">
		<html:options collection="areas" property="value" labelProperty="label"/>
	</html:select>
</td>	
</tr>
<tr>
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.minimumCredits"/></td><td>	<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumValue" property="minimumValue"/></td>
</tr>
<tr>	
<td><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.maximumCredits"/></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumValue" property="maximumValue"/></td>
</tr>
	
</table>	

<br/>
<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>