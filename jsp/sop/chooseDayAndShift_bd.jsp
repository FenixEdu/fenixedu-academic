<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.exams"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/chooseDayAndShiftForm">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.nextPage" property="nextPage" value='<%= pageContext.findAttribute(SessionConstants.NEXT_PAGE).toString() %>'/>

		<table width="100%">
	    	<tr>
	            <td class="infoop">Por favor, proceda &agrave; escolha do dia pretendido.</td>
	         </tr>
	   	</table>
		<br />
		<table cellpadding="0" cellspacing="0">
           	<tr>
                <td width="50"><bean:message key="property.exam.year"/>:</td>
                <td width="100"><html:select bundle="HTMLALT_RESOURCES" altKey="select.year" property="year">
                    	<option value="" selected="selected"></option>
                    	<html:options name="<%= SessionConstants.LABLELIST_YEARS %>"/>
                   	</html:select>
               	</td>
               	<td width="50"><bean:message key="property.exam.month"/>:</td>
               	<td width="125"><html:select bundle="HTMLALT_RESOURCES" altKey="select.month" property="month">
                       <option value="" selected="selected"></option>
                       <html:options collection="<%= SessionConstants.LABLELIST_MONTHSOFYEAR %>" property="value" labelProperty="label"/>
                  	</html:select>
               	</td>
               	<td width="50"><bean:message key="property.exam.day"/>:</td>
               	<td width="105"><html:select bundle="HTMLALT_RESOURCES" altKey="select.day" property="day">
                       <option value="" selected="selected"></option>
                       <html:options name="<%= SessionConstants.LABLELIST_DAYSOFMONTH %>"/>
                  	</html:select>
             	</td>
          	</tr>
		</table>
       	<br />
       	
       	<table width="100%">
	   		<tr>
	        	<td class="infoop">Escolha o turno de exame pretendido.</td>
	        </tr>
	    </table>
	    <br />
	    <table>
	    	<tr>
            	<td width="50"><bean:message key="property.exam.beginning"/>:</td>
                <td>
                    <html:select bundle="HTMLALT_RESOURCES" altKey="select.beginning" property="beginning">
                    	<option value="" selected="selected"></option>                        
                        <html:options name="<%= SessionConstants.LABLELIST_HOURS %>"/>
                    </html:select>
                </td>
            </tr>
      	</table>
        <br />

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="lable.choose"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
</html:form>