

package net.sourceforge.fenixedu.domain.cms.infrastructure;

public class CmsConfiguration extends CmsConfiguration_Base
{
	private static String SMTP_SERVER_ADDRESS_DEFAULT = "mail.adm";
	private static Boolean REPLY_TO_MAILING_LIST_DEFAULT = true;
	private static Boolean FILTER_NON_TEXTUAL_ATTACHMENTS_DEFAULT = true;
	private static Integer SEND_MAIL_INTERVAL_DEFAULT = 60;
	private static String MAILING_LIST_HOST_DEFAULT = "lists.fenix.ist.utl.pt";
	private static String CONVERSATION_REPLY_MARKERS_DEFAULT = "Re Reply";
	private static Integer MAX_ATTACHMENT_SIZE = 10000000;
	private static Integer MAX_MESSAGE_SIZE =    10500000;	

	public CmsConfiguration()
	{
		super();
	}

	public String getSmtpServerAddressToUse()
	{
		return this.getSmtpServerAddress() == null ? SMTP_SERVER_ADDRESS_DEFAULT
				: this.getSmtpServerAddress();
	}

	public Boolean getReplyToMailingListToUse()
	{
		return this.getReplyToMailingList() == null ? REPLY_TO_MAILING_LIST_DEFAULT
				: this.getReplyToMailingList();
	}

	public boolean getFilterNonTextualAttachmentsToUse()
	{
		return this.getFilterNonTextualAttachments() == null ? FILTER_NON_TEXTUAL_ATTACHMENTS_DEFAULT
				: this.getFilterNonTextualAttachments();
	}

	public Integer getSendMailIntervalToUse()
	{
		return this.getSendMailInterval() == null ? SEND_MAIL_INTERVAL_DEFAULT
				: this.getSendMailInterval();
	}
	
	public String getMailingListsHostToUse()
	{
		return this.getMailingListsHost() == null? MAILING_LIST_HOST_DEFAULT: this.getMailingListsHost();
	}
	
	public String[] getConversationReplyMarkersToUse()
	{
		return this.getConversationReplyMarkers() == null? CONVERSATION_REPLY_MARKERS_DEFAULT.split(","): this.getConversationReplyMarkers().split(",");
	}

	public Integer getMaxAttachmentSizeToUse()
	{
		return this.getMaxAttachmentSize() == null? MAX_ATTACHMENT_SIZE: this.getMaxAttachmentSize();
	}

	public Integer getMaxMessageSizeToUse()
	{
		return this.getMaxMessageSize() == null? MAX_MESSAGE_SIZE:this.getMaxMessageSize();
	}
	
	public Integer getSendMailTimeoutToUse()
	{
		return this.getSendMailTimeout() == null? MAX_MESSAGE_SIZE:this.getSendMailTimeout();
	}
}
