<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>



<h2><bean:message key="label.manager.findPerson" /></h2>
<br />
<span class="error"><html:errors/></span>
  
<html:form action="/preparePerson" >
	<html:hidden property="method" value="preparePerson" />
	<html:hidden property="startIndex" value="0" />

	
	<table>
		<tr>
			<td>
				<bean:message key="label.type"/>:
			</td>
			<td>	
			   	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.RoleType" bundle="ENUMERATION_RESOURCES" includedFields="STUDENT,TEACHER,GRANT_OWNER,EMPLOYEE" />
				
				<html:select property="roleType" onchange="this.form.submit()">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				<html:hidden property="roleType" name="findPersonForm"/>
				</html:select>
				
			</td>
		</tr>
		<logic:present name="degreeType">
		<tr>
		<td>
			<bean:message key="label.degree"/>:
		</td>
		<td>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"  />
				<html:select property="degreeType" onchange="this.form.submit()">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>				
				</html:select>
		</td>
		</tr>
</logic:present>
		
 </table>
</html:form>

<html:form action="/findPerson" >
<html:hidden property="method" value="findPerson" />
<html:hidden property="startIndex" value="0" />
<html:hidden property="page" value="1" />
<bean:define id="roleType" name="findPersonForm" property="roleType" type="java.lang.String"/>
<html:hidden property="roleType" value="<%= roleType %>"/>
<bean:define id="degreeType" name="findPersonForm" property="degreeType" type="java.lang.String"/>
<html:hidden property="degreeType" value="<%= degreeType %>"/>

<table>

<logic:present name="departments">
		<tr>
		<td>
			<bean:message key="label.teacher.finalWork.department"/>:
		</td>
		<td>
			<html:select property="departmentId">	
			<html:option key="dropDown.Default" value=""/>
				<logic:iterate id="department" name="departments" > 
			   	<bean:define id="departmentID" name="department" property="idInternal"/>
					<html:option value="<%= departmentID.toString() %>"> <bean:write name="department" 
						property="realName"/> 
					</html:option>
					
			  </logic:iterate>
			</html:select>
		</td>
		</tr>
</logic:present>
<logic:present name="nonMasterDegree">
		<tr>
		<td>
			<bean:message key="link.degree.nonMaster"/>:
		</td>
		<td>
			<html:select property="degreeId">	
				<html:option key="dropDown.Default" value=""/>
				<logic:iterate id="degree" name="nonMasterDegree" > 
			   	<bean:define id="degreeID" name="degree" property="idInternal"/>
			   		
					<html:option value="<%= degreeID.toString() %>"> <bean:write name="degree" 
						property="nome"/> 
					</html:option>
					
			  </logic:iterate>
			</html:select>
		</td>
		</tr>
</logic:present>
<logic:notPresent name="nonMasterDegree">
<html:hidden property="degreeId" value=""/>
</logic:notPresent>
<logic:notPresent name="departments">
<html:hidden property="departmentId" value=""/>
</logic:notPresent>
	<tr>
		<td colspan="2" class="infoop">
			<bean:message key="info.person.findPerson"/>
		</td>		
	</tr>
	
	
	
		
	<tr>
		<td>
			<bean:message key="label.viewPhoto" />
		</td>
		<td>
			<html:checkbox  property="viewPhoto" />
		</td>
	</tr>
	
	
	<tr>
		<td>
			<bean:message key="label.nameWord" />
		</td>
		<td>
			<html:text property="name" size="50"/>
		</td>		
	</tr>
	
</table>

<html:submit styleClass="inputbutton">
	<bean:message key="button.search"/>
</html:submit>
<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>	
</html:form>