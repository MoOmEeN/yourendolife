package com.moomeen;

import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vaadin.spring.servlet.SpringAwareVaadinServlet;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

@SuppressWarnings("serial")
@Service
public class EndoVaadinServlet extends SpringAwareVaadinServlet {
	
	private static Logger LOG = LoggerFactory.getLogger(EndoVaadinServlet.class);

	private final static String KEYWORDS = "endomondo, tracking, workouts, analysis";
	private final static String DESCRIPTION = "yourENDOlife is a tool that brings more value to your Endomondo workouts. Geolocation, workouts street view, custom best distances and more!";
	
	private final static String[] OPENGRAPH_TAGS = {
		asString("description", DESCRIPTION),
		asString("title","yourENDOlife"), 
		asString("url","https://www.yourendolife.com"), 
		asString("type","website"),
		asString("image", "https://www.yourendolife.com/VAADIN/themes/mytheme/img/logo.jpg"),
	};
	
	private final static String[] OTHER_META_TAGS = {
		asString("description", DESCRIPTION),
		asString("keywords", KEYWORDS),
		asString("fragment", "!"), // crawling
		asString("google-site-verification", "c--ynQQuafe6jDVyx8fxM3-QqP4hcXLsJiL2PZC6dkU"),
	};
//		asString("locale",":en_US"),
	
	private static String asString(String property, String content){
		return property+"="+content;
	}
	
	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(new SessionInitListener() {
			@Override
			public void sessionInit(SessionInitEvent event) throws ServiceException {
				event.getSession().addBootstrapListener(new BootstrapListener() {
							@Override
							public void modifyBootstrapPage(BootstrapPageResponse response) {
								for (String og_tag : OPENGRAPH_TAGS) {
									String[] tag = split(og_tag);
									addMetaHeader(response, Property.valueOf("property", toOpenGraphProperty(tag[0])), Property.valueOf("content", tag[1]));
								}
								for (String meta_tag : OTHER_META_TAGS) {
									String[] tag = split(meta_tag);
									addMetaHeader(response, Property.valueOf("name", tag[0]), Property.valueOf("content", tag[1]));
								}
							}

							private String[] split(String tag) {
								return tag.split("=");
							}

							@Override
							public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
							}
						});
			}
		});
	}
	
	private static String toOpenGraphProperty(String key){
		return "og:"+key;
	}
	
	private static void addMetaHeader(BootstrapPageResponse response, Property... properties){
		Element meta = response.getDocument().head().prependElement("meta");
		for (Property property : properties) {
			meta.attr(property.key, property.value);
		}		
	}
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (isCrawlerRequest(request)){
			LOG.debug("Crawler detected, returning static html");
			response.setContentType("text/html");
			Writer writer = response.getWriter();
			String staticHTML = readResourceFile("VAADIN/static.html");
			writer.append(staticHTML);
		} else
			super.service(request, response);
	}
	
	private boolean isCrawlerRequest(HttpServletRequest request){
		return request.getParameter("_escaped_fragment_") != null;
	}
	
	private String readResourceFile(String path){
		Scanner s = new Scanner(EndoVaadinServlet.class.getClassLoader().getResourceAsStream(path), "UTF-8");
		try {
			s.useDelimiter("\\A");
			return s.next();
		} finally {
			s.close();
		}
	}
	
	private static class Property {
		String key;
		String value;
		
		public static Property valueOf(String key, String value){
			Property p = new Property();
			p.key = key;
			p.value = value;
			return p;
		}
	}

}
