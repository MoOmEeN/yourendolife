package com.moomeen;

import javax.servlet.ServletException;

import org.jsoup.nodes.Element;
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

	private final static String KEYWORDS = "endomondo, tracking, workouts, analysis";
	private final static String DESCRIPTION = "yourENDOlife is a tool that brings more value to your Endomondo workouts. Geolocation, workouts street view, custom best distances and more!";
	
	private final static String[] OPENGRAPH_TAGS = {
		asString("description", DESCRIPTION),
		asString("title","yourENDOlife"), 
		asString("url","https://www.yourendolife.com"), 
		asString("type","website"),
		asString("image", "https://www.yourendolife.com/VAADIN/themes/mytheme/img/logo.jpg")
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
								addMetaHeader(response, Property.valueOf("name", "description"), Property.valueOf("content", DESCRIPTION));
								addMetaHeader(response, Property.valueOf("name", "keywords"), Property.valueOf("content", KEYWORDS));
								for (String og_tag : OPENGRAPH_TAGS) {
									String[] tag = og_tag.split("=");
									addMetaHeader(response, Property.valueOf("property", toOpenGraphProperty(tag[0])), Property.valueOf("content", tag[1]));
								}
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
