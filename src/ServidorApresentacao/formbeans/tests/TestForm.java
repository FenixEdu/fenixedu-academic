package ServidorApresentacao.formbeans.tests;

import org.apache.struts.action.ActionForm;

import Util.tests.Response;

public final class TestForm extends ActionForm {

    public TestForm() {
        super();
    }

    private Response[] question;

    public Response[] getQuestion() {
        return question;
    }

    public void setQuestion(Response[] op) {
        this.question = op;
    }
}