
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<logic:present name="component" property="infoCurriculum">
<bean:define id="infoCurriculum" name="component" property="infoCurriculum"/>
<logic:notEmpty name="infoCurriculum" property="program">
<h2><bean:message key="label.program"/></h2>
<bean:write name="infoCurriculum" property="program" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="programEn">
<h2><bean:message key="label.program.eng"/></h2>
<bean:write name="infoCurriculum" property="programEn" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
<h2><bean:message key="label.operacionalObjectives"/></h2>
<bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="operacionalObjectivesEn">
<h2><bean:message key="label.operacionalObjectives.eng"/></h2>
<bean:write name="infoCurriculum" property="operacionalObjectivesEn" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="generalObjectives">
<h2><bean:message key="label.generalObjectives"/></h2>
<bean:write name="infoCurriculum" property="generalObjectives" filter="false"/>
</logic:notEmpty>

<logic:notEmpty name="infoCurriculum" property="generalObjectivesEn">
<h2><bean:message key="label.generalObjectives.eng"/></h2>
<bean:write name="infoCurriculum" property="generalObjectivesEn" filter="false"/>
</logic:notEmpty>

<logic:equal name="infoCurriculum" property="infoCurricularCourse.basic" value="true">
<bean:define id="curriculumId" name="infoCurriculum" property="idInternal"/>
<br/>
<br/>
<br/>
<div class="gen-button">
	<html:link page="<%= "/curricularCourseManager.do?method=prepareEditCurriculum&index=" + pageContext.findAttribute("curriculumId")%>">
		<bean:message key="button.edit"/>
	</html:link>
</div>
</logic:equal>
</logic:present>
<logic:notPresent name="component" property="infoCurriculum">
<bean:define id="infoCurricularCourse" name="component" property="infoCurricularCourse"/>
<bean:define id="curricularCourseId" name="infoCurricularCourse" property="idInternal"/>
<bean:message key="message.curriculum.notAvailable"/>
<br/>
<br/>
<br/>
<logic:equal name="infoCurricularCourse" property="basic" value="true">
<div class="gen-button">
	<html:link page="<%= "/curriculumManager.do?method=prepareInsertCurriculum&index=" + pageContext.findAttribute("curricularCourseId")%>">
		<bean:message key="button.insert"/>
	</html:link>
</div>
</logic:equal>
</logic:notPresent>