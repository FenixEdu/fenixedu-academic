<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%--<logic:present name="degreeId" >--%>
	<bean:define id="degreeId" name="degreeId"/>
	
<table>		
<tr>
			
	<td>
		<h3><bean:message key="label.manager.degree.editing" /></h3>
	</td>
	<td>
		<h2><b><bean:write name="infoDegree" property="sigla" /> - </b></h2>
	</td>
	<td>
		<h2><bean:write name="infoDegree" property="nome" /></h2>
	</td>
			
</tr>
</table>
	
	<h3><bean:message key="label.manager.degreeCurricularPlans" /></h3>

<ul style="list-style-type: square;">
	<li><html:link page="/editDegree.do?method=prepareEdit" paramId="degreeId" paramName="degreeId"><bean:message key="label.manager.edit.degree"/></html:link></li>
</ul>
<%--</logic:present>--%>

<ul style="list-style-type: square;">
	<li><html:link page="/insertDegreeCurricularPlan.do?method=prepareInsert" paramId="degreeId" paramName="degreeId"><bean:message key="label.manager.insert.degreeCurricularPlan"/></html:link></li>			
</ul>





<logic:present name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLANS_LIST %>" scope="request">
<logic:notEmpty name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLANS_LIST %>">
	


<br>
	
<html:form action="/deleteDegreeCurricularPlans" method="get">

<html:hidden property="degreeId" value="<%= degreeId.toString() %>"/>
<table width="50%" cellpadding="0" border="0">
	<tr>
		<td class="listClasses-header">
			
		</td>
		<td class="listClasses-header"><bean:message key="label.manager.degreeCurricularPlan.name" />
		</td>
	</tr>
	<tr>	 
		<logic:iterate id="degreeCurricularPlan" name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLANS_LIST %>">			
		<td class="listClasses">
		<html:multibox property="internalIds">
		<bean:write name="degreeCurricularPlan" property="idInternal"/>
		</html:multibox>
		</td>				
		<td class="listClasses"><html:link page="/readDegreeCurricularPlan.do" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlan" paramProperty="idInternal"><bean:write name="degreeCurricularPlan" property="name"/></html:link>
		</td>
	 </tr>	
</logic:iterate>				
<span class="error"><html:errors/></span>		
</table>
<br>
<br>		
<html:submit><bean:message key="label.manager.delete.selected.degreeCurricularPlans"/></html:submit>
</html:form> 
</logic:notEmpty>	 	
</logic:present>