package ServidorApresentacao.formbeans.assiduousness;

import java.util.List;

import org.apache.struts.action.ActionForm;

public final class MostrarListaForm extends ActionForm {

    private List headers = null;

    private List body = null;

    private List headers2 = null;

    private List body2 = null;

    private List headers3 = null;

    private List body3 = null;

    public List getHeaders() {
        return headers;
    }

    public List getBody() {
        return body;
    }

    public void setHeaders(List headers) {
        this.headers = headers;
    }

    public void setBody(List body) {
        this.body = body;
    }

    public List getHeaders2() {
        return headers2;
    }

    public List getBody2() {
        return body2;
    }

    public void setHeaders2(List headers2) {
        this.headers2 = headers2;
    }

    public void setBody2(List body2) {
        this.body2 = body2;
    }

    public List getHeaders3() {
        return headers3;
    }

    public List getBody3() {
        return body3;
    }

    public void setHeaders3(List headers3) {
        this.headers3 = headers3;
    }

    public void setBody3(List body3) {
        this.body3 = body3;
    }
}