<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>



<br/>
<bean:write name="academicInterval" property="pathName" />
<br/>

<html:form action="/mergeExecutionCoursesForm.do?method=mergeExecutionCourses" >

    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="<%= PresentationConstants.ACADEMIC_INTERVAL%>" value="<%= ((AcademicInterval)request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL)).getResumedRepresentationInStringFormat() %>"/>
	<br/>
	
	
	<br/>
	
	
	<table cellpadding="1" >
	<tr >
	<td valign="top">
	<bean:write name="sourceInfoDegree" property="sigla" /> - 
	<bean:write name="sourceInfoDegree" property="presentationName" />
	<br/>
	<strong>Escolha a Disciplina Execução de Origem</strong>
	<br/>	
	<br/>
	<table>
	<logic:iterate id="executionCourse" name="sourceExecutionCourses">
		<tr>
			<td class="listClasses">
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sourceExecutionCourseId" property="sourceExecutionCourseId" idName="executionCourse" value="idInternal"/> 
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="sigla"/>
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="nome"/>
			</td>
		</tr>
	</logic:iterate>
	</table>
	</td>
	<td valign="top">
	<bean:write name="destinationInfoDegree" property="sigla" /> - 
	<bean:write name="destinationInfoDegree" property="presentationName" />
	<br/>
	<strong>Escolha a Disciplina Execução de Destino</strong>
	<table>
	<logic:iterate id="executionCourse" name="destinationExecutionCourses">
		<tr>
			<td class="listClasses">
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.destinationExecutionCourseId" property="destinationExecutionCourseId" idName="executionCourse" value="idInternal"/> 
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="sigla"/>
			</td>
			<td class="listClasses">
			<bean:write name="executionCourse" property="nome"/>
			</td>
		</tr>
	</logic:iterate>
	</table>
	</td>
	</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>