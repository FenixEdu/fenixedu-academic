<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.person1Username" property="object1IdInternal" size="25"/>
			</td>		
		</tr>
		<tr>
			<td>
				OID 2:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.person2Username" property="object2IdInternal" size="25"/>
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