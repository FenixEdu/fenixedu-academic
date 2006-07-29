<%@ page language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
        <h2><bean:message key="title.choose.discipline"/></h2>
        <span class="error"><!-- Error messages go here --><html:errors /></span>
        <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
        <html:form action="<%= path %>">        
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
    	<table border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td nowrap="nowrap" class="formTD"><bean:message key="property.course"/></td>
            <td nowrap="nowrap" class="formTD"><jsp:include page="selectExecutionCourseList.jsp"/></td>
          </tr>
        </table>
<br />


<logic:present name="classOID">
	<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
				 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
</logic:present>

<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Procurar" styleClass="inputbutton"><bean:message key="label.search"/></html:submit>
</html:form>