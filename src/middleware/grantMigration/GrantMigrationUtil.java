/*
 * Created on Feb 26, 2004
 */
package middleware.grantMigration;

import middleware.middlewareDomain.grant.MWPerson;
import middleware.middlewareDomain.grant.utils.MWOldDocumentIdType2NewDocumentIdType;
import middleware.middlewareDomain.grant.utils.MWOldMaritalStatusType2NewMaritalStatusType;
import middleware.middlewareDomain.grant.utils.MWOldSexType2NewSexType;
import DataBeans.util.CopyUtils;
import Dominio.Pessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Pica
 * @author Barbosa
 */

public class GrantMigrationUtil
{
	private static void copyObjectProperties(Object destination, Object source)
	{
		if (source != null)
			try
			{
				//BeanUtils.copyProperties(destination, source);
				CopyUtils.copyProperties(destination, source);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	}

	public Pessoa checkIfOldPersonExistsInFenix(MWPerson oldPerson) throws ExcepcaoPersistencia
	{
		Pessoa fenixPerson = null;

		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPessoaPersistente pp = sp.getIPessoaPersistente();
		TipoDocumentoIdentificacao idType =
			new TipoDocumentoIdentificacao(oldPerson.getTipoDocumentoIdentificacao());
		fenixPerson =
			(Pessoa) pp.lerPessoaPorNumDocIdETipoDocId(
				oldPerson.getNumeroDocumentoIdentificacao(),
				idType);

		return fenixPerson;
	}

	public Pessoa copyMWPerson2FenixPerson(MWPerson mwPerson)
	{
		Pessoa fenixPerson = new Pessoa();

		if (mwPerson != null)
		{
			copyObjectProperties(fenixPerson, mwPerson);
			//copy tipoDocumentoIdentificacao
			MWOldDocumentIdType2NewDocumentIdType idTypeCopy =
				new MWOldDocumentIdType2NewDocumentIdType();
			idTypeCopy.ConvertOldDocumentType2NewDocumentType(mwPerson.getTipoDocumentoIdentificacao());
			//copy sexo
			MWOldSexType2NewSexType sexCopy = new MWOldSexType2NewSexType();
			sexCopy.ConvertOldSexType2NewSexType(mwPerson.getSexo());
			//copy estadoCivil;
			MWOldMaritalStatusType2NewMaritalStatusType maritalStatusCopy =
				new MWOldMaritalStatusType2NewMaritalStatusType();
			maritalStatusCopy.ConvertOldMaritalStatusType2NewMaritalStatusType(
				mwPerson.getEstadoCivil());
		}

		//nacionalidadeCompleta does not exist in the Fenix Domain Pessoa.
		//String nacionalidadeCompleta;

		return fenixPerson;
	}

/*	public Country copyMWCountry2FenixCountry(MWGrantCountry mwCountry)
	{
		Country fenixCountry = null;

		if (mwCountry != null)
		{
			fenixCountry = new Country();

			//MWOldGrantCountry2FenixCountry countryCopy = new MWOldGrantCountry2FenixCountry();
			//countryCopy.Convert(fenixCountry, mwCountry);
		}

		return fenixCountry;
	}*/

	/*public GrantContract copyMWGrantContract2FenixGrantContract(MWGrantContract mwGrantContract)
	{
		GrantContract fenixGrantContract = null;

		return fenixGrantContract;
	}*/

	/*public GrantCostCenter copyMWGrantCostCenter2GrantCostCenter(MWGrantCostCenter mwGrantCostCenter)
	{
		GrantCostCenter grantCostCenter = null;

		//TODO... copy properties

		return grantCostCenter;
	}*/

	/*public GrantOrientationTeacher copyMWGrantOrientationTeacher2GrantOrientationTeacher(MWGrantOrientationTeacher mwGrantOrientationTeacher)
	{
		GrantOrientationTeacher grantOrientationTeacher = null;

		//TODO.. copy properties

		return grantOrientationTeacher;
	}*/

	/*public GrantOwner copyMWGrantOwner2GrantOwner(MWGrantOwner mwGrantOwner) throws Throwable
	{
		GrantOwner fenixGrantOwner = new GrantOwner();
		Pessoa fenixPerson = null;

        copyObjectProperties(fenixGrantOwner, mwGrantOwner);
		fenixPerson = checkIfOldPersonExistsInFenix(mwGrantOwner.getPerson());
		//I N C O M P L E T O ! ! !
		return fenixGrantOwner;
	}*/

	/*public GrantPart copyMWGrantPart2GrantPart(MWGrantPart mwGrantPart)
	{
		GrantPart grantPart = null;

		if (mwGrantPart != null)
		{
			grantPart = new GrantPart();
			grantPart.setIdInternal(mwGrantPart.getIdInternal());
			grantPart.setPercentage(mwGrantPart.getPercentage());
			grantPart.setKeyGrantPaymentEntity(mwGrantPart.getKeyGrantPaymentEntity());
			grantPart.setGrantPaymentEntity(null);
			grantPart.setKeyGrantSubsidy(mwGrantPart.getKeyGrantSubsidy());
			grantPart.setGrantSubsidy(null);
			grantPart.setKeyResponsibleTeacher(mwGrantPart.getKeyResponsibleTeacher());
			grantPart.setResponsibleTeacher(null);
			//TODO.. função por fazer!!!
		}

		return grantPart;
	}*/

	/*public GrantProject copyMWGrantProject2GrantProject(MWGrantProject mwGrantProject)
	{
		GrantProject grantProject = null;

		//TODO.. pedir a lista de projectos....

		return grantProject;
	}*/

	/*public Qualification copyMWGrantQualification2Qualification(MWGrantQualification mwGrantQualification)
	{
		Qualification qualification = null;

		if (mwGrantQualification != null)
		{
			qualification = new Qualification();

			qualification.setIdInternal(mwGrantQualification.getIdInternal());
			if (mwGrantQualification.getMark() != null)
				qualification.setMark(mwGrantQualification.getMark());
			else
				qualification.setMark("0");
			if (mwGrantQualification.getSchool() != null)
				qualification.setSchool(mwGrantQualification.getSchool());
			else
				qualification.setSchool("");
			if (mwGrantQualification.getTitle() != null
				&& mwGrantQualification.getTitle().getCompleteName() != null)
				qualification.setTitle(mwGrantQualification.getTitle().getCompleteName());
			if (mwGrantQualification.getDegree() != null
				&& mwGrantQualification.getDegree().getCategoryLong() != null)
				qualification.setDegree(mwGrantQualification.getDegree().getCategoryLong());
			qualification.setDate(mwGrantQualification.getDate());
			if (mwGrantQualification.getBranch() != null)
				qualification.setBranch(mwGrantQualification.getBranch());
			if (mwGrantQualification.getSpecializationArea() != null)
				qualification.setSpecializationArea(mwGrantQualification.getSpecializationArea());
			if (mwGrantQualification.getDegreeRecognition() != null)
				qualification.setDegreeRecognition(mwGrantQualification.getDegreeRecognition());
			if (mwGrantQualification.getEquivalenceDate() != null)
				qualification.setEquivalenceDate(mwGrantQualification.getEquivalenceDate());
			if (mwGrantQualification.getEquivalenceSchool() != null)
				qualification.setEquivalenceSchool(mwGrantQualification.getEquivalenceSchool());

			if (mwGrantQualification.getCountry() != null)
			{
				qualification.setCountryKey(mwGrantQualification.getKeyCountry());
				//TODO.. copiar o pais.. usando a util ja criada
			}

			if (mwGrantQualification.getPerson() != null)
			{
				qualification.setPersonKey(mwGrantQualification.getKeyPerson());
				qualification.setPerson(copyMWPerson2FenixPerson(mwGrantQualification.getPerson()));
			}
		}
		return qualification;
	}*/

/*	public GrantSubsidy copyMWGrantSubsidy2GrantSubsidy(MWGrantSubsidy mwGrantSubsidy)
	{
		GrantSubsidy grantSubsidy = null;

		if (mwGrantSubsidy != null)
		{
			grantSubsidy = new GrantSubsidy();

			grantSubsidy.setIdInternal(mwGrantSubsidy.getIdInternal());
			if (mwGrantSubsidy.getValueFullName() != null)
				grantSubsidy.setValueFullName(mwGrantSubsidy.getValueFullName());
			if (mwGrantSubsidy.getValue() != null)
				grantSubsidy.setValue(mwGrantSubsidy.getValue());
			else
				grantSubsidy.setValue(new Double(0.0));
			if (mwGrantSubsidy.getTotalCost() != null)
				grantSubsidy.setTotalCost(mwGrantSubsidy.getTotalCost());

			if (mwGrantSubsidy.getKeyGrantContract() != null)
				grantSubsidy.setKeyGrantContract(mwGrantSubsidy.getKeyGrantContract());
			if (mwGrantSubsidy.getGrantContract() != null)
				grantSubsidy.setGrantContract(
					copyMWGrantContract2FenixGrantContract(mwGrantSubsidy.getGrantContract()));
			//Begin date isn't copied....

		}
		return grantSubsidy;
	}*/

/*	public GrantType copyMWGrantType2GrantType(MWGrantType mwGrantType)
	{
		GrantType grantType = null;

		if (mwGrantType != null)
		{
			grantType = new GrantType();

			grantType.setIdInternal(mwGrantType.getIdInternal());
			grantType.setName(mwGrantType.getName());
			grantType.setSigla(mwGrantType.getSigla());
			if (mwGrantType.getMinimalPeriod() != null)
				grantType.setMinPeriodDays(mwGrantType.getMinimalPeriod());
			if (mwGrantType.getMaximumPeriod() != null)
				grantType.setMaxPeriodDays(mwGrantType.getMaximumPeriod());
			if (mwGrantType.getIndicativeValue() != null)
				grantType.setIndicativeValue(mwGrantType.getIndicativeValue());
			grantType.setSource(mwGrantType.getOrigem());
		}
		return grantType;
	}
*/
	/*public Teacher copyMWTeacher2Teacher(MWTeacher mwTeacher)
	{
		Teacher teacher = null;

		//TODO copy properties

		return teacher;
	}*/
}