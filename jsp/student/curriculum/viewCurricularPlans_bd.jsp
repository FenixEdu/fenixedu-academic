<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


   <span class="error"><html:errors/></span>

    <bean:define id="studentCPList" name="studentCPs" scope="request" />
    

	<bean:define id="link">/viewCurriculum.do?method=getCurriculum&studentCPID=</bean:define>

    <%= ((List) studentCPList).size()%> <bean:message key="label.student.studentCPFound"/>    
    <br />    

    <% if (((List) studentCPList).size() != 0) { %>
        	<logic:iterate id="studentCP" name="studentCPList" >
            	<bean:define id="studentCPLink">
            		<bean:write name="link"/><bean:write name="studentCP" property="idInternal" />
            	</bean:define>
                <html:link page='<%= pageContext.findAttribute("studentCPLink").toString() %>'>
        			<bean:write name="studentCP" property="infoStudent.number" /> - 
                	(<bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" />)  
                    <bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.nome" /> - 
					<logic:present name="studentCP" property="specialization" >
	        			<bean:write name="studentCP" property="specialization" /> - 
					</logic:present>
	        		
        			<bean:write name="studentCP" property="startDate" />
                </html:link>
                <br>
        	</logic:iterate>
	<% } %>
