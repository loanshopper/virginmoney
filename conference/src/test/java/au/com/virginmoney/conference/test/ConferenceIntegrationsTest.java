/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.com.virginmoney.conference.test;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.com.virginmoney.conference.Session;

public class ConferenceIntegrationsTest extends Assert {
    private AbstractApplicationContext applicationContext;
    private ProducerTemplate template;
    private CamelContext camelContext;
    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("camel-integrations.xml");
        camelContext = getCamelContext();
        template = camelContext.createProducerTemplate();
    }

    @Test
    public void testApiCandidateHelperRoute() throws Exception {
		final Exchange exchange = new DefaultExchange(camelContext);
		final Message message = exchange.getIn();
		message.setHeader("uri_part", "sessions");
		exchange.setMessage(message);
		
		template.send("direct-vm:apiCandidatesHelper", exchange);    
		final String response = (String) exchange.getMessage().getBody();
		assertNotNull(response);
    }

    @Test
    public void testSessionsTransformerRoute() throws Exception {
		final Exchange exchange = new DefaultExchange(camelContext);
		final Message message = exchange.getIn();
		message.setBody(sessions);
		exchange.setMessage(message);
		
		template.send("direct-vm:transformToSessionList", exchange);    
		final Object output = exchange.getMessage().getBody();
		assertNotNull(output);
		assertEquals(2, ((List<Session>)output).size());
    }

    @Test
    public void testSessionsTransformerRouteWithSpeakerNameFilter() throws Exception {
		final Exchange exchange = new DefaultExchange(camelContext);
		final Message message = exchange.getIn();
		message.setBody(sessions);
		message.setHeader("speakerName", "Dan");
		exchange.setMessage(message);
		
		template.send("direct-vm:transformToSessionList", exchange);    
		final Object output = exchange.getMessage().getBody();
		assertNotNull(output);
		assertEquals(1, ((List<Session>)output).size());
    }


    @After
    public void tearDown() throws Exception {
        if (applicationContext != null) {
            applicationContext.stop();
        }
    }
    
    protected CamelContext getCamelContext() throws Exception {
        return applicationContext.getBean("camel-integrations", CamelContext.class);
    }
  
    private String sessions = "{\r\n"
    		+ "    \"collection\": {\r\n"
    		+ "        \"version\": \"1.0\",\r\n"
    		+ "        \"links\": [],\r\n"
    		+ "        \"items\": [{\r\n"
    		+ "                \"href\": \"https://conferenceapi.azurewebsites.net/session/100\",\r\n"
    		+ "                \"data\": [{\r\n"
    		+ "                        \"name\": \"Title\",\r\n"
    		+ "                        \"value\": \"Keynote with Dan North - Jackstones: the Journey to Mastery\"\r\n"
    		+ "                    },\r\n"
    		+ "                    {\r\n"
    		+ "                        \"name\": \"Timeslot\",\r\n"
    		+ "                        \"value\": \"04 December 2013 09:00 - 10:00\"\r\n"
    		+ "                    },\r\n"
    		+ "                    {\r\n"
    		+ "                        \"name\": \"Speaker\",\r\n"
    		+ "                        \"value\": \"Dan North\"\r\n"
    		+ "                    }\r\n"
    		+ "                ],\r\n"
    		+ "                \"links\": [{\r\n"
    		+ "                        \"rel\": \"http://tavis.net/rels/speaker\",\r\n"
    		+ "                        \"href\": \"https://conferenceapi.azurewebsites.net/speaker/19\"\r\n"
    		+ "                    },\r\n"
    		+ "                    {\r\n"
    		+ "                        \"rel\": \"http://tavis.net/rels/topics\",\r\n"
    		+ "                        \"href\": \"https://conferenceapi.azurewebsites.net/session/100/topics\"\r\n"
    		+ "                    }\r\n"
    		+ "                ]\r\n"
    		+ "            },\r\n"
    		+ "            {\r\n"
    		+ "                \"href\": \"https://conferenceapi.azurewebsites.net/session/101\",\r\n"
    		+ "                \"data\": [{\r\n"
    		+ "                        \"name\": \"Title\",\r\n"
    		+ "                        \"value\": \"\\r\\n\\t\\t\\tAsync in C# 5\\r\\n\\t\\t\"\r\n"
    		+ "                    },\r\n"
    		+ "                    {\r\n"
    		+ "                        \"name\": \"Timeslot\",\r\n"
    		+ "                        \"value\": \"04 December 2013 10:20 - 11:20\"\r\n"
    		+ "                    },\r\n"
    		+ "                    {\r\n"
    		+ "                        \"name\": \"Speaker\",\r\n"
    		+ "                        \"value\": \"Jon Skeet\"\r\n"
    		+ "                    }\r\n"
    		+ "                ],\r\n"
    		+ "                \"links\": [{\r\n"
    		+ "                        \"rel\": \"http://tavis.net/rels/speaker\",\r\n"
    		+ "                        \"href\": \"https://conferenceapi.azurewebsites.net/speaker/6\"\r\n"
    		+ "                    },\r\n"
    		+ "                    {\r\n"
    		+ "                        \"rel\": \"http://tavis.net/rels/topics\",\r\n"
    		+ "                        \"href\": \"https://conferenceapi.azurewebsites.net/session/101/topics\"\r\n"
    		+ "                    }\r\n"
    		+ "                ]\r\n"
    		+ "            }],\r\n"
    		+ "        \"queries\": [],\r\n"
    		+ "        \"template\": {\r\n"
    		+ "            \"data\": []\r\n"
    		+ "        }\r\n"
    		+ "    }\r\n"
    		+ "}";
}
