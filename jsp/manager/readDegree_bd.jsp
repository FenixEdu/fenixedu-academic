<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table>
	
<tr>
	
	<td>
		<h3><bean:message key="label.manager.degree.editing"/></h3>
	</td>
	<td>
		<h2><b><bean:write name="infoDegree" property="sigla"/> - </b></h2>
	</td>
	<td>
		<h2><bean:write name="infoDegree" property="nome"/></h2>
	</td>		
</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link page="<%= "/editDegree.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId") %>"><bean:message key="label.manager.edit.degree"/></html:link></li>
	<li><html:link page="<%= "/insertDegreeCurricularPlan.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") %>"><bean:message key="label.manager.insert.degreeCurricularPlan"/></html:link></li>			
</ul>

<span class="error"><html:errors/></span>

<h3><bean:message key="label.manager.degreeCurricularPlans"/></h3>

<logic:empty name="curricularPlansList">
<i><bean:message key="label.manager.degreeCurricularPlans.nonExisting"/></i>
</logic:empty>

<logic:present name="curricularPlansList" scope="request">
<logic:notEmpty name="curricularPlansList">
	
<html:form action="/deleteDegreeCurricularPlans" method="get">

 <bean:define id="onclick">
			return confirm('<bean:message key="message.confirm.delete.degree.curricular.plans"/>')
		  </bean:define>

<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
<table width="70%" cellpadding="0" border="0">
	<tr>
		<td class="listClasses-header">	
		</td>
		<td class="listClasses-header"><bean:message key="label.manager.degreeCurricularPlan.name" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.degree.curricular.plan.degreeDuration" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.degree.curricular.plan.numerusClausus" />
		</td>
	</tr>
	<tr>	 
		<logic:iterate id="degreeCurricularPlan" name="curricularPlansList">			
		<td class="listClasses">
		<html:multibox property="internalIds">
			<bean:write name="degreeCurricularPlan" property="idInternal"/>
		</html:multibox>
		</td>				
		<td class="listClasses"><html:link page="<%= "/readDegreeCurricularPlan.do?degreeId=" + request.getParameter("degreeId")%>" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlan" paramProperty="idInternal"><bean:write name="degreeCurricularPlan" property="name"/></html:link>
		</td>
		<td class="listClasses"><bean:write name="degreeCurricularPlan" property="degreeDuration"/>
		</td>
		<td class="listClasses"><bean:write name="degreeCurricularPlan" property="numerusClausus"/>
		</td>
	 </tr>
</logic:iterate>						
</table>
<br>
<br>	
<html:submit onclick='<%=onclick.toString() %>'><bean:message key="label.manager.delete.selected.degreeCurricularPlans"/></html:submit>
</html:form> 
</logic:notEmpty>	 	
</logic:present>