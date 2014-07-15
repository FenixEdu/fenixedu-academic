<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<div class="modal-dialog modal-lg">
    <div class="modal-content">
        <form id="supportForm" class="form-horizontal">
        <input type="hidden" name="userAgent" value="${pageContext.request.getHeader('User-Agent')}" />
        <input type="hidden" name="referer" value="${pageContext.request.getHeader('Referer')}" />
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">${fr:message('resources.FenixIstResources', 'label.support.form')}</h4>
        </div>
        <div class="modal-body form-body">
            ${fr:message('resources.FenixIstResources', 'label.support.form.welcome')}
            <hr />
                <div class="form-group">
                    <label for="subject" class="col-sm-2 control-label">${fr:message('resources.FenixIstResources', 'label.error.page.subject')}:</label>
                    <div class="col-sm-10">
                        <input type="text" name="subject" id="subject" class="form-control" required />
                    </div>
                </div>
                <div class="form-group">
                    <label for="description" class="col-sm-2 control-label">${fr:message('resources.FenixIstResources', 'label.error.page.description')}:</label>
                    <div class="col-sm-10">
                        <textarea id="description" name="description" rows="5" style="width: 100%" required></textarea>
                        <small>${fr:message('resources.FenixIstResources', 'label.error.page.help')}</small>
                    </div>
                </div>
                <div class="form-group">
                    <label for="type" class="col-sm-2 control-label">${fr:message('resources.FenixIstResources', 'label.support.form.type')}:</label>
                    <div class="col-sm-10">
                        <label class="radio-inline">
                            <input type="radio" name="type" id="type-error" value="error" checked>
                            ${fr:message('resources.FenixIstResources', 'label.support.form.type.error')}
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="type" id="type-request" value="request" required>
                            ${fr:message('resources.FenixIstResources', 'label.support.form.type.request')}
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="type" id="type-question" value="question">
                            ${fr:message('resources.FenixIstResources', 'label.support.form.type.question')}
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="attachment" class="col-sm-2 control-label">${fr:message('resources.FenixIstResources', 'label.support.form.attachment')}:</label>
                    <div class="col-sm-10">
                        <div class="alert alert-danger" id="largeFile" style="display: none">
                            ${fr:message('resources.FenixIstResources', 'label.support.form.attachment.file.too.large')}
                        </div>
                        <input type="file" name="attachment" id="attachment" />
                        <small>${fr:message('resources.FenixIstResources', 'label.support.form.attachment.help')}</small>
                    </div>
                </div>
        </div>
        <div class="modal-body success text-center hide">
            <h3>${fr:message('resources.FenixIstResources', 'message.error.page.submitted')}</h3>
            ${fr:message('resources.FenixIstResources', 'message.error.page.submitted.body')} <a href="mailto:dsi@tecnico.ulisboa.pt">dsi@tecnico.ulisboa.pt</a>.
        </div>
        <div class="modal-footer">
            <button type="submit" class="btn btn-primary">${fr:message('resources.FenixIstResources', 'label.error.page.submit')}</button>
        </div>
        </form>
    </div>
</div>

<script>
var data = { 'functionality': window.current$functionality };
$.ajaxSetup({ headers: {'Content-Type':'application/json; charset=UTF-8'} });
$('#supportForm input[type=file]').on('change', function (event) {
    var file = event.target.files[0]; $('#largeFile').hide();
    if(file.size > 5 * 1024 * 1024) { $('#largeFile').show(); return; }
    var reader = new FileReader();
    data['fileName'] = file.name; data['mimeType'] = file.type;
    reader.onload = function (e) {
        var content = e.target.result;
        data['attachment'] = content.substr(content.indexOf(",") + 1, content.length);
    };
    reader.readAsDataURL(file);
});
$('#supportForm').on('submit', function(event) {
    event.preventDefault(); var target = $(event.target); $.map(target.serializeArray(), function(n, i){ data[n['name']] = n['value']; });
    target.find('button[type=submit]').html("${fr:message('resources.FenixIstResources', 'label.error.page.submitting')}...");
    target.find('button[type=submit]').attr('disabled', true);
    $.post('${pageContext.request.contextPath}/api/fenix-ist/support-form', JSON.stringify(data), function () {
        target.find('.success').removeClass('hide'); target.find('.modal-footer').hide(); target.find('.form-body').hide();
    });
});
</script>