<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<logic:present role="PERSON">
<bean:define id="person" name="<%=pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE%>" property="person"/>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.person.title.photoHistory"/></h2>

<p>
    <span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>


<logic:present name="history">

	<logic:empty name="history">
		<div class="infoop6">
			<bean:message key="message.person.photoHistory.empty" bundle="APPLICATION_RESOURCES"/>
		</div>
		<p>
			<span class="mtop1 mbottom0">
				<html:link page="/uploadPhoto.do?method=prepare">
					<bean:message key="link.person.upload.photo" bundle="APPLICATION_RESOURCES" />
				</html:link>
			</span>
		</p>
    </logic:empty>
    
	<logic:notEmpty name="history">
		<logic:iterate id="photo" name="history">
			<bean:define id="photoId" name="photo" property="externalId"/>
        	<table class="dinline">
        		<tr>
		    		<td>
						<html:img align="middle"
							src="<%=request.getContextPath() + "/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoId %>"
							altKey="personPhoto" bundle="IMAGE_RESOURCES"
							style="border: 1px solid #aaa; padding: 3px;" />
		       		</td>
	        	</tr>
	        	<tr>
		    		<td>
			    	    <fr:view name="photo" property="state"/><br/>
                    	<logic:notEmpty name="photo" property="stateChange">
                    		<fr:view name="photo" property="stateChange" layout="no-time" />
                 	   </logic:notEmpty>
		    		</td>
	    		</tr>
      		</table>

		</logic:iterate>
	</logic:notEmpty>
	
	<p class="mtop2">
		<html:link page="/photoHistory.do?method=backToShowInformation">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
	
</logic:present>

</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>