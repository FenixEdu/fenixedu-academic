<%@ page language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
        
	   	<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong>
			  <%--
				<logic:match name="path" value="Exam">
			  --%>
    	    		<jsp:include page="contextNotSelectable.jsp"/>
    	      <%--
		        </logic:match>
				<logic:notMatch name="path" value="Exam">
    	    		<jsp:include page="context.jsp"/>
		        </logic:notMatch>
		      --%>
	          </strong>
            </td>
          </tr>
        </table>
        <br/>
        <h2><bean:message key="title.choose.discipline"/></h2>
        <span class="error"><!-- Error messages go here --><html:errors /></span>
        <html:form action="<%= path %>">        
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>

		<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

		<logic:present name="examDateAndTime">
			<html:hidden alt="<%= PresentationConstants.EXAM_DATEANDTIME %>" property="<%= PresentationConstants.EXAM_DATEANDTIME %>"
						 value="<%= pageContext.findAttribute("examDateAndTime").toString() %>"/>
		</logic:present>

		<logic:present name="examDateAndTime">
			<html:hidden alt="<%= PresentationConstants.EXAM_DATEANDTIME %>" property="<%= PresentationConstants.EXAM_DATEANDTIME %>"
						 value="<%= pageContext.findAttribute("examDateAndTime").toString() %>"/>
		</logic:present>

    	<table border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td nowrap="nowrap" class="formTD"><bean:message key="property.course"/></td>
            <td nowrap="nowrap" class="formTD"><jsp:include page="selectExecutionCourseList.jsp"/></td>
          </tr>
        </table>
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Procurar" styleClass="inputbutton"><bean:message key="label.search"/></html:submit>
</html:form>