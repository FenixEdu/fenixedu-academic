package ServidorApresentacao.formbeans.person;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Fernanda Quitério & Tania Pousao
 */
public final class AlterarPasswordForm extends ActionForm {
    //private String _username = null;
    private String _passwordAntiga = null;

    private String _passwordNova = null;

    private String _passwordNovaConfirmacao = null;

    /*
     * public String getUsername(){ return _username; } public void
     * setUsername(String username){ _username = username; }
     */

    public String getPasswordAntiga() {
        return _passwordAntiga;
    }

    public void setPasswordAntiga(String passwordAntiga) {
        _passwordAntiga = passwordAntiga;
    }

    public String getPasswordNova() {
        return _passwordNova;
    }

    public void setPasswordNova(String passwordNova) {
        _passwordNova = passwordNova;
    }

    public String getPasswordNovaConfirmacao() {
        return _passwordNovaConfirmacao;
    }

    public void setPasswordNovaConfirmacao(String passwordNovaConfirmacao) {
        _passwordNovaConfirmacao = passwordNovaConfirmacao;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        //_username = "";
        _passwordAntiga = "";
        _passwordNova = "";
        _passwordNovaConfirmacao = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        /*
         * if ((_username == null) || (_username.length() < 1)){
         * errors.add("username", new
         * ActionError("error.username.obrigatorio")); } else
         */if ((_passwordAntiga == null) || (_passwordAntiga.length() < 1)) {
            errors.add("password", new ActionError("error.password.obrigatoria"));
        } else if ((_passwordNova == null) || (_passwordNova.length() < 1)) {
            errors.add("password", new ActionError("error.password.obrigatoria"));
        } else if ((_passwordNovaConfirmacao == null) || (_passwordNovaConfirmacao.length() < 1)) {
            errors.add("password", new ActionError("error.password.obrigatoria"));
        } else if (!_passwordNova.equals(_passwordNovaConfirmacao)) {
            errors.add("password", new ActionError("error.password.diferentes"));
        }
        return errors;
    }
}