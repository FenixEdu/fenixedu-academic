<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2>Consulta de Corpo Docente Disciplina</h2>
<logic:present name="detailedProfessorShipsListofLists">
    <table width="90%" class="tab_altrow" >
    <logic:iterate id="detailedProfessorShipsList" name="detailedProfessorShipsListofLists" indexId="i">
      
    
          <tr>
        <td >
         <logic:iterate id="detailedProfessorship" name="detailedProfessorShipsList" length="1">
           
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoExecutionCourse.nome"/>
           
          </logic:iterate> 
           </td>
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