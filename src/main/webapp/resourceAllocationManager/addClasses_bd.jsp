<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turmas"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<strong><jsp:include page="context.jsp"/></strong>


<h3>Adicionar Turmas</h3>

<logic:present name="<%= PresentationConstants.CLASSES %>" scope="request">
	<html:form action="/addClasses" focus="selectedItems">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="add"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

        <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                     value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
					 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table class="tstyle4 thlight mtop05">
			<tr>
				<th>
				</th>
				<th>
					<bean:message key="label.name"/>
				</th>
				<th>
					<bean:message key="label.degree"/>
				</th>
			</tr>
			<logic:iterate id="infoClass" name="<%= PresentationConstants.CLASSES %>">
				<bean:define id="infoClassOID" name="infoClass" property="externalId"/>
				<bean:define id="infoExecutionDegreeOID" name="infoClass" property="infoExecutionDegree.externalId"/>
				<tr>
	              	<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
							<bean:write name="infoClass" property="externalId"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="infoClass" property="nome"/>
					</td>
					<td>
						<bean:write name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.add"/>
		</html:submit>			
	</html:form>
</logic:present>

<logic:notPresent name="<%= PresentationConstants.CLASSES %>" scope="request">
	<p>
		<span class="warning0"><!-- Error messages go here --><bean:message key="errors.classes.none"/></span>	
	</p>
</logic:notPresent>