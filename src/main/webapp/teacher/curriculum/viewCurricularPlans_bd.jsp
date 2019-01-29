<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>
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
					O plano curricular com a data mais antiga (ano da sua entrada no <%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>), contem o seu currï¿?culo tal e qual como o pode visionar no ponto habitual acedido atravï¿?s da pï¿?gina do <%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>.<br/>
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
        		<bean:write name="link"/><bean:write name="studentCP" property="externalId" />
        	</bean:define>
        	<bean:define id="linkDescription">
        		<bean:write name="studentCP" property="infoStudent.number" /> - 
            	(<bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.degreeType" />)  
                <bean:write name="studentCP" property="infoDegreeCurricularPlan.infoDegree.nome" /> - 
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
