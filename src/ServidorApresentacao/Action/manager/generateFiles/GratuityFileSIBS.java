/*
 * Created on 28/Jan/2004
 *  
 */
package ServidorApresentacao.Action.manager.generateFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGratuitySituation;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;

/**
 * @author Tânia Pousão
 *  
 */
public class GratuityFileSIBS
{
	public static final int WHITE_SPACES_HEADER = 3;
	public static final int WHITE_SPACES_LINE = 2;
	public static final int WHITE_SPACES_FOOTER = 41;
	public static final int MAX_LINES_NUMBER = 8;
	public static final int MAX_INT_PAYMENT = 8;
	public static final int MAX_DEC_PAYMENT = 2;
	public static final int MAX_STUDENT_NUMBER = 5;
	public static final char SPACE = ' ';
	public static final char ZERO = '0';
	public static final double INSURANCE = 2.50;
	public static final String WITHOUT_ADDRESS = "Sem morada";
	public static final String NOTHING_TO_PAY = "Nada a pagar";

	public static File buildFile(List infoGratuitySituations) throws Exception
	{
		if (infoGratuitySituations == null)
		{
			return null;
		}

		String fileName = null;
		File file = null;
		BufferedWriter writer = null;

		String fileNameErrors = null;
		File fileErrors = null;
		BufferedWriter writerErrors = null;

		try
		{
			//build the file's name with the first element
			fileName = nameFile((InfoGratuitySituation) infoGratuitySituations.get(0));
			file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
			writer = new BufferedWriter(new FileWriter(file));

			//errors if student hasn´t address or nothing to pay
			fileNameErrors = "erros_" + nameFile((InfoGratuitySituation) infoGratuitySituations.get(0));
			fileErrors =
				new File(System.getProperty("java.io.tmpdir") + File.separator + fileNameErrors);
			writerErrors = new BufferedWriter(new FileWriter(fileErrors));

			writeHeader(writer);

			Iterator iterator = infoGratuitySituations.listIterator();
			int linesNumber = 0;
			while (iterator.hasNext())
			{
				InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) iterator.next();

				if (valid(infoGratuitySituation, writerErrors))
				{
					writeLine(writer, infoGratuitySituation);
					linesNumber++;
				}
			}

			writeFooter(writer, linesNumber);

			writer.close();
			writerErrors.close();

			return file;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			throw new Exception();
		}
	}

	private static String nameFile(InfoGratuitySituation infoGratuitySituation)
	{
		StringBuffer fileName = new StringBuffer();

		String year =
			infoGratuitySituation
				.getInfoGratuityValues()
				.getInfoExecutionDegree()
				.getInfoExecutionYear()
				.getYear()
				.replace('/', '-');
		fileName.append("SIBSPropinas");
		fileName.append(year);
		fileName.append(".txt");

		return fileName.toString();
	}

	/**
	 * Creates a header that describes all column in file like <name column>;...; <name column>
	 * 
	 * @param writer
	 */
	private static void writeHeader(BufferedWriter writer) throws IOException
	{
		StringBuffer header = new StringBuffer();
		header.append(0); //register type, usually 0
		header.append("AEPS"); //file type
		header.append(90000820); //number with 8 digits
		header.append(50000000); //number with 8 digits
		header.append(formatDate(Calendar.getInstance().getTime())); //today's date
		header.append(1); //sequence number, for omission it is 1
		header.append(formatDate(Calendar.getInstance().getTime()));
		//last file's date that it was sended
		header.append(1); //last file's sequence number that it was sended, usually it is 1
		header.append(20801); //entity's code
		header.append(978); //currency(coin) code, in this case it is euro's code
		header.append(addCharToStringUntilMax(SPACE, "", WHITE_SPACES_HEADER)); // three white spaces

		writer.write(header.toString());
		writer.newLine();
	}

	/**
	 * Verify if the student hasn't address and nothing to pay if true the student doesn't receive the
	 * letter
	 * 
	 * @param infoGratuitySituation
	 * @return
	 */
	private static boolean valid(
		InfoGratuitySituation infoGratuitySituation,
		BufferedWriter writerErrors)
		throws IOException
	{
		if (isNullOrEmpty(infoGratuitySituation
			.getInfoStudentCurricularPlan()
			.getInfoStudent()
			.getInfoPerson()
			.getMorada())
			|| isNullOrEmpty(
				infoGratuitySituation
					.getInfoStudentCurricularPlan()
					.getInfoStudent()
					.getInfoPerson()
					.getLocalidade())
			|| isNullOrEmpty(
				infoGratuitySituation
					.getInfoStudentCurricularPlan()
					.getInfoStudent()
					.getInfoPerson()
					.getCodigoPostal())
			|| isNullOrEmpty(
				infoGratuitySituation
					.getInfoStudentCurricularPlan()
					.getInfoStudent()
					.getInfoPerson()
					.getLocalidadeCodigoPostal()))
		{
			//write the error
			writerErrors.write(
				infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getNumber()
					+ " "
					+ WITHOUT_ADDRESS);
			writerErrors.newLine();
			return false;
		}
		else if (
			infoGratuitySituation.getRemainingValue().doubleValue() <= 0
				&& infoGratuitySituation.getInsurancePayed().equals(SessionConstants.PAYED_INSURANCE))
		{
			writerErrors.write(
				infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getNumber()
					+ " "
					+ NOTHING_TO_PAY);
			writerErrors.newLine();
			return false;
		}

		return true;
	}

	/**
	 * Verify if the string is null pu empty
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isNullOrEmpty(String string)
	{
		return (string == null || string.length() <= 0);
	}

	/**
	 * Creates a line with student's data separate by tab
	 * 
	 * @param writer
	 * @param infoGratuitySituation
	 */
	private static void writeLine(BufferedWriter writer, InfoGratuitySituation infoGratuitySituation)
		throws IOException
	{
		StringBuffer line = new StringBuffer();

		line.append(1); //register type, usually 1
		line.append(80); //processing type, usually 80 but can be 82
		line.append(buildPaymentReference(infoGratuitySituation)); //payment's reference
		//payment's end date
		line.append(formatDate(infoGratuitySituation.getInfoGratuityValues().getEndPayment()));

		double payValue = findPayValue(infoGratuitySituation);

		//payment's max value
		line.append(
			buildPaymentValue(
				payValue,
				MAX_INT_PAYMENT,
				MAX_DEC_PAYMENT));
		//payment's begin date
		line.append(formatDate(infoGratuitySituation.getInfoGratuityValues().getStartPayment()));
		//payment's min value
		line.append(
			buildPaymentValue(
				payValue,
				MAX_INT_PAYMENT,
				MAX_DEC_PAYMENT));
		line.append(addCharToStringUntilMax(SPACE, "", WHITE_SPACES_LINE)); // two white spaces

		//write the line
		writer.write(line.toString());
		writer.newLine();
	}

	/**
	 * @param writer
	 */
	private static void writeFooter(BufferedWriter writer, int linesNumber) throws IOException
	{
		StringBuffer footer = new StringBuffer();

		footer.append(9); //register type, usually 9
		footer.append(addCharToStringUntilMax(ZERO, String.valueOf(linesNumber), MAX_LINES_NUMBER));
		//number of lines except header and footer, attention number with 8 digits
		footer.append(addCharToStringUntilMax(SPACE, "", WHITE_SPACES_FOOTER));
		// forty-one white spaces

		writer.write(footer.toString());
		writer.newLine();
	}

	/**
	 * Build the payment's reference that the first two digits represent the year, the next fifth digits
	 * are the student's number and the last two digits are a payment's code
	 * 
	 * @param infoGratuitySituation
	 * @return
	 */
	private static String buildPaymentReference(InfoGratuitySituation infoGratuitySituation)
	{
		StringBuffer reference = new StringBuffer();

		//year
		String year =
			infoGratuitySituation
				.getInfoGratuityValues()
				.getInfoExecutionDegree()
				.getInfoExecutionYear()
				.getYear();
		reference.append(year.substring(2, year.indexOf('/'))); //year was like 2003/2004
		//student's number
		reference.append(addCharToStringUntilMax(ZERO, infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getNumber().toString(), MAX_STUDENT_NUMBER));
		//code
		reference.append("40"); //for now is 40 and fixe, but later will not be thus

		return reference.toString();
	}

	/**
	 * Find the value that the student will be 
	 * @param infoGratuitySituation
	 * @return
	 */
	private static double findPayValue(InfoGratuitySituation infoGratuitySituation)
	{
		double valueToPay = infoGratuitySituation.getRemainingValue().doubleValue();
		
		if(infoGratuitySituation.getInsurancePayed().equals(SessionConstants.NOT_PAYED_INSURANCE)){
			valueToPay = infoGratuitySituation.getRemainingValue().doubleValue() + INSURANCE;
		}
		
		return valueToPay;
	}
	
	/**
	 * Build the value that it has 8 digits in int part, and it has 2 digits in decimal part
	 * 
	 * @param infoGratuitySituation
	 * @return
	 */
	private static String buildPaymentValue(double value, int intDigits, int decDigits)
	{
		StringBuffer stringBuffer = new StringBuffer();

		String valueString = String.valueOf(value);
		String intPart = valueString.substring(0, valueString.indexOf('.'));
		String decPart = valueString.substring(valueString.indexOf('.') + 1);
//		
//		System.out.println("-->1 " + valueString);
//		System.out.println("-->2 " + intPart);
//		System.out.println("-->3 " + decPart);
		
		for (int i = 0; i < intDigits - intPart.length(); i++)
		{
			stringBuffer.append(ZERO);
		}
		stringBuffer.append(intPart);
//		System.out.println("-->4 " + stringBuffer);
		
		for (int i = 0; i < decDigits - decPart.length(); i++)
		{
			stringBuffer.append(ZERO);
		}
		stringBuffer.append(decPart);
//		System.out.println("-->5 " + stringBuffer);
		
		return stringBuffer.toString();
	}

	/**
	 * Formats a date like yyyymmdd
	 * 
	 * @param date
	 * @return string
	 */
	private static String formatDate(Date date)
	{
		//if date is null then date is equal to today's date
		if (date == null)
		{
			date = new Date();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());

		StringBuffer dateString = new StringBuffer();
		dateString.append(calendar.get(Calendar.YEAR));
		

		int month = calendar.get(Calendar.MONTH) + 1;
		String monthString = "";
		if (month < 10)
		{
			monthString = "0";
		}
		monthString = monthString + String.valueOf(month);
		dateString.append(monthString);
		
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String dayString = "";
		if (day < 10)
		{
			dayString = "0";
		}
		dayString = dayString + String.valueOf(day);
		dateString.append(dayString);
		
		return dateString.toString();
	}

	/**
	 * add a char to the string until reach the max lenth
	 * 
	 * @param maximum
	 *            digits for the number
	 * @param number
	 * @return string
	 */
	private static String addCharToStringUntilMax(char c, String string, int maxlength)
	{
		StringBuffer stringComplete = new StringBuffer();

		int stringLength = 0;
		if(string != null)
		{
			stringLength = string.length();
		}
		
		for (int i = 0; i < maxlength - stringLength; i++)
		{
			stringComplete.append(c);
		}
		stringComplete.append(string);

		return stringComplete.toString();
	}
}
