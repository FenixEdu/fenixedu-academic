<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message key="label.manager.executionCourses"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/executionCoursesInformation" >
	<html:hidden property="degreeCurricularPlanID" value="<%= request.getAttribute("degreeCurricularPlanID").toString() %>"/>

<div class="infoop2">
	Nota: Na indicaçãodo nome pode ser fornecido apenas parte do nome da disciplina.<br/>
	Exemplo 1: Para selecionar todas as disciplinas que começam com a letra "A" escreva <strong>A%</strong><br />
	Exemplo 2: Para selecionar todas as disciplinas que começam com a letra "A" e que tenham um segundo nome que começa com a letra "M" escreva <strong>A% M%</strong><br />
</div>



<table class="tstyle5">
	<tr>
    	<td>
    		<bean:message key="property.executionPeriod"/>:
    	</td>
    	<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodOID" property="executionPeriodOID" size="1">
				<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD %>" />
			</html:select>
    	</td>
  	</tr>
  <tr>
    <td>
    	<bean:message key="property.curricularYear"/>:
    </td>
    <td>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYearOID" property="curricularYearOID" size="1">
			<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= PresentationConstants.LABELLIST_CURRICULAR_YEARS %>" />
		</html:select>
    </td>
  </tr>
  <tr>
    <td>
    	<bean:message key="property.executionCourse.name"/>:
    </td>
    <td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.executionCourseName" property="executionCourseName" size="30"/>
    </td>
  </tr>  
</table>



<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="getExecutionCourses"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose"/>
	</html:submit>
</p>

</html:form>    

