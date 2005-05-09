<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="label.manager.degrees"/></h2>
<ul style="list-style-type: square;">
<li><html:link page="/insertDegree.do?method=prepareInsert"><bean:message key="label.manager.insert.degree"/></html:link></li>
</ul>
<br>
<br>

<span class="error"><html:errors/></span>

<logic:present name="degreesList" scope="request">
<logic:notEmpty name="degreesList" >
<html:form action="/deleteDegrees" method="get">

 <bean:define id="onclick">
			return confirm('<bean:message key="message.confirm.delete.degrees"/>')
		  </bean:define>

<table width="100%" cellpadding="0" border="0">
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
		
		 
	<logic:iterate id="degree" name="degreesList">			
		<tr>	
			<td class="listClasses">
			
			<html:multibox property="internalIds">
			<bean:write name="degree" property="idInternal"/>
			</html:multibox>
			</td>	
			<td class="listClasses"><html:link page="/readDegree.do" paramId="degreeId" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="sigla"/></html:link>
			</td>			
			<td class="listClasses"><p align="left"><html:link page="/readDegree.do" paramId="degreeId" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="nome"/></html:link></p>
			</td>
			<bean:define id="tipoCurso" name="degree" property="tipoCurso.name"/>
			<td class="listClasses"><bean:message name="tipoCurso" bundle="ENUMERATION_RESOURCES"/>
			</td>
	 	</tr>
	 		
	 </logic:iterate>
	
</table>

<br>
<br>	

  <html:submit onclick='<%=onclick.toString() %>'>
  
   <bean:message key="label.manager.delete.selected.degrees"/>
  </html:submit>

</html:form> 
</logic:notEmpty>	 	
</logic:present>