
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="infoCurricularCourse" name="siteView" property="component"/>

<html:form action="/curriculumManager">
		
<h3><bean:message key="title.objectives" />	</h3>			
<table>		
	
	<tr>
		<td><h2><bean:message key="label.generalObjectives" />	</h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectives" rows="4" cols="56"  property="generalObjectives" />
		</td>
		<td> <span class="error" ><html:errors property="generalObjectives"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.generalObjectives.eng" />	</h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectivesEn" rows="4" cols="56"   property="generalObjectivesEn" />
		</td>
		<td> <span class="error" ><html:errors property="generalObjectivesEn"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.operacionalObjectives" /></h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectives" rows="4" cols="56"  property="operacionalObjectives" />
		</td>
		<td> <span class="error" ><html:errors property="operacionalObjectives"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.operacionalObjectives.eng" /></h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectivesEn" rows="4" cols="56"  property="operacionalObjectivesEn" />
		</td>
		<td> <span class="error" ><html:errors property="operacionalObjectivesEn"/></span>	
		</td>
	</tr>
</table>
<h3><bean:message key="title.program" />	</h3>	
<table>		
<tr>
		<td><h2><bean:message key="label.program" />	</h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.program" rows="4" cols="56"   property="program" />
		</td>
		<td> <span class="error" ><html:errors property="program"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.program.eng" />	</h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.programEn" rows="4" cols="56"  property="programEn" />
		</td>
		<td> <span class="error" ><html:errors property="programEn"/></span>	
		</td>
	</tr>
</table>	
<h3>	
<table>
	<tr>
		
		<td> 
		
		<bean:define id="curricularCourseId" name="infoCurricularCourse" property="externalId"/>
		<bean:define id="degreeCurricularPlanId" name="infoCurricularCourse" property="infoDegreeCurricularPlan.externalId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.index" property="index" value="<%= degreeCurricularPlanId.toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= curricularCourseId.toString() %>"/>
			 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertCurriculum"/>
    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" >
	<bean:message key="button.save"/>
	</html:submit>
		</td>
		<td>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
          <bean:message key="label.clear"/>
    </html:reset>
		</td>
	</tr>
</table></h3>	
</html:form>