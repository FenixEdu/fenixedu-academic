<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
    <table width="100%" height="100%" border="1">
	  <tr> 
        <td align="right" valign="bottom"> <b>ALUNO Nº: </b> 
         <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/> 
        </td>
      </tr>
	  <tr> 
        <td align="center" > <b>DECLARAÇÃO</b></td>
      </tr>
 

	<%-- The Original Guide --%>
	<jsp:include page="./declarationTemplate1.jsp" flush="true" />

	<%-- The Original Guide --%> 
	<%-- <jsp:include page="./declarationTemplate1.jsp" flush="true" /> --%>


   <logic:equal name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="Mestrado">
    	<%-- Candidate Information if necessary --%>
   		<jsp:include page="./informationTemplate1.jsp" flush="true" />
	</logic:equal >	

