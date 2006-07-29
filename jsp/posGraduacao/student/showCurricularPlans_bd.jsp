<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>


   <span class="error"><!-- Error messages go here --><html:errors /></span>

    <bean:define id="studentCPList" name="studentCurricularPlans" scope="request" />
    

	<bean:define id="link">/gratuityOperations.do?method=getInformation&studentCPID=</bean:define>

    <%= ((List) studentCPList).size()%> <bean:message key="label.student.studentCPFound"/>    
    <br />    

    <% if (((List) studentCPList).size() != 0) { %>
        	<logic:iterate id="studentCP" name="studentCPList" >
            	<bean:define id="studentCPLink">
            		<bean:write name="link"/><bean:write name="studentCP" property="idInternal" />
            	</bean:define>
                <html:link page='<%= pageContext.findAttribute("studentCPLink").toString() %>'>
                	(<bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" />)  
                    <bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.nome" /> - 
					<logic:present name="studentCP" property="specialization" >
	        			<bean:write name="studentCP" property="specialization.name" bundle="ENUMERATION_RESOURCES" /> - 
					</logic:present>
	        		
        			<bean:write name="studentCP" property="infoStudent.number" />
                </html:link>
                <br/>
        	</logic:iterate>
	<% } %>
