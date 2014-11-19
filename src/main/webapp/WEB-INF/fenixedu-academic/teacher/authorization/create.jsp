<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}	

<spring:url var="authorizationsUrl" value="/teacher/authorizations"></spring:url>
<spring:url var="createUrl" value="/teacher/authorizations/create"></spring:url>

<div class="page-header">
	<h1>
		<spring:message code="teacher.authorizations.title" />
		<small><spring:message code="teacher.authorizations.create.title" /></small>
	</h1>
</div>
<section>
	<form:form role="form" modelAttribute="formBean" method="POST" class="form-horizontal" action="${createUrl}">
	
		<div class="form-group">
			<form:label for="selectDepartment" path="department" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.department" /></form:label>
			<div class="col-sm-11">
				<form:select path="department" id="selectDepartment" items="${departments}" class="form-control" itemLabel="nameI18n.content" itemValue="externalId"/>
			</div>
		</div>
	
		<div class="form-group">
			<form:label for="selectPeriod" path="period" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.period" /></form:label>
			<div class="col-sm-11">
				<form:select path="period" id="selectPeriod" items="${periods}" class="form-control" itemLabel="qualifiedName" itemValue="externalId"/>
			</div>
		</div>
		
		<div class="form-group">
			<form:label for="category" path="category" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.category" /></form:label>
			<div class="col-sm-11">
				<form:select items="${categories}" itemLabel="name.content" itemValue="externalId" id="category" path="category" class="form-control" />
			</div>
		</div>
		
		<div class="form-group">
			<label for="username" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.username" /></label>
			<div class="col-sm-11">
				<input id="username" class="form-control col-sm-11 user-search" required placeholder="${i18n.message('teacher.authorizations.placeholder.user')}"/>
			</div>
		</div>
		
		<div class="form-group">
			<form:label for="lessonHours" path="lessonHours" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.lessonHours" /></form:label>
			<div class="col-sm-11">
				<input id="lessonHours" name="lessonHours" class="form-control" type="number" step="any" min="0" required/>
			</div>
		</div>
		
		<div class="form-group">
			<form:label for="contracted" path="contracted" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.contracted" /></form:label>
			<div class="col-sm-11">
				<div class="checkbox">
					<form:checkbox id="contracted" path="contracted" />
				</div>
			</div>
		</div>
		
		<form:hidden path="user" class="form-control user-search"/>
		
		<div class="form-group">
			<div class="col-sm-push-1 col-sm-11">
				<a class="btn btn-default" href="${authorizationsUrl}"><spring:message code="label.cancel"/></a>
				<button type="submit" class="btn btn-primary"><spring:message code="label.create"/></button>
			</div>
		</div>
		
	</form:form>
</section>

<script type='text/javascript'>

	var example = new Bloodhound({
	    datumTokenizer: function (d) {
	        return Bloodhound.tokenizers.whitespace(d.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    limit:10,
	    remote: {
	        url: Bennu.contextPath + "/api/bennu-core/users/find",
	
	        replace: function (url, query) {
	            return url + "?query=" + query + "&maxHits=10";
	        },
	
	        ajax: {
	            beforeSend: function (jqXhr, settings) {
	                //settings.data = $.param({q: queryInput.val()});
	            },
	            type: "POST"
	
	        },
	
	        filter: function (response) {
	            return response.users;
	        }
	    }
	});
	
	example.initialize();
	
	$('.user-search').typeahead({
	    hint: true,
	    highlight: true,
	    minLength: 3,
	}, {
	    name: 'username',
	    displayKey: function(user) {
	    	return user.displayName;
	    },
	    source: example.ttAdapter(),
	    templates: {
	        empty: [
	            '<div class="empty-message">',
	            'No User Found',
	            '</div>'
	        ].join('\n'),
	        suggestion: function (x) {
	//             Bennu.group.userCache[x.username] = x;
	            return '<p><div class="row">' +
	                '<div class="col-xs-1"><img class="img-circle" src="' + x.avatar + '?s=32" alt="" /></div>' +
	                '<div class="col-sm-11">' +
	                '<div>' + x.displayName + '</div>' +
	                '<div>' + x.username + '</div>' +
	                '</div></div></p>';
	        }
	    }
	}).on("typeahead:autocompleted typeahead:selected", function (el, user, elName) {
		$("#user").val(user.id);
	});

</script>