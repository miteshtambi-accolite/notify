package com.accolite.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.accolite.core.entity.User;
import com.accolite.core.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final static Logger logger = Logger.getLogger(EmailServiceImpl.class);

    private static final String SUBJECT_MAIL_REGISTRATION_CONFIRMATION = "Registration Confirmation";

    private static final String CHARSET_UTF8 = "UTF-8";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Override
    public String sendEmail(final User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(user.getEmail());
                message.setSubject(SUBJECT_MAIL_REGISTRATION_CONFIRMATION);

                Map model = new HashMap<>();
                model.put("user", user);

                message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine
                        , "registration-confirmation.vm", CHARSET_UTF8, model), true);
            }
        };

        this.javaMailSender.send(preparator);
        return "Sent Successfully";
    }
    
    
   /* private MimeBodyPart buildAttachment(InputStream inputStream, String fileName) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MimeBodyPart pdfBodyPart = null;
		try {
			if (inputStream != null) {
				org.apache.commons.io.IOUtils.copy(inputStream, baos);
				byte[] bytes = baos.toByteArray();
				DataSource dataSource = new ByteArrayDataSource(bytes,
						"application/" + AttachmentType.fromAttachmentTypeCode(attachmentType.getId()));
				pdfBodyPart = new MimeBodyPart();
				pdfBodyPart.setDataHandler(new DataHandler(dataSource));
				pdfBodyPart.setFileName(fileName);
			}
		} catch (Exception e) {
			logger.error("Error in buildAttachment", e);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}

		return pdfBodyPart;
	}*/

}
