<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<bean:define id="seminary" scope="request" name="seminary"/>
<bean:define id="equivalencies" scope="page" name="seminary" property="equivalencies"/>
<logic:notEmpty name="equivalencies">
	<logic:present name="seminary" >
    	<h2><bean:write name="seminary" property="name"/></h2>
    	<br />
    		<span class="error">ATENÇÃO: As inscrições foram adiadas até à próxima sexta-feira dia 26 de Setembro 2003.</span>
    	<br />
    	<h3><bean:message key="label.seminariesEquivalencies"/></h3>
    	<table width="90%" align="center">
    	<tr>
    		<td class="listClasses-header" ><bean:message key="label.candidacyCurricularCourseTitle"/></td>
    		<td class="listClasses-header" ><bean:message key="label.modalityTitle" /></td>
    		<td class="listClasses-header" ><bean:message key="label.themesTitle" /></td>
    		<td class="listClasses-header" ><bean:message key="label.enroll" /></td>
    	</tr>	
    		<logic:iterate id="equivalency" name="equivalencies">
    		<tr>
    			<td class="listClasses"><bean:write name="equivalency" property="curricularCourse.name"/></td>	
    			<td class="listClasses"><bean:write name="equivalency" property="modality.name"/></td>		
    			<td class="listClasses">
    				<logic:iterate indexId="index_1" id="eqTheme" name="equivalency" property="themes">
						<logic:notEqual name="index_1" value="0">
							,
						</logic:notEqual>
    					<bean:write name="eqTheme" property="shortName"/>
    				</logic:iterate>
    			</td>
    			<td class="listClasses">
    				<html:link page="/fillCandidacy.do" 
    						   paramName="equivalency" 
    						   paramProperty="idInternal" 
    						   paramId="objectCode">
    								<bean:message key="label.seminaryEnroll" />
    				</html:link>
    			</td>
    		</tr>
    	</logic:iterate>
    	</table>
    </logic:present>
    <br/>
    <br/>
    <html:errors/>
 </logic:notEmpty>
 <logic:empty name="equivalencies">
 	<span class="error">Não existem seminários disponíveis para a sua licenciatura.</span>
 </logic:empty>