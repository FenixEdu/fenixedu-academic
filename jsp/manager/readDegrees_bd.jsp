<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.readDegrees"/></h2>
<ul style="list-style-type: square;">
<li><html:link module="/manager" page="/insertDegree.do?method=prepareInsert"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.degree"/></html:link></li>
</ul>
<br/>
<br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="degreesList" scope="request">
<logic:notEmpty name="degreesList" >
<html:form action="/deleteDegrees" method="get">

 <bean:define id="onclick">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.degrees"/>')
		  </bean:define>

<table width="100%" cellpadding="0" border="0">
	<tr>
		<th class="listClasses-header">
		</th>
		<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.code" />
		</th>
		<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.name" />
		</th>
		<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.tipoCurso" />
		</th>
	</tr>
		
		 
	<logic:iterate id="degree" name="degreesList">			
		<tr>	
			<td class="listClasses">
			
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.internalIds" property="internalIds">
			<bean:write name="degree" property="idInternal"/>
			</html:multibox>
			</td>	
			<td class="listClasses"><html:link module="/manager" page="/readDegree.do" paramId="degreeId" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="sigla"/></html:link>
			</td>			
			<td class="listClasses"><p align="left"><html:link module="/manager" page="/readDegree.do" paramId="degreeId" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="nome"/></html:link></p>
			</td>
			<bean:define id="tipoCurso" name="degree" property="tipoCurso.name"/>
			<td class="listClasses"><bean:message bundle="MANAGER_RESOURCES" name="tipoCurso" bundle="ENUMERATION_RESOURCES"/>
			</td>
	 	</tr>
	 		
	 </logic:iterate>
	
</table>

<br/>
<br/>	

  <html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='<%=onclick.toString() %>'>
  
   <bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.degrees"/>
  </html:submit>

</html:form> 
</logic:notEmpty>	 	
</logic:present>