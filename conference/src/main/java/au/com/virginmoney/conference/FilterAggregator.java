/**
 * 
 */
package au.com.virginmoney.conference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author rahul
 *
 */
public class FilterAggregator extends CollectionAggregator {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange)  {
		if (oldExchange == null) {
			final Session session = (Session) newExchange.getIn().getBody();
			final String speakerHeader = (String) newExchange.getMessage().getHeader("speakerName");
			final String sessionDate = (String) newExchange.getMessage().getHeader("sessionDate");
			
			final Exchange firstExchange = super.aggregate(oldExchange, newExchange);
			if (isHeaderEmpty(firstExchange, "speakerName") && isHeaderEmpty(firstExchange, "sessionDate"))
				return firstExchange;
			else if (!matchesSession(speakerHeader, sessionDate, session)) {
				firstExchange.getIn().getBody(List.class).remove(session);
				return firstExchange;
			} else
				return firstExchange;
				
        } else if (newExchange.getIn().getBody() instanceof Session) {
			final Session session = (Session) newExchange.getIn().getBody();
			final String speakerHeader = (String) oldExchange.getMessage().getHeader("speakerName");
			final String sessionDate = (String) oldExchange.getMessage().getHeader("sessionDate");
			
			if (isHeaderEmpty(oldExchange, "speakerName") && isHeaderEmpty(oldExchange, "sessionDate"))
				return super.aggregate(oldExchange, newExchange);
			else if (!matchesSession(speakerHeader, sessionDate, session)) {
				oldExchange.getIn().getBody(List.class).remove(session);
				return oldExchange;
			}
		} 
		return super.aggregate(oldExchange, newExchange);
	}

	private boolean isHeaderEmpty(Exchange oldExchange, String header) {
		return !oldExchange.getMessage().getHeaders().containsKey(header) ||  
				oldExchange.getMessage().getHeader(header) == null;
	}

	private boolean matchesSession(String speakerHeader, String sessionDate, Session session) {
		try {
			return matchesDate(sessionDate, session.getTimeslot()) && 
					matches(speakerHeader, session.getSpeaker().getName());
		} catch (ParseException e) {
			return false;
		}
	}

	private boolean matches(String headerValue, String data) {
		return headerValue == null || headerValue.equals("") || 
				(data != null && data.contains(headerValue));
	}

	private boolean matchesDate(String headerDate, String data) throws ParseException {
		final SimpleDateFormat headerDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat timeslotFormat = new SimpleDateFormat("dd MMMM yyyy");
		return headerDate == null || headerDate.equals("") || 
				(data != null && data.contains(timeslotFormat.format(headerDateFormat.parse(headerDate))));
	}

}
