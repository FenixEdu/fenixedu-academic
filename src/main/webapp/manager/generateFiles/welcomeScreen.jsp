<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.generateFiles"/></h2>

<table>
	<tr>
		<td class="infoop">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.generateFiles.welcome" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>		
		</td>
	</tr>
</table>