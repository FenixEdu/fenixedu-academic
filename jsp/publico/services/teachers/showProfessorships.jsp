<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2>Consulta de Corpo Docente de Disciplina</h2>
<logic:present name="detailedProfessorShipsListofLists">
    <table width="90%" class="tab_altrow" >
    <tr>
    	<th>Disciplina</th>
    	<th>Cursos</th>
    	<th>Semestre</th>
    	<th>Corpo Docente</th>
    	
    </tr>
    <logic:iterate id="detailedProfessorShipsList" name="detailedProfessorShipsListofLists" indexId="i">
      
    
          <tr>
       
         <logic:iterate id="detailedProfessorship" name="detailedProfessorShipsList" length="1">
            <td >
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoExecutionCourse.nome"/>
            </td>
            <td >
            	<logic:iterate id="curricularCourse" name="detailedProfessorship" property="executionCourseCurricularCoursesList">
            		<bean:write name="curricularCourse" 
                    property="infoDegreeCurricularPlan.infoDegree.sigla"/>&nbsp;
            	</logic:iterate>
                
            </td>
            <td >
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoExecutionCourse.infoExecutionPeriod.name"/>
                    -
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoExecutionCourse.infoExecutionPeriod.infoExecutionYear.year"/>    
            </td>
          </logic:iterate> 
          
            <td >
            <logic:iterate id="detailedProfessorship" name="detailedProfessorShipsList">
                 
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoTeacher.infoPerson.nome"/> &nbsp;
                <logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
                  (responsável)
                </logic:equal>
                <br/>
            </logic:iterate>
          </td>  
          
      </tr>
    </logic:iterate>
    </table>
    
</logic:present>