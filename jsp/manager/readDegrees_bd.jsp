<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="Lista de licenciaturas" scope="request">
<logic:notEmpty name="Lista de licenciaturas" >
		<h2><bean:message key="label.manager.degrees"/></h2>
<ul style="list-style-type: square;">
<li><html:link page="/insertDegree.do?method=prepareInsert"><bean:message key="label.manager.insert.degree"/></html:link></li>
</ul>
<br>
<br>
<html:form action="/deleteDegrees" method="get">

<table width="50%" cellpadding="0" border="0">
	<tr>
		<td class="listClasses-header">
		</td>
		<td class="listClasses-header"><bean:message key="label.manager.degree.code" />
		</td>
		<td class="listClasses-header"><bean:message key="label.manager.degree.name" />
		</td>
		<td class="listClasses-header"><bean:message key="label.manager.degree.tipoCurso" />
		</td>
	</tr>
		 
	<logic:iterate id="degree" name="Lista de licenciaturas">			
		<tr>	
			<td class="listClasses">
			<html:multibox property="internalIds">
			<bean:write name="degree" property="idInternal"/>
			</html:multibox>
			</td>	
			<td class="listClasses"><html:link page="/readDegree.do" paramId="degreeId" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="sigla"/></html:link>
			</td>			
			<td class="listClasses"><html:link page="/readDegree.do" paramId="degreeId" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="nome"/></html:link>
			</td>
			<bean:define id="tipoCurso" name="degree" property="tipoCurso"/>
			<td class="listClasses"><%= tipoCurso.toString() %>
			</td>
	 	</tr>	
	 </logic:iterate>
	<span class="error"><html:errors/></span>		
</table>

<br>
<br>	
<html:submit><bean:message key="label.manager.delete.selected.degrees"/></html:submit>
</html:form> 
</logic:notEmpty>	 	
</logic:present>