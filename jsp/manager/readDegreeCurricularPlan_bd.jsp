<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="Util.Data" %>
<%@ page import="java.util.Date" %>

<table>
	<tr>
		<logic:present name="infoDegreeCurricularPlan">
			<td>
				<h3><bean:message key="label.manager.degreeCurricularPlan.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:write name="infoDegreeCurricularPlan" property="name"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link page="/editDegreeCurricularPlan.do?method=prepareEdit"  paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message key="label.manager.edit.degreeCurricularPlan"/></html:link></li>
</ul>

<ul style="list-style-type: square;">
	<li><html:link page="/insertCurricularCourse.do?method=prepareInsert" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message key="label.manager.insert.curricularCourse"/></html:link></li>			
</ul>

<h3><bean:message key="label.manager.curricularCourses"/></h3>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId"/>

<logic:present name="curricularCoursesList" scope="request">
<logic:notEmpty name="curricularCoursesList">
	
	<html:form action="/deleteDegreeCurricularPlans" method="get">
		<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
			<table width="50%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header">
			
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourse.name" />
					</td>
				</tr>
				<logic:iterate id="curricularCourse" name="curricularCoursesList">
				<tr>	 
					<td class="listClasses">
						<html:multibox property="internalIds">
							<bean:write name="curricularCourse" property="idInternal"/>
						</html:multibox>
					</td>				
					<td class="listClasses"><html:link page="/readCurricularCourse.do" paramId="curricularCourseId" paramName="curricularCourse" paramProperty="idInternal"><bean:write name="curricularCourse" property="name"/></html:link>
					</td>
	 			</tr>
	 			</logic:iterate>				
				<span class="error"><html:errors/></span>		
			</table>
			
<br>
<br>	

		<html:submit><bean:message key="label.manager.delete.selected.curricularCourses"/></html:submit>
	</html:form> 
</logic:notEmpty>	 	
</logic:present>