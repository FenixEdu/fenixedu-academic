package Dominio;

/**
 * @author jorge
 */
public class Privilegio implements IPrivilegio {
    protected IPessoa _pessoa;
    protected String _servico;
    
    // códigos internos da base de dados
    protected int _codigoInterno;
    protected int _chavePessoa;

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public Privilegio() { };

    public Privilegio(IPessoa pessoa, String servico) {
        setPessoa(pessoa);
        setServico(servico);
    }

    public int getCodigoInterno() {
        return _codigoInterno;
    }
    
    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public int getChavePessoa() {
        return _chavePessoa;
    }
    
    public void setChavePessoa(int chavePessoa) {
        _chavePessoa = chavePessoa;
    }

    public IPessoa getPessoa() {
        return _pessoa;
    }

    public String getServico() {
        return _servico;
    }

    public void setPessoa(IPessoa pessoa) {
        _pessoa = pessoa;
    }

    public void setServico(String servico) {
        _servico = servico;
    }

    public int hashCode() {
        return _servico.hashCode();
    }

    public boolean equals(Object obj) {
        boolean result = false;
    
        if (obj instanceof IPrivilegio) {
            IPrivilegio privilegio = (IPrivilegio) obj;
            
            result = getPessoa().equals(privilegio.getPessoa()) &&
                     getServico().equals(privilegio.getServico());
        }
        
        return result;
    }
    
    public String toString() {
        return _servico;
    }

}
