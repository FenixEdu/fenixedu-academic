<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<span class="error"><html:errors/></span>
<bean:define id="degreeList" name="<%= SessionConstants.MASTER_DEGREE_LIST %>" scope="session" />
<bean:define id="candidates" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATES_AMMOUNT %>" scope="session" />
<bean:define id="link">/chooseDegree.do?degree=</bean:define>
<p><span class="emphasis"><%= ((List) degreeList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></p>
<p><bean:message key="label.masterDegree.chooseOne"/></p>
<% if (((List) degreeList).size() != 0) { 
	List candidateAmmounts = (List) candidates;
	int i = 0; %>
	<table>
		<tr>
			<td class="listClasses-header">Nome</td>
			<!--<td class="listClasses-header">Ano Lectivo</td> -->
			<td class="listClasses-header">Plano Curricular</td>
			<!-- <td class="listClasses-header">&nbsp;</td> -->
		</tr>
		<logic:iterate id="degree" name="degreeList" indexId="indexDegree">
			<bean:define id="degreeLink">
				<bean:write name="link"/><bean:write name="indexDegree"/>
			</bean:define>            	
			<tr>
				<td class="listClasses">
					<html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
						<bean:write name="degree" property="infoDegreeCurricularPlan.infoDegree.nome" /> 
					</html:link>
			   </td>
				<!--<td class="listClasses">
					<html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
						<bean:write name="degree" property="infoExecutionYear.year" />
					</html:link>
			   </td>-->
				<td class="listClasses">
					<html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
						<bean:write name="degree" property="infoDegreeCurricularPlan.name" />
					</html:link>
			   </td>
   			 <!--   <td class="listClasses">
   			   		<logic:equal name="degree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			     		<%= candidateAmmounts.get(i++) %>
			   			<bean:message key="label.masterDegree.coordinator.candidatesAmmount"/> 
        	      	</logic:equal>
			   </td> -->
			</tr>
		</logic:iterate>
	</table>
<% } %>