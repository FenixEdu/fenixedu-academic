<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	
<h2><bean:message key="title.teachers"/></h2>
<table>
	<tr>
		<td width="150" class="listClasses-header"><bean:message key="label.teacherNumber"/>	
		</td>
		<td width="250" class="listClasses-header"><bean:message key="label.name"/>	
		</td>
	</tr>	
<logic:iterate id="infoTeacher" name="<%= SessionConstants.TEACHERS_LIST %>">
	<tr>
		<td class="listClasses"><bean:write name="infoTeacher"  property="teacherNumber"/>	
		</td>
		<td class="listClasses"><bean:write name="infoTeacher" property="infoPerson.nome" /> 
		</td>
<logic:equal name="<%= SessionConstants.IS_RESPONSIBLE %>" value="true">
		<td><html:link page="/teachersManagerDA.do?method=removeTeacher" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
			(<bean:message key="link.removeTeacher"/>)
			</html:link>
		</td>
</logic:equal>
	</tr>
	</logic:iterate>	
</table>
<logic:equal name="<%= SessionConstants.IS_RESPONSIBLE %>" value="true">
<table>
	<tr>
		<td><html:link page="/teacherManagerDA.do?method=prepareAssociateTeacher"><bean:message key="link.addTeacher"/>
			</html:link>
		</td>
	</tr>
</table>
</logic:equal>