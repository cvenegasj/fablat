package org.fablat.resource.util;

public interface EmailService {
	
    void sendHTMLMessage(String to,
                           String subject,
                           String text);
    /*
    void sendSimpleMessageUsingTemplate(String to,
                                        String subject,
                                        SimpleMailMessage template,
                                        String ...templateArgs);
    
    void sendMessageWithAttachment(String to,
                                   String subject,
                                   String text,
                                   String pathToAttachment);
                                   */
    
}
