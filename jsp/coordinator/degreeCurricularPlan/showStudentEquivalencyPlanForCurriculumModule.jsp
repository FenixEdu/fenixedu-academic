<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="curriculumModule" name="curriculumModule"/>
<bean:define id="indentLevel" type="java.lang.String" name="indentLevel"/>
<bean:define id="width" type="java.lang.String" name="width"/>

<logic:equal name="curriculumModule" property="leaf" value="true">

	<bean:define id="curriculumLine" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine" name="curriculumModule"/>

	<div class="indent<%= indentLevel %>">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr>
				<td>
					<bean:write name="curriculumLine" property="name"/>
				</td>
				<td class="highlight2 smalltxt" align="center" style="width: 14em;">
					???
				</td>
				<td class="smalltxt" align="right" style="width: 22em;">
					???
				</td>
			</tr>
		</table>
	</div>
</logic:equal>
<logic:notEqual name="curriculumModule" property="leaf" value="true">

	<bean:define id="curriculumGroup" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup" name="curriculumModule"/>

	<div style="padding-left: <%= indentLevel %>em;">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr class="bgcolor2">
				<th class="aleft">
					<bean:write name="curriculumGroup" property="name"/>
				</th>
				<th class="smalltxt" align="center" style="width: 14em;">
					???
				</th>
				<th class="smalltxt" align="right" style="width: 22em;">
					???
				</th>
			</tr>
		</table>
	</div>

	<logic:iterate id="childCurriculumModule" name="curriculumGroup" property="curriculumModulesSet">
		<bean:define id="curriculumModule" name="childCurriculumModule" toScope="request"/>
		<% 
			Integer newIndentLevel = Integer.valueOf(Integer.parseInt(indentLevel) + 3);
			Integer newWidth = Integer.valueOf(Integer.parseInt(width) - 3);
		%>
		<bean:define id="indentLevel" type="java.lang.String" value="<%= newIndentLevel.toString() %>" toScope="request"/>
		<bean:define id="width" type="java.lang.String" value="<%= newWidth.toString() %>" toScope="request"/>
		<jsp:include page="showStudentEquivalencyPlanForCurriculumModule.jsp"/>
	</logic:iterate>
</logic:notEqual>
