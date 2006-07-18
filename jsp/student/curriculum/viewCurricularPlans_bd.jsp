<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.applicationTier.IUserView" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudent" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoPerson" %>

<span class="error"><html:errors/></span>
<bean:define id="studentCPList" name="studentCPs" scope="request" />
<bean:define id="link">/viewCurriculum.do?method=getCurriculum&page=0&studentCPID=</bean:define>
<%= ((List) studentCPList).size()%> <bean:message key="label.student.studentCPFound"/>
<br />    
<% if (((List) studentCPList).size() != 0) { %>

	<% if (((List) studentCPList).size() > 1) { %>
		<br/>
		<br/>
		<br/>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="infoop" >
					<span class="emphasis-box">info</span>
				</td>
				<td class="infoop">
					<strong>Nota: � normal a exist�ncia de dois planos curriculares para o mesmo curso.</strong><br/>
					O plano curricular com a data mais antiga (ano da sua entrada no IST), contem o seu curr�culo tal e qual como o pode visionar no ponto habitual acedido atrav�s da p�gina do IST.<br/>
					O plano curricular com a data mais recente (este ano lectivo), contem o seu curr�culo como se o tivesse iniciado este ano, ou seja, apenas com as disciplinas em que se encontra inscrito a partir deste ano lectivo.<br/>
					A raz�o desta separa��o � dar a hip�tese de verificar a correc��o do seu curr�culo passado para, mais tarde (e depois de ter a certeza de que est� tudo em ordem), juntar toda a informa��o num s� plano curricular.
				</td>
			</tr>
		</table>
		<br/>
		<br/>
		<br/>
		<%
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			if(userView.getUtilizador().startsWith("D"))
			{
				InfoStudentCurricularPlan infoScp = (InfoStudentCurricularPlan) ((List) studentCPList).get(0);
				Integer personCode = infoScp.getInfoStudent().getInfoPerson().getIdInternal();
		%>
		
	    <html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personCode.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
	    
		<br/>
		<br/>
		<br/>
		<% } %>
	<% } %>
    	<logic:iterate id="studentCP" name="studentCPList" >
        	<bean:define id="studentCPLink">
        		<bean:write name="link"/><bean:write name="studentCP" property="idInternal" />
        	</bean:define>
        	<bean:define id="linkDescription">
        		<bean:write name="studentCP" property="infoStudent.number" /> - 
            	(<bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" />)  
                <bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.nome" /> - 
				<logic:present name="studentCP" property="specialization" >
        			<bean:message name="studentCP" property="specialization.name" bundle="ENUMERATION_RESOURCES" /> - 
				</logic:present>
    			<bean:write name="studentCP" property="startDate" />
        	</bean:define>
        	<logic:present name="executionDegreeId">
	            <html:link page='<%= pageContext.findAttribute("studentCPLink").toString() + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId").toString() %>'>
	    			<bean:write name="linkDescription"/>
	            </html:link>
            </logic:present>
        	<logic:notPresent name="executionDegreeId">
	            <html:link page='<%= pageContext.findAttribute("studentCPLink").toString() %>'>
	    			<bean:write name="linkDescription"/>
	            </html:link>
            </logic:notPresent>
            <br>
    	</logic:iterate>
<% } %>
