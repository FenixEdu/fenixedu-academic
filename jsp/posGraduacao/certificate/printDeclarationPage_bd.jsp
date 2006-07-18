<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
<div id="vert-spacer">
<table width="100%">
	<tr>
        <td align="center"><h2>DECLARA��O</h2></td>
  	</tr>
	<tr>
		<td>
			<br />	
			<br />
			<%-- The Original Declaration --%>
				<jsp:include page="./declarationTemplate1.jsp" flush="true" />
   					<logic:equal name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.MASTER_DEGREE.toString()%>'>
    		<%-- Candidate Information if necessary --%>
   				<jsp:include page="./declarationTemplate2.jsp" flush="true" />
					</logic:equal >	
				<jsp:include page="./templateDocumentReason.jsp" flush="true" />
				<jsp:include page="./templateFinal.jsp" flush="true" />
		</td>
	</tr>
	<tr> 
	     <td><h2 style="display: inline;">Aluno N�: </h2><bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/></td>
  	</tr>
</table>
</div>