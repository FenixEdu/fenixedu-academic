<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!--h2><bean:message key="label.teachers.search"/></h2-->

<h1 align="center">
	<%= request.getAttribute("searchDetails").toString() %>
</h1>
<h2 align="center">
	<bean:message key="label.teachers.search"/>
	-
	<%= request.getAttribute("searchType").toString() %>
</h2>
<h2 align="center">
	<%= request.getAttribute("searchTarget").toString() %>
</h2>
	<br>

<logic:notPresent name="detailedProfessorShipsListofLists">
	<table>
		<td bgcolor="#FFFFCC">
			<font color="#CC0000">
				<b><bean:message key="message.public.notfound.professorships"/></b>
			</font>
		</td>
	</table>
	<br>
</logic:notPresent>

	
		
<logic:present name="detailedProfessorShipsListofLists">
    <table width="90%" class="invisible">
    <tr>
	<td class="box_header"><b>Disciplina</b></td>
		<td class="box_header"><b>Cursos</b></td>
		<td class="box_header"><b>Semestre</b></td>
		<td class="box_header"><b>Corpo Docente</b></td>
    </tr>
    <logic:iterate id="detailedProfessorShipsList" name="detailedProfessorShipsListofLists" indexId="i">
      
    
          <tr>
       
         <logic:iterate id="detailedProfessorship" name="detailedProfessorShipsList" length="1">
            <td class="box_cell">
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoExecutionCourse.nome"/>
            </td>
            <td class="box_cell">
            	<logic:iterate id="curricularCourse" name="detailedProfessorship" property="executionCourseCurricularCoursesList">
            		<bean:write name="curricularCourse" 
                    property="infoDegreeCurricularPlan.infoDegree.sigla"/>&nbsp;
            	</logic:iterate>
                
            </td>
            <td class="box_cell">
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoExecutionCourse.infoExecutionPeriod.name"/>
                    -
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoExecutionCourse.infoExecutionPeriod.infoExecutionYear.year"/>    
            </td>
          </logic:iterate> 
          
            <td class="box_cell">
            <logic:iterate id="detailedProfessorship" name="detailedProfessorShipsList">
                 
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoTeacher.infoPerson.nome"/> &nbsp;
                <logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
                  (respons&aacute;vel)
                </logic:equal>
                <br/>
            </logic:iterate>
          </td>  
          
      </tr>
    </logic:iterate>
    </table>
    
</logic:present>