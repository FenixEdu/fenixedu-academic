/*
 * Created on 10/Dez/2003
 *
 */
package middleware.middlewareDomain;

public class MWDisciplinasIleec
{

    private String codigoDisciplina;
    private Integer funciona;
    private Integer idAnoCurricular;
    private Integer idAreaCientifica;
    private Integer idDisciplina;
    private Integer idSemestre;
    private Integer inscObrigatoria;
    private String nome;
    private Integer numeroCreditos;
    private Integer obrigatoria;
    private Integer tipoPrecedencia;

    public String getCodigoDisciplina()
    {
        return this.codigoDisciplina;
    }
    public void setCodigoDisciplina(String codigoDisciplina)
    {
        this.codigoDisciplina = codigoDisciplina;
    }

    public Integer getFunciona()
    {
        return this.funciona;
    }
    public void setFunciona(Integer funciona)
    {
        this.funciona = funciona;
    }

    public Integer getIdAnoCurricular()
    {
        return this.idAnoCurricular;
    }
    public void setIdAnoCurricular(Integer idAnoCurricular)
    {
        this.idAnoCurricular = idAnoCurricular;
    }

    public Integer getIdAreaCientifica()
    {
        return this.idAreaCientifica;
    }
    public void setIdAreaCientifica(Integer idAreaCientifica)
    {
        this.idAreaCientifica = idAreaCientifica;
    }

    public Integer getIdDisciplina()
    {
        return this.idDisciplina;
    }
    public void setIdDisciplina(Integer idDisciplina)
    {
        this.idDisciplina = idDisciplina;
    }

    public Integer getIdSemestre()
    {
        return this.idSemestre;
    }
    public void setIdSemestre(Integer idSemestre)
    {
        this.idSemestre = idSemestre;
    }

    public Integer getInscObrigatoria()
    {
        return this.inscObrigatoria;
    }
    public void setInscObrigatoria(Integer inscObrigatoria)
    {
        this.inscObrigatoria = inscObrigatoria;
    }

    public String getNome()
    {
        return this.nome;
    }
    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public Integer getNumeroCreditos()
    {
        return this.numeroCreditos;
    }
    public void setNumeroCreditos(Integer creditos)
    {
        this.numeroCreditos = creditos;
    }

    public Integer getObrigatoria()
    {
        return this.obrigatoria;
    }
    public void setObrigatoria(Integer obrigatoria)
    {
        this.obrigatoria = obrigatoria;
    }

    public Integer getTipoPrecedencia()
    {
        return this.tipoPrecedencia;
    }
    public void setTipoPrecedencia(Integer tipoPrecedencia)
    {
        this.tipoPrecedencia = tipoPrecedencia;
    }

    public String toString()
    {
        return " [codigoDisciplina] "
            + codigoDisciplina
            + " [funciona] "
            + funciona
            + " [idAnoCurricular] "
            + idAnoCurricular
            + " [idAreaCientifica] "
            + idAreaCientifica
            + " [idDisciplina] "
            + idDisciplina
            + " [idSemestre] "
            + idSemestre
            + " [inscObrigatoria] "
            + inscObrigatoria
            + " [nome] "
            + nome
            + " [numeroCreditos] "
            + numeroCreditos
            + " [obrigatoria] "
            + obrigatoria
            + " [tipoPrecedencia] "
            + tipoPrecedencia;

    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof MWDisciplinasIleec)
        {
            MWDisciplinasIleec mwd = (MWDisciplinasIleec) obj;
            resultado =
                (getCodigoDisciplina() != null)
                    && (getCodigoDisciplina().equals(mwd.getCodigoDisciplina()));
        }
        
        return resultado;
    }

}
