package ServidorAplicacao.utils.smsResponse;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *
 */
public class SmsCommandExpressionConstants
{
    // Regex expression constants
    public static final String SPACE_REGEX = "\\s";
    public static final String BEGIN_LINE_REGEX = "^";
    public static final String END_LINE_REGEX = "$";
    public static final String INTEGER_NUMBER_REGEX = "\\d+";
    public static final String DECIMAL_NUMBER_REGEX = "\\d+\\.\\d+";
    public static final String WORD_REGEX = "\\S+";
    public static final String STRING_REGEX = ".+";
    
    // Known types for variables
    public static final String INTEGER_TYPE = "integer";
    public static final String WORD_TYPE = "word";
    public static final String DECIMAL_TYPE = "decimal";
    public static final String STRING_TYPE = "string";
    
    // Reserved keywords
    public static final String FULL_SMS_TEXT_KEYWORD = "FULLSMSTEXT";
    public static final String SENDER_MSISDN_KEYWORD = "SENDERMSISDN";
    
    //Fixed variables for authentication
    public static final String USERNAME_VARIABLE = "USERNAME";
    public static final String PASSWORD_VARIABLE = "PASSWORD";
}
