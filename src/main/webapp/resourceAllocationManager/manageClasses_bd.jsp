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
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/commons/contextExecutionDegreeAndCurricularYear.jsp"/>

<h2>Manipular Turmas <span class="small"><c:out value="${context_selection_bean.executionDegree.degreeCurricularPlan.name} - ${context_selection_bean.curricularYear.year}º ano (${context_selection_bean.academicInterval.pathName})" /></span></h2>

<jsp:include page="context.jsp" />

<html:form action="/manageClasses" focus="className">

    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value= "1"/>

	<p class="mbottom15">
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

   	<bean:define id="degree" type="org.fenixedu.academic.domain.Degree" name="executionDegreeD" property="degreeCurricularPlan.degree"/>   	
   	
	   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.className" property="className"/>
	   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="btn btn-primary btn-xs">
	   		<bean:message key="label.create"/>
	   	</html:submit>


</html:form>


<logic:present name="<%= PresentationConstants.CLASSES %>" scope="request">
  <html:form action="/manageClasses">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteClasses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

<table class="tstyle2 mtop15">
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.name"/>
		</th>
		<th>
			<bean:message key="label.delete"/>
		</th>				
	</tr>		
	<bean:define id="deleteConfirm">
		return confirm('<bean:message key="message.confirm.delete.class"/>')
	</bean:define>
	<logic:iterate id="classView" name="<%= PresentationConstants.CLASSES %>" scope="request">
		<bean:define id="classOID"
					 type="java.lang.String"
					 name="classView"
					 property="externalId"/>
		<tr>
       		<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
					<bean:write name="classView" property="externalId"/>
				</html:multibox>
			</td>
			<td>
				<html:link page="<%= "/manageClass.do?method=prepare&amp;"
						+ PresentationConstants.CLASS_VIEW_OID
						+ "="
						+ pageContext.findAttribute("classOID")
						+ "&amp;"
						+ PresentationConstants.ACADEMIC_INTERVAL
						+ "="
						+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
						+ "&amp;"
						+ PresentationConstants.CURRICULAR_YEAR_OID
						+ "="
						+ pageContext.findAttribute("curricularYearOID")
						+ "&amp;"
						+ PresentationConstants.EXECUTION_DEGREE_OID
						+ "="
						+ pageContext.findAttribute("executionDegreeOID") %>">
					<span style="align: center;">
						<jsp:getProperty name="classView" property="nome" />
					</span>
				</html:link>
			</td>
			<td>
				<div align="center">
					<html:link page="<%= "/manageClasses.do?method=delete&amp;"
							+ PresentationConstants.CLASS_VIEW_OID
						  	+ "="
						  	+ pageContext.findAttribute("classOID")
						  	+ "&amp;"
							+ PresentationConstants.ACADEMIC_INTERVAL
						  	+ "="
						  	+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
						  	+ "&amp;"
						  	+ PresentationConstants.CURRICULAR_YEAR_OID
							+ "="
						  	+ pageContext.findAttribute("curricularYearOID")
						  	+ "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID
						  	+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>"
								onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
						<bean:message key="label.delete"/>
					</html:link>
				</div>
			</td>
		</tr>
	</logic:iterate>
</table>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="btn btn-danger btn-sm" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
		<bean:message key="link.delete"/>
	</html:submit>
  </html:form>
</logic:present>
<logic:notPresent name="<%= PresentationConstants.CLASSES %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="listClasses.emptyClasses"/></span>
</logic:notPresent>