<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" %>
<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN%>"/>

<div style="padding-top: 100px;">

		<logic:notPresent name="<%= PresentationConstants.DIPLOMA%>">
	
	  		<p>O COORDENADOR DO NÚCLEO DE PÓS-GRADUAÇÃO E FORMAÇÃO CONTÍNUA DO <bean:message key="institution.nameUpperCase" bundle="GLOBAL_RESOURCES"/> DA UNIVERSIDADE TÉCNICA DE LISBOA CERTIFICA, a requerimento do(a) interessado(a), que do seu processo individual organizado e arquivado nesta secretaria, consta que:<br />
			<%--Certificate --%>			
			<jsp:include page="./certificateTemplate1.jsp" flush="true" />
				<logic:present name="<%= PresentationConstants.MATRICULA%>">
			<jsp:include page="./certificateTemplate2.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= PresentationConstants.MATRICULA_ENROLMENT%>">
			<jsp:include page="./certificateTemplate2.jsp" flush="true" />
				</logic:present>	
				<logic:present name="<%= PresentationConstants.DURATION_DEGREE%>">
    	  			<logic:equal name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE.toString()%>'>
    	 	<jsp:include page="./certificateTemplate2.jsp" flush="true" />
    	 	<jsp:include page="./certificateTemplate3.jsp" flush="true" />
    	  			</logic:equal >	
    	  			<logic:notEqual name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE.toString()%>'>
    	  	<jsp:include page="./certificateTemplate2.jsp" flush="true" />	  
    	  			</logic:notEqual >	
				</logic:present>
				<logic:present name="<%= PresentationConstants.ENROLMENT%>">
	  		<jsp:include page="./certificateTemplate2.jsp" flush="true" />
			<jsp:include page="./certificateTemplate4.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= PresentationConstants.APROVMENT%>">
	  		<jsp:include page="./certificateTemplate5.jsp" flush="true" />
			<jsp:include page="./certificateTemplate4.jsp" flush="true" />		
				</logic:present>
				<logic:present name="<%= PresentationConstants.EXTRA_CURRICULAR_APROVMENT%>">
	  		<jsp:include page="./certificateTemplate6.jsp" flush="true" />
			<jsp:include page="./certificateTemplate4.jsp" flush="true" />
				</logic:present>
				
				<logic:present name="<%= PresentationConstants.FINAL_RESULT_SIMPLE%>">
					
					<logic:equal name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate16.jsp" flush="true" />
					</logic:equal>
					<logic:notEqual name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate7.jsp" flush="true" /> 
					</logic:notEqual>
			  		
					<jsp:include page="./certificateTemplate8.jsp" flush="true" />
					
				</logic:present>
				
				<logic:present name="<%= PresentationConstants.DISCRIMINATED_WITHOUT_AVERAGE%>">				
					
					<logic:equal name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate17.jsp" flush="true" />
					</logic:equal>
					<logic:notEqual name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate9.jsp" flush="true" />
					</logic:notEqual>
			  		
			  		<jsp:include page="./certificateTemplate8.jsp" flush="true" />
			  		<jsp:include page="./certificateTemplate10.jsp" flush="true" />
	  		
				</logic:present>
				<logic:present name="<%= PresentationConstants.DISCRIMINATED_WITH_AVERAGE%>">
				
					<logic:equal name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate17.jsp" flush="true" />
					</logic:equal>
					<logic:notEqual name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate9.jsp" flush="true" />
					</logic:notEqual>
			  		
			  		<jsp:include page="./certificateTemplate8.jsp" flush="true" />
			  		<jsp:include page="./certificateTemplate10.jsp" flush="true" />				
				
				</logic:present>
				<logic:present name="<%= PresentationConstants.FINAL_DEGREE_DISCRIMINATED_WITH_AVERAGE%>">	
				
					<logic:equal name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate18.jsp" flush="true" />
					</logic:equal>
					<logic:notEqual name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate14.jsp" flush="true" />
					</logic:notEqual>				
				
					<jsp:include page="./certificateTemplate10.jsp" flush="true" />		
		  			<jsp:include page="./certificateTemplate13.jsp" flush="true" />
				</logic:present>
				<logic:present name="<%= PresentationConstants.FINAL_RESULT_DEGREE_SIMPLE%>">
				
					<logic:equal name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate19.jsp" flush="true" />
					</logic:equal>
					<logic:notEqual name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
						<jsp:include page="./certificateTemplate12.jsp" flush="true" />
					</logic:notEqual>
									
			  		<jsp:include page="./certificateTemplate13.jsp" flush="true" />
				</logic:present>
				
	</p>
	<p>
			<logic:present name="<%= PresentationConstants.DOCUMENT_REASON_LIST%>">
			<jsp:include page="./templateCertificateReason.jsp" flush="true" />
   			</logic:present>
			<%-- Date --%>
			<jsp:include page="./templateFinal.jsp" flush="true" />
	 </p>
	 <p>
		<h2 style="display: inline;">Aluno: </h2><bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/>
	 	</logic:notPresent>
	 	<logic:present name="<%= PresentationConstants.DIPLOMA%>">

			<logic:equal name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
			  	<jsp:include page="./certificateTemplate15.jsp" flush="true" />
			</logic:equal>

			<logic:notEqual name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome" value="Logística">
				<jsp:include page="./certificateTemplate11.jsp" flush="true" />
			</logic:notEqual>
		  	
	  	</logic:present>
	  </p>

</div>


	<%--<logic:equal name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE.toString()%>'>
    	<%-- Candidate Information if necessary --%>
   		<%--<jsp:include page="./declarationTemplate2.jsp" flush="true" />
	</logic:equal >	
	<jsp:include page="./declarationTemplate3.jsp" flush="true" />--%>
	  	