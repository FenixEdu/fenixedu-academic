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
	
<%--
	<logic:notEqual name="component" property="size" value="0">
		<logic:iterate id="program" name="infoCurriculums">
			<h2>
				<bean:message key="title.program"/>
			</h2>
			<h3>
				<bean:write name="program" property="infoCurricularCourse.name"/>
			</h3>
			<h3>
				<bean:write name="program" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>
			</h3>
			<table>
				<tr>
					<td>
						<bean:write name="program" property="program" filter="false"/>
					</td>
				</tr>
			</table>
			<br/>
			<logic:notEmpty name="program" property="programEn">
				<h2>
					<bean:message key="title.program.eng"/>
				</h2>
				<h3>
					<bean:write name="program" property="infoCurricularCourse.name"/>
				</h3>
				<h3>
					<bean:write name="program" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>
				</h3>
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
			<logic:equal name="program" property="infoCurricularCourse.basic" value="false">
				<div class="gen-button">
					<html:link page="<%= "/programManagerDA.do?method=prepareEditProgram&amp;objectCode=" + pageContext.findAttribute("objectCode") +"&amp;curriculumCode="+ pageContext.findAttribute("curriculumId")%>" paramId="curricularCourseCode" paramName="program" paramProperty="infoCurricularCourse.idInternal" >
						<bean:message key="button.edit"/>
					</html:link>
				</div>
			</logic:equal>
		</logic:iterate>
	</logic:notEqual>
	<logic:equal name="component" property="size" value="0">
		<bean:define id="infoCurricularCourses" name="component" property="infoCurricularCourses"/>
		<logic:iterate id="infoCurricularCourse" name="infoCurricularCourses">
			<%-- </logic:empty> --%>
<%--			<h2>
				<bean:message key="title.program"/>
			</h2>
			<h3>
				<bean:write name="infoCurricularCourse" property="name"/>
			</h3>
			<h3>
				<bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/>
			</h3>
			<br />
			<bean:define id="curricularCourseId" name="infoCurricularCourse" property="idInternal"/>
			<logic:equal name="infoCurricularCourse" property="basic" value="false">
				<div class="gen-button">
					<html:link page="<%= "/programManagerDA.do?method=prepareEditProgram&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;curricularCourseCode="+ pageContext.findAttribute("curricularCourseId")%>">
						<bean:message key="button.edit"/>
					</html:link>
				</div>
			</logic:equal>
		</logic:iterate>
	</logic:equal>
</logic:present>
--%>