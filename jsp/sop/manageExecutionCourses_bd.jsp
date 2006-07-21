<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>
<h2>Gestão de Disciplinas</h2>
<br />
<span class="error"><html:errors /></span>
<html:form action="/manageExecutionCourses" focus="executionDegreeOID">
<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2" class="infoop">
			Nota: Na indicaçãodo nome pode ser fornecido apenas parte do nome da disciplina.<br />
			Exemplo 1: Para selecionar todas as disciplinas que começam com a letra "A" escreva <strong>A%</strong><br />
			Exemplo 2: Para selecionar todas as disciplinas que começam com a letra "A" e que tenham um segundo nome que começa com a letra "M" escreva <strong>A% M%</strong><br />
		</td>
	</tr>
	<tr><td colspan="2"><br /><br /></td></tr>
  <tr>
    <td nowrap="nowrap">
    	<bean:message key="property.executionPeriod"/>:
    </td>
    <td nowrap="nowrap">
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodOID" property="executionPeriodOID"
					 size="1"
					 onchange="this.form.method.value='changeExecutionPeriod';this.form.page.value='0';this.form.submit();">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td nowrap="nowrap">
    	<bean:message key="property.executionDegree"/>:
    </td>
    <td nowrap="nowrap">
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeOID" property="executionDegreeOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LIST_INFOEXECUTIONDEGREE %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td nowrap="nowrap">
    	<bean:message key="property.curricularYear"/>:
    </td>
    <td nowrap="nowrap">
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYearOID" property="curricularYearOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_CURRICULAR_YEARS %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td nowrap="nowrap">
    	<bean:message key="property.executionCourse.name"/>:
    </td>
    <td nowrap="nowrap">
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.executionCourseName" property="executionCourseName" size="30"/>
    </td>
  </tr>
</table>
<br />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>    
<br />
<jsp:include page="listExecutionCourses.jsp"/>