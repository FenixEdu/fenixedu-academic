<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
<bean:define id="degreeList" name="<%= SessionConstants.MASTER_DEGREE_LIST %>" scope="session" />
<bean:define id="link">/chooseDegree.do?degreeCurricularPlanID=</bean:define>
<p><span class="emphasis"><%= ((List) degreeList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></p>
<p><bean:message key="label.masterDegree.chooseOne"/></p>
<table>
	<tr>
		<td class="listClasses-header">Nome</td>
		<td class="listClasses-header">Plano Curricular</td>
	</tr>
	
	<logic:iterate id="degree" name="degreeList">
		<bean:define id="degreeLink">
			<bean:write name="link"/><bean:write name="degree" property="idInternal"/>
		</bean:define>            	

		<tr>
		   <td class="listClasses">
		        <html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
					<bean:write name="degree" property="infoDegree.nome" />
				</html:link>
		   </td>
		   <td class="listClasses">
				<html:link page='<%= pageContext.findAttribute("degreeLink").toString() %>'>
					<bean:write name="degree" property="name" /> 
				</html:link>
		   </td>	   
		</tr>
	</logic:iterate>

</table>