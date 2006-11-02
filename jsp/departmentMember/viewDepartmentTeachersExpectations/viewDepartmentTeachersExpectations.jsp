<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
	.header {
	background-color: #eee;
	padding: 0.5em;
	font-size: 1.4em;
	}
	.block {
	padding: 0 0.5em;
	}
	.indent {
	padding: 0 2em;
	}
	.limbottom li {
	padding-bottom: 8px;			
	
	}
</style>

<html:form
	action="/viewDepartmentTeachersExpectations.do">
	<input alt="input.method" type="hidden" name="method" value="changeExecutionYear"/>
	<logic:notEmpty name="executionYears" scope="request">
		<table class="showinfo2 invisible">
			<tr>
				<td class="aright"><bean:message key="label.common.chooseExecutionYear" />:</td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID"
					onchange="this.form.method.value='changeExecutionYear';this.form.submit();">
					<html:options collection="executionYears" property="value"
						labelProperty="label" />
					</html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</td>
			</tr>
			<tr>
				<td class="aright"><bean:message
					key="label.viewDepartmentTeachersExpectations.chooseTeacher" />:</td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.teacherID" property="teacherID" onchange="this.form.method.value='changeTeacher';this.form.submit();">
					<html:option value="-1" key="label.common.allTeachers" />
					<html:options collection="departmentTeachers" property="value"
						labelProperty="label" />
					</html:select>
				</td>
			</tr>
			<logic:notEmpty name="teacherPersonalExpectations">
			<tr>
				<td colspan="2">
					<bean:define id="executionYearID" name="viewDepartmentTeachersPersonalExpectationsForm" property="executionYearID" />
					<bean:define id="teacherID" name="viewDepartmentTeachersPersonalExpectationsForm" property="teacherID" />
					<html:link action="<%="/viewDepartmentTeachersExpectations.do?method=print&executionYearID=" + executionYearID + "&teacherID=" + teacherID%>" target="_blank">
						<bean:message  key="label.print"/>
					</html:link>
				</td>
			</tr>
			</logic:notEmpty>
		</table>
		<br/>
		<logic:notEmpty name="teacherPersonalExpectations" scope="request">
			<fr:view name="teacherPersonalExpectations" scope="request">
	  			<fr:layout name="flowLayout">
					<fr:property name="eachLayout" value="viewDepartmentTeachersExpectationsNestedTemplateLayout" />
					<fr:property name="htmlSeparator" value="<br/><br/><hr/><br/>"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		<logic:empty name="teacherPersonalExpectations" scope="request">
			<span class="error"><!-- Error messages go here --><bean:message key="label.viewDepartmentTeachersExpectations.noTeacherExpectationsMatchingCriteriaFound"/></span>
		</logic:empty>
	</logic:notEmpty>
	<logic:empty name="executionYears" scope="request">
		<span class="error"><!-- Error messages go here --><bean:message key="label.common.noExecutionsYearsDefined"/></span>
	</logic:empty>
</html:form>




