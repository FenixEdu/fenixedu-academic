<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" %>
<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<div id="vert-spacer">
<table width="100%">
	<tr>
        <td align="center"><h2>DECLARAÇÃO</h2></td>
  	</tr>
	<tr>
		<td>
			<br />	
			<br />
			<%-- The Original Declaration --%>
				<jsp:include page="./declarationTemplate1.jsp" flush="true" />
   					<logic:equal name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE.toString()%>'>
    		<%-- Candidate Information if necessary --%>
   				<jsp:include page="./declarationTemplate2.jsp" flush="true" />
					</logic:equal >	
				<jsp:include page="./templateDocumentReason.jsp" flush="true" />
				<jsp:include page="./templateFinal.jsp" flush="true" />
		</td>
	</tr>
	<tr> 
	     <td><h2 style="display: inline;">Aluno Nº: </h2><bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/></td>
  	</tr>
</table>
</div>