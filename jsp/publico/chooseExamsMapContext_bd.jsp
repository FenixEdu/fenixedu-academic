<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<span class="error"><html:errors/></span>
<html:form action="/chooseExamsMapContextDA" method="GET">
	<html:hidden  property="ePName" value="<%= pageContext.findAttribute("ePName").toString() %>" />
	<html:hidden  property="eYName" value="<%= pageContext.findAttribute("eYName").toString() %>" /> 
	
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="choose"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    	<tr>
        	<td bgcolor="#FFFFFF" class="infoop"><bean:message key="message.public.degree.choose"/></td>
        </tr>
    </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
	<html:select property="index" size="1">
    	<html:options collection="degreeList" property="value" labelProperty="label"/>
    </html:select>
	</p>
	<br />
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    	<tr>
        	<td bgcolor="#FFFFFF" class="infoop"><bean:message key="label.select.curricularYears" /></td>
        </tr>
    </table>
	<br />
	<br />   
   	<bean:message key="property.context.curricular.year"/>:<br/>
	<logic:present name="curricularYearList" >
		<logic:iterate id="item" name="curricularYearList">
			<html:multibox property="selectedCurricularYears">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/><bean:message key="label.exam.year"/><br/>
		</logic:iterate>
		<html:checkbox property="selectAllCurricularYears">
			<bean:message key="checkbox.show.all"/><br/>
		</html:checkbox>
	</logic:present>
	<br/>
   <p><html:submit value="Submeter" styleClass="inputbutton">
   		<bean:message key="label.next"/>
   </html:submit></p>
</html:form>
