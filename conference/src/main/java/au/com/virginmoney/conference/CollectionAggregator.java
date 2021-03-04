/**
 * 
 */
package au.com.virginmoney.conference;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author rahul
 *
 */
public class CollectionAggregator implements AggregationStrategy {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange == null) {
			// Only gets here for the first iteration of the split
			// Here we create the container for aggregation and begin aggregating
			final List aggregatingList = firstAggregationRun(newExchange.getIn().getBody());
			newExchange.getIn().setBody(aggregatingList);
			return newExchange;
        }
		// Keep aggregating in the oldExchange, eventually that is the only one that matters
		final Object payload = newExchange.getIn().getBody();
		if (oldExchange.getIn().getBody() instanceof List) {
            final List resourceCollection = oldExchange.getIn().getBody(List.class);
    		resourceCollection.add(payload);
		} else {
			oldExchange.getIn().setBody(firstAggregationRun(payload));
		}
        return oldExchange;		
	}
	
	protected ArrayList firstAggregationRun(Object payload) {
		final ArrayList aggregatingList = new ArrayList();
		aggregatingList.add(payload);
		return aggregatingList;
	}

}
