<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<p>O curso seleccionado &eacute;:</p>
    		<strong><jsp:include page="context.jsp"/></strong>
		</td>
	</tr>
</table>

<br />
<h2>Manipular Turmas</h2>

<br />
<html:form action="/manageClasses" focus="className">

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value= "1"/>

	<span class="error"><!-- Error messages go here --><html:errors /></span>
   	<br />
   	<bean:define id="degree" type="net.sourceforge.fenixedu.domain.Degree" name="executionDegreeD" property="degreeCurricularPlan.degree"/>
   	<bean:define id="curricularYear" type="java.lang.Integer" name="curricularYearOID"/>
   	<%= degree.constructSchoolClassPrefix(curricularYear) %>
   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.className" property="className"/>
   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
   		<bean:message key="label.create"/>
   	</html:submit>
</html:form>

<br/>
<logic:present name="<%= SessionConstants.CLASSES %>" scope="request">
  <html:form action="/deleteClasses">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteClasses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

<table cellpadding="0" border="0">
	<tr>
		<th class="listClasses-header">
		</th>
		<th class="listClasses-header">
			<bean:message key="label.name"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.delete"/>
		</th>				
	</tr>		
	<bean:define id="deleteConfirm">
		return confirm('<bean:message key="message.confirm.delete.class"/>')
	</bean:define>
	<logic:iterate id="classView" name="<%= SessionConstants.CLASSES %>" scope="request">
		<bean:define id="classOID"
					 type="java.lang.Integer"
					 name="classView"
					 property="idInternal"/>
		<tr>
       		<td class="listClasses">
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
					<bean:write name="classView" property="idInternal"/>
				</html:multibox>
			</td>
			<td nowrap="nowrap" class="listClasses">
				<html:link page="<%= "/manageClass.do?method=prepare&amp;"
						+ SessionConstants.CLASS_VIEW_OID
						+ "="
						+ pageContext.findAttribute("classOID")
						+ "&amp;"
						+ SessionConstants.EXECUTION_PERIOD_OID
						+ "="
						+ pageContext.findAttribute("executionPeriodOID")
						+ "&amp;"
						+ SessionConstants.CURRICULAR_YEAR_OID
						+ "="
						+ pageContext.findAttribute("curricularYearOID")
						+ "&amp;"
						+ SessionConstants.EXECUTION_DEGREE_OID
						+ "="
						+ pageContext.findAttribute("executionDegreeOID") %>">
					<div align="center">
						<jsp:getProperty name="classView" property="nome" />
					</div>
				</html:link>
			</td>
			<td nowrap="nowrap" class="listClasses">
				<div align="center">
					<html:link page="<%= "/manageClasses.do?method=delete&amp;"
							+ SessionConstants.CLASS_VIEW_OID
						  	+ "="
						  	+ pageContext.findAttribute("classOID")
						  	+ "&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID
						  	+ "="
						  	+ pageContext.findAttribute("executionPeriodOID")
						  	+ "&amp;"
						  	+ SessionConstants.CURRICULAR_YEAR_OID
							+ "="
						  	+ pageContext.findAttribute("curricularYearOID")
						  	+ "&amp;"
							+ SessionConstants.EXECUTION_DEGREE_OID
						  	+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>"
								onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
						<bean:message key="label.delete"/>
					</html:link>
				</div>
			</td>
		</tr>
	</logic:iterate>
</table>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
		<bean:message key="link.delete"/>
	</html:submit>
  </html:form>
</logic:present>
<logic:notPresent name="<%= SessionConstants.CLASSES %>" scope="request">
	<span class="error"><bean:message key="listClasses.emptyClasses"/></span>
</logic:notPresent>