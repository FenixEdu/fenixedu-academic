<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administrativeOffice.chooseDegree" /></h2>
<br />
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.DEGREE_LIST %>">
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
	<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
</html:form>
</logic:present>