/*
 * Created on 17/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author dcs-rjao
 * 
 * 17/Jul/2003
 */
public interface IFuncionario {
    /**
     * @return Date
     */
    public abstract Date getAntiguidade();

    /**
     * @return String
     */
    public abstract String getCalendario();

    /**
     * @return int
     */
    public abstract int getChaveCCCorrespondencia();

    /**
     * @return int
     */
    public abstract int getChaveCCLocalTrabalho();

    /**
     * @return int
     */
    public abstract int getChaveCCVencimento();

    /**
     * @return int
     */
    public abstract int getChaveFuncResponsavel();

    /**
     * Getter for property chaveHorarioActual.
     * 
     * @return Value of property chaveHorarioActual.
     *  
     */
    public abstract int getChaveHorarioActual();

    /**
     * Getter for property chavePessoa.
     * 
     * @return Value of property chavePessoa.
     *  
     */
    public abstract int getChavePessoa();

    /**
     * Getter for property chaveStatus.
     * 
     * @return Value of property chaveStatus.
     *  
     */
    public abstract int getChaveStatus();

    /**
     * Getter for property codigoInterno.
     * 
     * @return Value of property codigoInterno.
     *  
     */
    public abstract int getCodigoInterno();

    /**
     * @return Date
     */
    public abstract Date getDataFim();

    /**
     * @return Date
     */
    public abstract Date getDataInicio();

    /**
     * @return
     */
    public abstract ICostCenter getMailingCostCenter();

    /**
     * Getter for property numeroMecanografico.
     * 
     * @return Value of property numeroMecanografico.
     *  
     */
    public abstract int getNumeroMecanografico();

    /**
     * @return
     */
    public abstract IPessoa getPerson();

    /**
     * @return Timestamp
     */
    public abstract Timestamp getQuando();

    /**
     * @return int
     */
    public abstract int getQuem();

    //	/**
    public abstract ICostCenter getWorkingPlaceCostCenter();

    /**
     * Sets the antiguidade.
     * 
     * @param antiguidade
     *            The antiguidade to set
     */
    public abstract void setAntiguidade(Date antiguidade);

    /**
     * Sets the calendario.
     * 
     * @param calendario
     *            The calendario to set
     */
    public abstract void setCalendario(String calendario);

    /**
     * Sets the chaveCCCorrespondencia.
     * 
     * @param chaveCCCorrespondencia
     *            The chaveCCCorrespondencia to set
     */
    public abstract void setChaveCCCorrespondencia(int chaveCCCorrespondencia);

    /**
     * Sets the chaveCCLocalTrabalho.
     * 
     * @param chaveCCLocalTrabalho
     *            The chaveCCLocalTrabalho to set
     */
    public abstract void setChaveCCLocalTrabalho(int chaveCCLocalTrabalho);

    /**
     * Sets the chaveCCVencimento.
     * 
     * @param chaveCCVencimento
     *            The chaveCCVencimento to set
     */
    public abstract void setChaveCCVencimento(int chaveCCVencimento);

    /**
     * Sets the chaveFuncResponsavel.
     * 
     * @param chaveFuncResponsavel
     *            The chaveFuncResponsavel to set
     */
    public abstract void setChaveFuncResponsavel(int chaveFuncResponsavel);

    /**
     * Setter for property chaveHorarioActual.
     * 
     * @param chaveHorarioActual
     *            New value of property chaveHorarioActual.
     *  
     */
    public abstract void setChaveHorarioActual(int chaveHorarioActual);

    /**
     * Setter for property chavePessoa.
     * 
     * @param chavePessoa
     *            New value of property chavePessoa.
     *  
     */
    public abstract void setChavePessoa(int chavePessoa);

    /**
     * Setter for property chaveStatus.
     * 
     * @param chaveStatus
     *            New value of property chaveStatus.
     *  
     */
    public abstract void setChaveStatus(int chaveStatus);

    /**
     * Setter for property codigoInterno.
     * 
     * @param codigoInterno
     *            New value of property codigoInterno.
     *  
     */
    public abstract void setCodigoInterno(int codigoInterno);

    /**
     * Sets the dataFim.
     * 
     * @param dataFim
     *            The dataFim to set
     */
    public abstract void setDataFim(Date dataFim);

    /**
     * Sets the dataInicio.
     * 
     * @param dataInicio
     *            The dataInicio to set
     */
    public abstract void setDataInicio(Date dataInicio);

    /**
     * @param mailingCostCenter
     */
    public abstract void setMailingCostCenter(ICostCenter mailingCostCenter);

    /**
     * Setter for property numeroMecanografico.
     * 
     * @param numeroMecanografico
     *            New value of property numeroMecanografico.
     *  
     */
    public abstract void setNumeroMecanografico(int numeroMecanografico);

    /**
     * @param pessoa
     */
    public abstract void setPerson(IPessoa person);

    /**
     * Sets the quando.
     * 
     * @param quando
     *            The quando to set
     */
    public abstract void setQuando(Timestamp quando);

    /**
     * Sets the quem.
     * 
     * @param quem
     *            The quem to set
     */
    public abstract void setQuem(int quem);

    //	/**
    public abstract void setWorkingPlaceCostCenter(ICostCenter workingPlaceCostCenter);
}