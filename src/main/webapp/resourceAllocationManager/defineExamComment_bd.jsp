<%@ page language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected"><p>O curso seleccionado
        	&eacute;:</p>
			<strong><jsp:include page="contextNotSelectable.jsp"/></strong>
         </td>
    </tr>
</table>
<br/>

<h2><bean:message key="title.exam.comment"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/defineExamComment">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table cellpadding="0" cellspacing="2">
        <tr>
            <td nowrap="nowrap" class="formTD" align="right">
                <bean:message key="property.exam.comment"/>
            </td>
            <td nowrap="nowrap" align="left">
            	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.comment" property="comment"
            				   value='<%= request.getParameter("comment")%>'
            				   rows="2"
            				   cols="56"/>
            </td>
       	</tr>
	</table>

	<br/>
    <table align="lef">
    	<tr align="center">
        	<td>
			    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode" property="executionCourseCode" value='<%= request.getParameter("executionCourseCode")%>'/>
			    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodName" property="executionPeriodName" value='<%= request.getParameter("executionPeriodName")%>'/>
        		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" value='<%= request.getParameter("executionYear")%>'/>
        		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value='define'/>

		<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>

	<logic:iterate id="year" name="<%= PresentationConstants.CURRICULAR_YEARS_LIST %>" scope="request">
		<logic:equal name="year" value="1">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_1 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_1 %>"
						 value="1"/>
		</logic:equal>
		<logic:equal name="year" value="2">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_2 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_2 %>"
						 value="2"/>
		</logic:equal>
		<logic:equal name="year" value="3">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_3 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_3 %>"
						 value="3"/>
		</logic:equal>
		<logic:equal name="year" value="4">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_4 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_4 %>"
						 value="4"/>
		</logic:equal>
		<logic:equal name="year" value="5">
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEARS_5 %>" property="<%= PresentationConstants.CURRICULAR_YEARS_5 %>"
						 value="5"/>
		</logic:equal>
	</logic:iterate>

            	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
              		<bean:message key="label.create"/>
             	</html:submit>
            </td>
            <td width="20"> </td>
            <td>
            	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
                	<bean:message key="label.clear"/>
                </html:reset>
            </td>
		</tr>
	</table>
</html:form>