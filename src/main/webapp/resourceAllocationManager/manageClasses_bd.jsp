<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2>Manipular Turmas</h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>

<jsp:include page="context.jsp"/>


<html:form action="/manageClasses" focus="className">

    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value= "1"/>

	<p class="mbottom15">
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

   	<bean:define id="degree" type="net.sourceforge.fenixedu.domain.Degree" name="executionDegreeD" property="degreeCurricularPlan.degree"/>
   	<bean:define id="curricularYear" type="java.lang.Integer" name="curricularYearOID"/>
   	<%= degree.constructSchoolClassPrefix(curricularYear) %>


	   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.className" property="className"/>
	   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
	   		<bean:message key="label.create"/>
	   	</html:submit>


</html:form>


<logic:present name="<%= PresentationConstants.CLASSES %>" scope="request">
  <html:form action="/deleteClasses">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteClasses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

<table class="tstyle2 mtop15">
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.name"/>
		</th>
		<th>
			<bean:message key="label.delete"/>
		</th>				
	</tr>		
	<bean:define id="deleteConfirm">
		return confirm('<bean:message key="message.confirm.delete.class"/>')
	</bean:define>
	<logic:iterate id="classView" name="<%= PresentationConstants.CLASSES %>" scope="request">
		<bean:define id="classOID"
					 type="java.lang.String"
					 name="classView"
					 property="externalId"/>
		<tr>
       		<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
					<bean:write name="classView" property="externalId"/>
				</html:multibox>
			</td>
			<td>
				<html:link page="<%= "/manageClass.do?method=prepare&amp;"
						+ PresentationConstants.CLASS_VIEW_OID
						+ "="
						+ pageContext.findAttribute("classOID")
						+ "&amp;"
						+ PresentationConstants.ACADEMIC_INTERVAL
						+ "="
						+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
						+ "&amp;"
						+ PresentationConstants.CURRICULAR_YEAR_OID
						+ "="
						+ pageContext.findAttribute("curricularYearOID")
						+ "&amp;"
						+ PresentationConstants.EXECUTION_DEGREE_OID
						+ "="
						+ pageContext.findAttribute("executionDegreeOID") %>">
					<span style="align: center;">
						<jsp:getProperty name="classView" property="nome" />
					</span>
				</html:link>
			</td>
			<td>
				<div align="center">
					<html:link page="<%= "/manageClasses.do?method=delete&amp;"
							+ PresentationConstants.CLASS_VIEW_OID
						  	+ "="
						  	+ pageContext.findAttribute("classOID")
						  	+ "&amp;"
							+ PresentationConstants.ACADEMIC_INTERVAL
						  	+ "="
						  	+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
						  	+ "&amp;"
						  	+ PresentationConstants.CURRICULAR_YEAR_OID
							+ "="
						  	+ pageContext.findAttribute("curricularYearOID")
						  	+ "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID
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
<logic:notPresent name="<%= PresentationConstants.CLASSES %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="listClasses.emptyClasses"/></span>
</logic:notPresent>