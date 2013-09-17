<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<h2>Fusaaaao de Objectos</h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/mergeObjects" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseObjects" />
	
	<table>
		<tr>
			<td colspan="2">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.domainObjectClass" property="classToMerge" >
					<html:options collection="domainClasses" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				OID 1:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.person1Username" property="object1ExternalId" size="25"/>
			</td>		
		</tr>
		<tr>
			<td>
				OID 2:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.person2Username" property="object2ExternalId" size="25"/>
			</td>		
		</tr>	
		<tr>
			<td>
				&nbsp;
			</td>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" key="button.next"/>
				</html:submit>
			</td>		
		</tr>
	</table>

</html:form>