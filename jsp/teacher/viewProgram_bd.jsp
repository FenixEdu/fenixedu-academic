<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
<p><span class="error"><html:errors/></span></p>
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
	<bean:define id="infoCurricularCourses" name="component" property="infoCurricularCourses"/>	
	<logic:iterate id="infoCurricularCourse" name="infoCurricularCourses">
			<h3><bean:write name="infoCurricularCourse" property="name"/> -- <bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/></h3>
			<blockquote>
			<bean:define id="curricularCourseId" name="infoCurricularCourse" property="idInternal"/>
									
			<logic:iterate id="infoCurriculum" name="infoCurriculums">
				<bean:define id="infoCurriculumCurricularCourse" name="infoCurriculum" property="infoCurricularCourse"/>
							
				<logic:equal name="infoCurriculumCurricularCourse" property="idInternal" value="<%= curricularCourseId.toString() %>">
				<h2><bean:message key="title.program"/></h2>	
					<table>
						<tr>
							<td> 
								<bean:write name="infoCurriculum" property="program" filter="false"/>
	 						</td>
						</tr>
					</table>
					<br />
					<logic:notEmpty name="infoCurriculum" property="programEn">	
						<h2><bean:message key="title.program.eng"/></h2>
						<table>	
							<tr>
								<td> 
									<bean:write name="infoCurriculum" property="programEn" filter="false"/>
	 							</td>
							</tr>
						</table>
						<br/>	
					</logic:notEmpty>
				</logic:equal>

			</logic:iterate>
		
			<logic:equal name="infoCurricularCourse" property="basic" value="false">
				<div class="gen-button">
					<html:link page="<%= "/programManagerDA.do?method=prepareEditProgram&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;curricularCourseCode="+ pageContext.findAttribute("curricularCourseId")%>">
						<bean:message key="button.edit"/>
					</html:link>
				</div>
			</logic:equal>
			</blockquote>				
			<br />
			<br />			
			<br />			
		</logic:iterate>
</logic:present>
