<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>

<em><bean:message key="label.manager.executionCourses"/></em>
<h2>Gestão de Disciplinas</h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/manageExecutionCourses" focus="executionDegreeOID">

		<div class="infoop3">
			<p class="mvert025">Nota: Na indicaçãodo nome pode ser fornecido apenas parte do nome da disciplina.</p>
			<p class="mvert025">Exemplo 1: Para selecionar todas as disciplinas que começam com a letra "A" escreva <strong>A%</strong></p>
			<p class="mvert025">Exemplo 2: Para selecionar todas as disciplinas que começam com a letra "A" e que tenham um segundo nome que começa com a letra "M" escreva <strong>A% M%</strong></p>
		</div>

<table class="tstyle5 thlight thright mtop15">
	<tr>
		<th>
	    	<bean:message key="property.executionPeriod"/>:
	    </th>
	    <td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodOID" property="executionPeriodOID"
						 size="1"
						 onchange="this.form.method.value='changeExecutionPeriod';this.form.page.value='0';this.form.submit();">
				<html:options	property="value" 
	     						labelProperty="label" 
								collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD %>" />
			</html:select>
			<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
	    </td>
  </tr>
  <tr>
    <th>
    	<bean:message key="property.executionDegree"/>:
    </th>
    <td>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeOID" property="executionDegreeOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LIST_INFOEXECUTIONDEGREE %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <th>
    	<bean:message key="property.curricularYear"/>:
    </th>
    <td>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYearOID" property="curricularYearOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_CURRICULAR_YEARS %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <th>
    	<bean:message key="property.executionCourse.name"/>:
    </th>
    <td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.executionCourseName" property="executionCourseName" size="30"/>
    </td>
  </tr>
</table>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose"/>
	</html:submit>
</p>

</html:form>    


<jsp:include page="listExecutionCourses.jsp"/>