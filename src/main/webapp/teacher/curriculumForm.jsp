<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
	
		
<html:form action="/curriculumManagerDA">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
<h3><bean:message key="title.objectives" />	</h3>			
<table>		
	
	<tr>
		<td><h2><bean:message key="label.generalObjectives" />	</h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectives" rows="4" cols="56" property="generalObjectives" />
		</td>
		<td> <span class="error" ><html:errors property="generalObjectives"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.generalObjectives.eng" />	</h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectivesEn" rows="4" cols="56" property="generalObjectivesEn" />
		</td>
		<td> <span class="error" ><html:errors property="generalObjectivesEn"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.operacionalObjectives" /></h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectives" rows="4" cols="56" property="operacionalObjectives" />
		</td>
		<td> <span class="error" ><html:errors property="operacionalObjectives"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.operacionalObjectives.eng" /></h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectivesEn" rows="4" cols="56" property="operacionalObjectivesEn" />
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
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.program" rows="4" cols="56" property="program" />
		</td>
		<td> <span class="error" ><html:errors property="program"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.program.eng" />	</h2>
		</td>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.programEn" rows="4" cols="56" property="programEn" />
		</td>
		<td> <span class="error" ><html:errors property="programEn"/></span>	
		</td>
	</tr>
</table>	
<h3>	
<table>
	<tr>
		<td>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
          <bean:message key="label.clear"/>
    </html:reset>
		</td>
		<td>
			
			 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertCurriculum"/>
    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" >
	<bean:message key="button.save"/>
	</html:submit>
		</td>
		
	</tr>
</table></h3>	
</html:form>