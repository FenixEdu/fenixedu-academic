<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.curricularCourse" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/insertCurricularCourse" method ="get">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insert"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.nameEn"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.nameEn" size="60" property="nameEn" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.code"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.code" size="12" property="code" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.acronym"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.acronym" size="12" property="acronym" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.type"/>
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.type" property="type">
					<html:options collection="values" property="value" labelProperty="label"/>
    			</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.gradeType"/>
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.GradeScale" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.gradeType" property="gradeType">
					<html:option bundle="ENUMERATION_RESOURCES" key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>							
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.mandatory"/>
			</td>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.mandatory" property="mandatory" value ="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.basic"/>
			</td>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.basic" property="basic" value ="true"/>
			</td>
		</tr>		
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoreticalHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHours" size="5" property="theoreticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.praticalHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHours" size="5" property="praticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoPratHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHours" size="5" property="theoPratHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.labHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.labHours" size="5" property="labHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.maxIncrementNac"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maxIncrementNac" size="5" property="maxIncrementNac" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.minIncrementNac"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minIncrementNac" size="5" property="minIncrementNac" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.credits" />
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.credits" size="5" property="credits" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.ectsCredits"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.ectsCredits" size="5" property="ectsCredits" />
			</td>
		</tr>			
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.weight"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.weight" size="5" property="weight" />
			</td>
		</tr>			

		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.enrollmentWeigth"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrollmentWeigth" size="5" property="enrollmentWeigth"/>
			</td>
		</tr>			
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.mandatoryEnrollment"/>
			</td>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.mandatoryEnrollment" property="mandatoryEnrollment" value ="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.enrollmentAllowed"/>
			</td>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.enrollmentAllowed" property="enrollmentAllowed" value ="true"/>
			</td>
		</tr>
	</table>
	
	<br>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>