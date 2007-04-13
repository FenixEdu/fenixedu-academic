<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.ExecutionPeriod" %>
<%@ page import="net.sourceforge.fenixedu.domain.CurricularYear" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
		<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
	</html:link>
	>
	<bean:message key="label.teacherServiceDistribution.createCourse"/>
</h3>


<br/>
<br/>
<html:form action="/valuationCoursesGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createValuationCompetenceCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCurricularCourse" property="valuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCourse" property="valuationCourse"/>

<table class="vtsbc">
	<tr>
		<th colspan="2">
			<b><bean:message key="label.teacherServiceDistribution.createNewCourse"/></b>
		</th>
	</tr>
	<tr>
		<td align="left">
			<b><bean:message key="label.teacherServiceDistribution.name"/>:</b> 
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="24" maxlength="240"/> 
			&nbsp;&nbsp;&nbsp;
			<html:submit>
				<bean:message key="label.teacherServiceDistribution.create"/>
			</html:submit>
		</td>
	</tr>
</table>
</html:form>


<html:form action="/valuationCoursesGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="loadValuationCompetenceCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCurricularCourse" property="valuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

<br/>
<span class="error"><html:errors property="name"/></span>
<br/>


<table class="search">
	<tr>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.editCourse"/>:</b>
		</td>
		<td>
			<html:select property="valuationCourse">
				<html:option value="0">&nbsp;</html:option>
				<html:options collection="competenceCoursesList" property="idInternal" labelProperty="name"/>
			</html:select>
			<html:submit>
			<bean:message key="button.submit"/>
			</html:submit>
		</td>
		<td>
		</td>
	<tr>
</table>
</html:form>

 
<bean:define id="valuationGroup" name="valuationTeachersGroupForm" property="valuationGrouping"/>

<html:form action="/valuationCoursesGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="addValuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCurricularCourse" property="valuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCourse" property="valuationCourse"/>
<br/>
<br/>
<br/>
<br/>
	
	<bean:define id="valuationCourse" name="valuationTeachersGroupForm" property="valuationCourse"/>
	
	<logic:notEqual name="valuationTeachersGroupForm" property="valuationCourse" value="0">		
		<b><bean:message key="label.teacherServiceDistribution.associatedCurricularPlans"/>:</b>
		<br/>
		<logic:present name="curricularCoursesList">
			<table class="tstyle1">
				<tr>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.acronym"/></b>
					</th>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.curricularPlan"/></b>
					</th>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.executionPEriod"/></b>
					</th>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.curricularYears"/></b>
					</th>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.theoreticalHoursShort"/></b>
					</th>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.praticalHoursShort"/></b>
					</th>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.theoPratHoursShort"/></b>
					</th>
					<th>
						<b><bean:message key="label.teacherServiceDistribution.laboratorialHoursShort"/></b>
					</th>
					<th>
					</th>
				</tr>
				<logic:iterate name="curricularCoursesList" id="curricularCourse">
				<tr>
					<td>
						<bean:write name="curricularCourse" property="acronym" />
					</td>
					<td>
						<bean:write name="curricularCourse" property="degreeCurricularPlan.name" />
					</td>
					<td>					
						<bean:write name="curricularCourse" property="executionPeriod.name" />
					</td>
					<td>
						<logic:iterate name="curricularCourse" property="curricularYears" id="curricularYear">
							<bean:write name="curricularYear" property="year"/>
						</logic:iterate>
					</td>
					<td>
						<bean:write name="curricularCourse" property="theoreticalHours"/>
					</td>
					<td>
						<bean:write name="curricularCourse" property="praticalHours"/>
					</td>
					<td>
						<bean:write name="curricularCourse" property="theoPratHours"/>
					</td>
					<td>
						<bean:write name="curricularCourse" property="laboratorialHours"/>
					</td>
					<td>
						<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal" />
						<html:link page="<%= "/valuationCoursesGroup.do?method=removeValuationCurricularCourse&valuationCurricularCourse=" + curricularCourseId + "&page=0&valuationGrouping=" + valuationGroup +"&valuationCourse=" + valuationCourse %>">
						<%--<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick='<%= "this.form.method.value='removeValuationCurricularCourse';this.form.valuationCurricularCourse.value=" +  curricularCourseId + ";this.form.page.value=0;this.form.submit();"%>'>
							--%>
						<bean:message key="link.remove"/>
						</html:link>
					</td>
				</tr>
				</logic:iterate>
			</table>
		</logic:present>
		<logic:notPresent name="curricularCoursesList">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span class="error">
				<bean:message key="label.teacherServiceDistribution.notAvailableCurricularPlans"/>
			</span>
		</logic:notPresent>
		
		<br/>
		<br/>
		<br/>
		<br/>
		
		<b><bean:message key="label.teacherServiceDistribution.addCurricularPlan"/>:</b>
		<br/>
	
		<table class="vtsbc">
			<tr>
				<th>
					<b><bean:message key="label.teacherServiceDistribution.acronym"/></b>
				</th>
				<th>
					<b><bean:message key="label.teacherServiceDistribution.curricularPlan"/></b>
				</th>
				<th>
					<b><bean:message key="label.teacherServiceDistribution.executionPEriod"/></b>
				</th>
				<th>
					<b><bean:message key="label.teacherServiceDistribution.curricularYears"/></b>
				</th>
				<th>
					<b><bean:message key="label.teacherServiceDistribution.hours"/></b>
				</th>
			</tr>
			<tr>
				<td>
					<logic:present name="competenceCourseName">
						<bean:define name="competenceCourseName" id="competenceCourseName" />
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.courseName" property="courseName" size="24" maxlength="240" value="<%= (String)competenceCourseName %>"/>
					</logic:present>
					<logic:notPresent name="competenceCourseName">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.courseName" property="courseName" size="24" maxlength="240" />
					</logic:notPresent>
				</td>
				<td>
					<html:select property="degreeCurricularPlan">
						<html:options collection="curricularPlansList" property="idInternal" labelProperty="name"/>
					</html:select> 
				</td>
				<td>
					<html:select property="executionPeriod">
						<html:options collection="executionPeriodsList" property="idInternal" labelProperty="name"/>
					</html:select> 
				</td>
				<td align="center">
					<table border="0">			
						<logic:iterate name="curricularYearsList" id="curricularYear">
							<tr>
								<td>
									<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularYearsArray" property="curricularYearsArray">
										<bean:write name="curricularYear" property="idInternal"/>
									</html:multibox>
									<b><bean:write name="curricularYear" property="year"/></b>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
				<td>
					<table border="0">
						<tr>
							<td>
								<b><bean:message key="label.teacherServiceDistribution.theoretical"/></b>
							</td>
							<td>
								<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHours" property="theoreticalHours" size="2" maxlength="4" />
							</td>
						</tr>
						<tr>
							<td>
								<b><bean:message key="label.teacherServiceDistribution.pratical"/></b>
							</td>
							<td>
								<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHours" property="praticalHours" size="2" maxlength="4" />
							</td>
						</tr>
						<tr>
							<td>
								<b><bean:message key="label.teacherServiceDistribution.theoPrat"/></b>
							</td>
							<td>
								<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHours" property="theoPratHours" size="2" maxlength="4" />
							</td>
						</tr>
						<tr>
							<td>
								<b><bean:message key="label.teacherServiceDistribution.laboratorial"/></b>
							</td>
							<td>
								<html:text bundle="HTMLALT_RESOURCES" altKey="text.laboratorialHours" property="laboratorialHours" size="2" maxlength="4" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
		<%-- <html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='addValuationCurricularCourse'; this.form.page.value=3; this.form.submit();">
		--%>
		<html:submit>
			<bean:message key="button.add"/>
		</html:submit>
		
		<span class="error"><html:errors /></span>
		
	</logic:notEqual>
	

</html:form> 

<br/>
<br/>

<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>

