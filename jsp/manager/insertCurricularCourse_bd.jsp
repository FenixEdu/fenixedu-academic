<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="DataBeans.InfoDepartmentCourse" %>
<%@ page language="java" %>

<h2><bean:message key="label.manager.insert.curricularCourse" /></h2>

<br>

<html:form action="/insertCurricularCourse" method ="get">
	    
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insert"/>
	<html:hidden property="degreeId"/>
	<html:hidden property="degreeCurricularPlanId"/>
	
	<table>
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text size="60" property="name" />
			</td>
			<td>
				<span class="error"><html:errors property="name"/></span>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="label.manager.curricularCourse.code"/>
			</td>
			<td>
				<html:text size="60" property="code" />
			</td>
			<td>
				<span class="error"><html:errors property="code"/></span>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="message.manager.credits"/>
			</td>
			<td>
				<html:text size="60" property="credits" />
			</td>
			<td>
				<span class="error"><html:errors property="credits"/></span>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="message.manager.theoreticalHours"/>
			</td>
			<td>
				<html:text size="60" property="theoreticalHours" />
			</td>
			<td>
				<span class="error"><html:errors property="theoreticalHours"  /></span>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="message.manager.praticalHours"/>
			</td>
			<td>
				<html:text size="60" property="praticalHours" />
			</td>
			<td>
				<span class="error"><html:errors property="praticalHours"  /></span>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="message.manager.theoPratHours"/>
			</td>
			<td>
				<html:text size="60" property="theoPratHours" />
			</td>
			<td>
				<span class="error"><html:errors property="theoPratHours"  /></span>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="message.manager.labHours"/>
			</td>
			<td>
				<html:text size="60" property="labHours" />
			</td>
			<td>
				<span class="error"><html:errors property="labHours"  /></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.departmentCourse"/>
			</td>
			<td>
				<html:select property="departmentCourse">
					<html:options collection="departmentCoursesList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.type"/>
			</td>
			<td>
				<html:select property="type">
				<html:option value=""/>
				<html:option key="option.curricularCourse.normal" value="1"/>
    			<html:option key="option.curricularCourse.optional" value="2"/>
    			<html:option key="option.curricularCourse.project" value="3"/>
    			<html:option key="option.curricularCourse.tfc" value="4"/>
    			<html:option key="option.curricularCourse.training" value="5"/>
    			<html:option key="option.curricularCourse.laboratory" value="6"/>
    			<html:option key="option.curricularCourse.mType" value="7"/>
    			<html:option key="option.curricularCourse.pType" value="8"/>
    			<html:option key="option.curricularCourse.dmType" value="9"/>
    			<html:option key="option.curricularCourse.aType" value="10"/>
    			<html:option key="option.curricularCourse.mlType" value="11"/>
    			</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.mandatory"/>
			</td>
			<td>
				<html:select property="mandatory">
				<html:option value=""/>
				<html:option key="option.manager.true" value="true"/>
    			<html:option key="option.manager.false" value="false"/>
    			</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.basic"/>
			</td>
			<td>
				<html:select property="basic">
				<html:option value=""/>
				<html:option key="option.manager.true" value="true"/>
    			<html:option key="option.manager.false" value="false"/>
    			</html:select>
			</td>
		</tr>
	</table>
	
	<br>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>