<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>"/>
<br />
<table width="100%">
 	<tr>
		<td>	
	  		<p>O CHEFE DE SECÇÃO DE PÓS-GRADUAÇÃO DO INSTITUTO SUPERIOR TÉCNICO DA UNIVERSIDADE TÉCNICA DE LISBOA CERTIFICA, a requerimento do interessado que do seu processo individual organizado e arquivado nesta secretaria, consta que:</p>
	   </td>
	</tr>
    <tr>
		<td> 
			<%--Certificate --%>
			<jsp:include page="./certificateTemplate1.jsp" flush="true" />
				<logic:present name="<%= SessionConstants.MATRICULA%>">
			<jsp:include page="./certificateTemplate2.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= SessionConstants.MATRICULA_ENROLMENT%>">
			<jsp:include page="./certificateTemplate2.jsp" flush="true" />
				</logic:present>	
				<logic:present name="<%= SessionConstants.DURATION_DEGREE%>">
    	  			<logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
    	 	<jsp:include page="./certificateTemplate2.jsp" flush="true" />
    	 	<jsp:include page="./certificateTemplate3.jsp" flush="true" />
    	  			</logic:equal >	
    	  			<logic:notEqual name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
    	  	<jsp:include page="./certificateTemplate2.jsp" flush="true" />	  
    	  			</logic:notEqual >	
				</logic:present>
				<logic:present name="<%= SessionConstants.ENROLMENT%>">
	  		<jsp:include page="./certificateTemplate2.jsp" flush="true" />
			<jsp:include page="./certificateTemplate4.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= SessionConstants.APROVMENT%>">
	  		<jsp:include page="./certificateTemplate5.jsp" flush="true" />
			<jsp:include page="./certificateTemplate4.jsp" flush="true" />		
				</logic:present>
				<logic:present name="<%= SessionConstants.EXTRA_CURRICULAR_APROVMENT%>">
	  		<jsp:include page="./certificateTemplate6.jsp" flush="true" />
			<jsp:include page="./certificateTemplate4.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= SessionConstants.FINAL_RESULT_SIMPLE%>">
	  		<jsp:include page="./certificateTemplate7.jsp" flush="true" />
			<jsp:include page="./certificateTemplate8.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= SessionConstants.DISCRIMINATED_WITHOUT_AVERAGE%>">
	  		<jsp:include page="./certificateTemplate9.jsp" flush="true" />
	  		<jsp:include page="./certificateTemplate8.jsp" flush="true" />
	  		<jsp:include page="./certificateTemplate10.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= SessionConstants.DISCRIMINATED_WITH_AVERAGE%>">
	  		<jsp:include page="./certificateTemplate9.jsp" flush="true" />
	  		<jsp:include page="./certificateTemplate8.jsp" flush="true" />
	  		<jsp:include page="./certificateTemplate10.jsp" flush="true" />
				</logic:present>
		</td>
	</tr>
</table>
	<logic:present name="<%= SessionConstants.DOCUMENT_REASON_LIST%>">
		<jsp:include page="./templateDocumentReason.jsp" flush="true" />
   	</logic:present>
	<%-- Date --%>
	<jsp:include page="./templateFinal.jsp" flush="true" />
	 <h2 style="display: inline;">Aluno: </h2><bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/>
	<%--<logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
    	<%-- Candidate Information if necessary --%>
   		<%--<jsp:include page="./declarationTemplate2.jsp" flush="true" />
	</logic:equal >	
	<jsp:include page="./declarationTemplate3.jsp" flush="true" />--%>
	  	