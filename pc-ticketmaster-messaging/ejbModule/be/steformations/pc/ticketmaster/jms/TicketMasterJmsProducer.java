package be.steformations.pc.ticketmaster.jms;

import be.steformations.pc.ticketmaster.common.beans.Show;
import be.steformations.pc.ticketmaster.common.beans.Venue;
import be.steformations.pc.ticketmaster.jpa.entities.ShowEntity;

@javax.ejb.Stateless
public class TicketMasterJmsProducer {

	@javax.annotation.Resource(mappedName="jms/TicketMasterConnectionFactory")
	private javax.jms.ConnectionFactory connectionFactory;
	@javax.annotation.Resource(mappedName="jms/TicketMasterDestination")
	private javax.jms.Destination destination;
	
	public void broadcast(ShowEntity entity) {
		try {
			Show show = this.createShow(entity);
			javax.jms.Connection connection = this.connectionFactory.createConnection();
			javax.jms.Session session = connection.createSession();
			javax.jms.Message message = session.createObjectMessage(show);
			javax.jms.MessageProducer messageProducer = session.createProducer(this.destination);
			messageProducer.send(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Show createShow(ShowEntity entity) {
		Show show = null;
		if (entity != null) {
			Venue venue = null;
			if (entity.getVenue() != null) {
				venue = new Venue();
				venue.setId(entity.getVenue().getId());
				venue.setName(entity.getVenue().getName());
				venue.setCapacity(entity.getVenue().getCapacity());
			}
			show = new Show();
			show.setId(entity.getId());
			show.setTitle(entity.getTitle());
			show.setDay(entity.getDay());
			show.setTime(entity.getTime());
			show.setVenue(venue);
		}
		return show;
	}
}
