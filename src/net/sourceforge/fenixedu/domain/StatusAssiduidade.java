package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidade extends StatusAssiduidade_Base {
    private Timestamp _quando;

    public StatusAssiduidade() {
        setIdInternal(0);
        setEstado("inactivo");
        setAssiduidade("false");
	    setQuem(0);
    }

    public StatusAssiduidade(String sigla, String designacao, String estado, String assiduidade) {
        setIdInternal(0);
        setSigla(sigla);
        setDesignacao(designacao);
        setEstado(estado);
        setAssiduidade(assiduidade);
        setQuem(0);
    }

    public StatusAssiduidade(String sigla, String designacao, String estado, String assiduidade,
            int quem, Timestamp quando) {
        setIdInternal(0);
        setSigla(sigla);
        setDesignacao(designacao);
        setEstado(estado);
        setAssiduidade(assiduidade);
        setQuem(quem);
        setQuando(quando);
    }

    public StatusAssiduidade(int codigoInterno, String sigla, String designacao, String estado,
            String assiduidade) {
        setIdInternal(codigoInterno);
        setSigla(sigla);
        setDesignacao(designacao);
        setEstado(estado);
        setAssiduidade(assiduidade);
        setQuem(0);
    }

    public StatusAssiduidade(int codigoInterno, String sigla, String designacao, String estado,
            String assiduidade, int quem, Timestamp quando) {
        setIdInternal(codigoInterno);
        setSigla(sigla);
        setDesignacao(designacao);
        setEstado(estado);
        setAssiduidade(assiduidade);
        setQuem(quem);
        setQuando(quando);
    }

    public Timestamp getQuando() {
        return _quando;
    }

    public void setQuando(Timestamp quando) {
        _quando = quando;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof StatusAssiduidade) {
            StatusAssiduidade status = (StatusAssiduidade) obj;

            resultado = (this.getIdInternal() == status.getIdInternal()
                    && this.getSigla() == status.getSigla()
                    && this.getDesignacao() == status.getDesignacao()
                    && this.getEstado() == status.getEstado() && this.getAssiduidade() == status
                    .getAssiduidade());
        }
        return resultado;
    }

}