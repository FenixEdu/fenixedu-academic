package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidade extends StatusAssiduidade_Base {

    public StatusAssiduidade() {
        setIdInternal(0);
        setEstado("inactivo");
        setAssiduidade("false");
	    setQuem(0);
    }

    public StatusAssiduidade(Integer codigoInterno, String sigla, String designacao, String estado,
            String assiduidade) {
        setIdInternal(codigoInterno);
        setSigla(sigla);
        setDesignacao(designacao);
        setEstado(estado);
        setAssiduidade(assiduidade);
        setQuem(0);
    }

    public StatusAssiduidade(Integer codigoInterno, String sigla, String designacao, String estado,
            String assiduidade, Integer quem, Timestamp quando) {
        setIdInternal(codigoInterno);
        setSigla(sigla);
        setDesignacao(designacao);
        setEstado(estado);
        setAssiduidade(assiduidade);
        setQuem(quem);
        setQuando(quando);
    }

    public Timestamp getQuando() {
        if (this.getWhen() != null) {
            return new Timestamp(this.getWhen().getTime());
        }
        return null;
    }

    public void setQuando(Timestamp quando) {
        if (quando != null) {
            this.setWhen(new Date(quando.getTime()));
        } else {
            this.setWhen(null);
        }
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
