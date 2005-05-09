<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoBranch" %>

<html:form action="/manageCurricularCourseGroups">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insertCurricularCourseGroup" />
	<bean:define id="dcpId" name="infoDegreeCurricularPlan" property="idInternal"/>
	<bean:define id="curricularCourseGroupId" name="infoCurricularCourseGroup" property="idInternal"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= dcpId.toString() %>"/>
	<html:hidden property="groupId" value="<%= curricularCourseGroupId.toString() %>"/>
	<logic:match name="infoCurricularCourseGroup" property="type" value="label.curricularCourseGroup.area">	
	<html:hidden property="type" value="area"/>
	</logic:match>
	<logic:match name="infoCurricularCourseGroup" property="type" value="label.curricularCourseGroup.optional">	
	<html:hidden property="type" value="optional"/>
	</logic:match>
<table>
<tr>	
<td><bean:message key="label.name"/></td><td><html:text property="name"/></td>
</tr>
<tr>	
<td><bean:message key="label.branch"/></td>
<td>	<html:select property="branchId">
		<html:options collection="branches" property="idInternal" labelProperty="name"/>
	</html:select>
</td>	
</tr>
<logic:match name="infoCurricularCourseGroup" property="type" value="label.curricularCourseGroup.area">
<tr>	
<td><bean:message key="label.areaType"/></td>
<bean:define id="areaValue" name="infoCurricularCourseGroup" property="areaType"/>
<td>	
	<html:select property="areaType" value="<%= areaValue.toString() %>">		
		<html:options collection="areas" property="value" labelProperty="label"/>
	</html:select>
</td>	
</tr>
<tr>
<td><bean:message key="label.minimumCredits"/></td><td>	<html:text property="minimumValue"/></td>
</tr>
<tr>	
<td><bean:message key="label.maximumCredits"/></td><td><html:text property="maximumValue"/></td>
</tr>
</logic:match>
<logic:match name="infoCurricularCourseGroup" property="type" value="label.curricularCourseGroup.optional">
<tr>
<td><bean:message key="label.minimumCourses"/></td><td>	<html:text property="minimumValue"/></td>
</tr>
<tr>	
<td><bean:message key="label.maximumCourses"/></td><td><html:text property="maximumValue"/></td>
</tr>	
</logic:match>
</table>
<br/>
<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>			
</html:form>