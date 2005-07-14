package net.sourceforge.fenixedu.domain;

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
            String assiduidade, Integer quem, Date quando) {
        this.setIdInternal(codigoInterno);
        this.setSigla(sigla);
        this.setDesignacao(designacao);
        this.setEstado(estado);
        this.setAssiduidade(assiduidade);
        this.setQuem(quem);
        this.setQuando(quando);
    }

}
