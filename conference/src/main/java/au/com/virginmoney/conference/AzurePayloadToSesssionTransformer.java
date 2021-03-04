package au.com.virginmoney.conference;

import java.util.Iterator;

import org.apache.camel.Exchange;

public class AzurePayloadToSesssionTransformer {

	public void transformBody(Exchange exchange) {
		final AzurePayload azurePayload = exchange.getMessage().getBody(AzurePayload.class);
		final Session session = new Session();
		
		session.setTitle(findData(azurePayload, "Title"));
		
		session.setTimeslot(findData(azurePayload, "Timeslot"));

		final Speaker sessionSpeaker = new Speaker();
		session.setSpeaker(sessionSpeaker);
		session.getSpeaker().setName(findData(azurePayload, "Speaker"));
		
		exchange.getMessage().setBody(session);
	}
	
	public String findData(AzurePayload payload, String data) {
		for (Iterator iterator = payload.getData().iterator(); iterator.hasNext();) {
			AzureRecord next = (AzureRecord) iterator.next();
			if (next.getName().equals(data))
				return next.getValue();
		}
		return null;
	}


}
