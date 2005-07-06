package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidade extends StatusAssiduidade_Base {

    public StatusAssiduidade() {
        this.setIdInternal(0);
        this.setEstado("inactivo");
        this.setAssiduidade("false");
        this.setQuem(0);
    }

    public StatusAssiduidade(Integer codigoInterno, String sigla, String designacao, String estado,
            String assiduidade) {
        this.setIdInternal(codigoInterno);
        this.setSigla(sigla);
        this.setDesignacao(designacao);
        this.setEstado(estado);
        this.setAssiduidade(assiduidade);
        this.setQuem(0);
    }

    public StatusAssiduidade(Integer codigoInterno, String sigla, String designacao, String estado,
            String assiduidade, Integer quem, Timestamp quando) {
        this.setIdInternal(codigoInterno);
        this.setSigla(sigla);
        this.setDesignacao(designacao);
        this.setEstado(estado);
        this.setAssiduidade(assiduidade);
        this.setQuem(quem);
        this.setQuando(quando);
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
        if (obj instanceof IStatusAssiduidade) {
            final IStatusAssiduidade statusAssiduidade = (IStatusAssiduidade) obj;
            return this.getIdInternal().equals(statusAssiduidade.getIdInternal());
        }
        return false;
    }

}
