<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<bean:define id="studentCPList" name="studentCPs" scope="request" />
<bean:define id="link">/finalWorkManagement.do?method=getCurriculum&page=0&studentCPID=</bean:define>
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
					<strong>Nota: ï¿? normal a existï¿?ncia de dois planos curriculares para o mesmo curso.</strong><br/>
					O plano curricular com a data mais antiga (ano da sua entrada no IST), contem o seu currï¿?culo tal e qual como o pode visionar no ponto habitual acedido atravï¿?s da pï¿?gina do IST.<br/>
					O plano curricular com a data mais recente (este ano lectivo), contem o seu currï¿?culo como se o tivesse iniciado este ano, ou seja, apenas com as disciplinas em que se encontra inscrito a partir deste ano lectivo.<br/>
					A razï¿?o desta separaï¿?ï¿?o ï¿? dar a hipï¿?tese de verificar a correcï¿?ï¿?o do seu currï¿?culo passado para, mais tarde (e depois de ter a certeza de que estï¿? tudo em ordem), juntar toda a informação num sï¿? plano curricular.
				</td>
			</tr>
		</table>
		<br/>
		<br/>
		<br/>
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
        			<bean:message name="studentCP" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> - 
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
            <br/>
    	</logic:iterate>
<% } %>
