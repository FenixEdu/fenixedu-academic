<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
		<table width="100%">
			<tr>
				<td class="infoop">
					<bean:message key="label.program.explanation" />
				</td>
			</tr>
		</table>



<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoCurriculums" name="component" property="infoCurriculums"/>
<logic:empty name="infoCurriculums">
<h2><bean:message key="message.teacher.scientificCouncilControl.program"/></h2>
</logic:empty>	
<logic:iterate id="program" name="infoCurriculums">
<h2><bean:message key="title.program"/></h2>
<h3><bean:write name="program" property="infoCurricularCourse.name"/></h3>
<h3><bean:write name="program" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></h3>
<table>
	<tr>
		<td>
			<bean:write name="program" property="program" filter="false"/>
		</td>
	</tr>
</table>
<br/>	
<logic:notEmpty name="program" property="programEn">
<h2><bean:message key="title.program.eng"/></h2>
<h3><bean:write name="program" property="infoCurricularCourse.name"/></h3>
<h3><bean:write name="program" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></h3>
<table>
	<tr>
		<td>
			<bean:write name="program" property="programEn" filter="false"/>
		</td>
	</tr>
</table>
<br />	
</logic:notEmpty>
<bean:define id="curriculumId" name="program" property="idInternal"/>
<div class="gen-button">
	<html:link page="<%= "/programManagerDA.do?method=prepareEditProgram&amp;objectCode=" + pageContext.findAttribute("objectCode") +"&amp;curriculumCode="+ pageContext.findAttribute("curriculumId")%>">
		<bean:message key="button.edit"/>
	</html:link>
</div>	 
</logic:iterate>
</logic:present>