<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administrativeOffice.marksManagement" /></h2>
<br />
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.DEGREE_LIST %>">
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
<br />
<bean:message key="title.masterDegree.administrativeOffice.chooseDegree" />
<html:form action="/marksManagementDispatchAction?method=chooseCurricularCourse">
	<table>
    <!-- Degree -->
    	<tr>
        	<td><bean:message key="label.masterDegree.administrativeOffice.degree"/>: </td>
         	<td>
            	<html:select property="degree">
               		<html:options collection="<%= SessionConstants.DEGREE_LIST %>" property="infoDegreeCurricularPlan.infoDegree.sigla" labelProperty="infoDegreeCurricularPlan.infoDegree.nome"/>
             	</html:select>          
         	</td>
        </tr>
	</table>
	<br />
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
</html:form>
</logic:present>