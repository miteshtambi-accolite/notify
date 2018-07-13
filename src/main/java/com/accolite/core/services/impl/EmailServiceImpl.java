package com.accolite.core.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.accolite.core.entity.User;
import com.accolite.core.services.EmailService;
import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
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
    
	@Override
	public String sendEmailWithAttachment(final User user) {
		
		//velocityEngine.init();
		
		VelocityContext context = new VelocityContext();
		context.put("user", user);
		
		 Template t = velocityEngine.getTemplate( "policy-certificate.vm" );
		 StringWriter writer = new StringWriter();
         t.merge( context, writer );
         
         String fileName = "Certificate.pdf";
         InputStream attachment = null;
         
         try {
        	    String k = writer.toString();
        	    OutputStream file = new FileOutputStream(new File(fileName));
        	    Document document = new Document();
        	    PdfWriter.getInstance(document, file);
        	    document.open();
        	    HTMLWorker htmlWorker = new HTMLWorker(document);
        	    htmlWorker.parse(new StringReader(k));
        	    document.close();
        	    file.close();
        	    attachment = new FileInputStream(new File(fileName));
        	} catch (Exception e) {
        	    e.printStackTrace();
        	}
         
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		try {
			MimeBodyPart mimeBodyPartAttachment = buildAttachment(attachment, fileName);

			email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			email.setSubject(SUBJECT_MAIL_REGISTRATION_CONFIRMATION);
			Map model = new HashMap<>();
			model.put("user", user);
			email.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "registration-confirmation.vm",
					CHARSET_UTF8, model));

			MimeBodyPart mimeBodyPart = new MimeBodyPart();

			mimeBodyPart.setContent(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
					"registration-confirmation.vm", CHARSET_UTF8, model), "text/html");
			mimeBodyPart.setHeader("Content-Type", "text/html; charset=\"UTF-8\"");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			if (attachment != null)
				multipart.addBodyPart(mimeBodyPartAttachment);

			email.setContent(multipart);
		} catch (VelocityException | MessagingException e) {
			throw new RuntimeException();
		}

		this.javaMailSender.send(email);
		return "Sent Successfully";
	}
    
    private MimeBodyPart buildAttachment(InputStream inputStream, String fileName) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MimeBodyPart pdfBodyPart = null;
		try {
			if (inputStream != null) {
				org.apache.commons.io.IOUtils.copy(inputStream, baos);
				byte[] bytes = baos.toByteArray();
				DataSource dataSource = new ByteArrayDataSource(bytes,
						"application/pdf");
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
	}

}
