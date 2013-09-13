<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>


   <span class="error"><!-- Error messages go here --><html:errors /></span>

    <bean:define id="studentCPList" name="studentCurricularPlans" scope="request" />
    

	<bean:define id="link">/gratuityOperations.do?method=getInformation&studentCPID=</bean:define>

    <%= ((List) studentCPList).size()%> <bean:message key="label.student.studentCPFound"/>    
    <br />    

    <% if (((List) studentCPList).size() != 0) { %>
        	<logic:iterate id="studentCP" name="studentCPList" >
            	<bean:define id="studentCPLink">
            		<bean:write name="link"/><bean:write name="studentCP" property="externalId" />
            	</bean:define>
                <html:link page='<%= pageContext.findAttribute("studentCPLink").toString() %>'>
                	(<bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.degreeType" />)  
                    <bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.nome" /> - 
					<logic:present name="studentCP" property="specialization" >
	        			<bean:write name="studentCP" property="specialization.name" bundle="ENUMERATION_RESOURCES" /> - 
					</logic:present>
	        		
        			<bean:write name="studentCP" property="infoStudent.number" />
                </html:link>
                <br/>
        	</logic:iterate>
	<% } %>
