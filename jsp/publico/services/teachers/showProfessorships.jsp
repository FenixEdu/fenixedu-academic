<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2>Consulta dos Responsáveis por Disciplina</h2>
<logic:present name="detailedProfessorShipsListofLists">
    <table width="90%" >
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
                 <logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
                   <strong>
                </logic:equal>
                <bean:write name="detailedProfessorship" 
                    property="infoProfessorship.infoTeacher.infoPerson.nome"/> &nbsp;
                <logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
                   </strong>
                </logic:equal>
                <br/>
            </logic:iterate>
          </td>  
          
      </tr>
    </logic:iterate>
    </table>
    
</logic:present>