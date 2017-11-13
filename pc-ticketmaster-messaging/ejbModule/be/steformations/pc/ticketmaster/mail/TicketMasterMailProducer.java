package be.steformations.pc.ticketmaster.mail;

import be.steformations.pc.ticketmaster.common.beans.Booking;

@javax.ejb.Stateless
public class TicketMasterMailProducer {

	@javax.annotation.Resource(mappedName="mail/java2015.steformations.be")
	private javax.mail.Session session;
	private java.text.DateFormat dayFormatter;
	private java.text.SimpleDateFormat timeFormatter;
	
	public TicketMasterMailProducer() {
		super();
		this.dayFormatter = java.text.DateFormat.getDateInstance(
				java.text.DateFormat.FULL, java.util.Locale.FRENCH);
		this.timeFormatter = new java.text.SimpleDateFormat("H:mm", java.util.Locale.FRENCH);
	}
	
	public void send(Booking booking) {
		String subject = "Confirmation de votre réservation";
		String body = String.format(
				  "Confirmation de votre réservation pour le spectacle '%s'.\n"
				+ "Lieu: %s\n"
				+ "Date: le %s à %s", 
				booking.getShow().getTitle(),
				booking.getShow().getVenue().getName(),
				this.dayFormatter.format(booking.getShow().getDay()),
				this.timeFormatter.format(booking.getShow().getTime()));
		String recipient = booking.getClient().getEmail();

		this.send(subject, body, recipient);
	}
	
	public void send(String subject, String body, String recipient) {
		try {
			javax.mail.internet.MimeMessage message
				= new javax.mail.internet.MimeMessage(this.session);
			message.setSender(
				new javax.mail.internet.InternetAddress(
					this.session.getProperty("mail.from")));
			message.setFrom(this.session.getProperty("mail.from"));
			message.setSubject(subject);
			message.setContent(body, "text/plain; charset=UTF-8");
			message.setRecipient(
				javax.mail.Message.RecipientType.TO, 
				new javax.mail.internet.InternetAddress(recipient));
			javax.mail.Transport.send(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
