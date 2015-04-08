package com.moomeen.views.main;

import static com.moomeen.utils.ImageResourceLoader.*;

import java.io.IOException;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Video;
import com.vaadin.util.FileTypeResolver;

@SuppressWarnings("serial")
public class StreetViewStripe extends HorizontalLayout {
	
	static {
		FileTypeResolver.addExtension("webm", "video/webm");
	}
	
	public StreetViewStripe() {
		setWidth("100%");
		setStyleName("main-view-street");
		
		VerticalLayout videoWrapper = new VerticalLayout();
		videoWrapper.setSpacing(false);
		Image browserBarImage = fromResourceImage("toolbar.png");
		browserBarImage.setStyleName("street-view-video-bar");
		Label url = new Label("https://yourendolife.com/#!stats");
		url.setStyleName("street-view-video-bar-url");
		url.setWidthUndefined();
		videoWrapper.addComponent(url);
		videoWrapper.addComponent(browserBarImage);
		Video video = new Video();
		try {
			video.setSources(resourceFromFile("streetview.webm"));
			video.setAutoplay(true);
			video.setCaption(null);
			video.setStyleName("street-view-video");
			Panel p = new Panel();
			p.setStyleName("street-view-video-panel");
			p.setContent(video);
			videoWrapper.addComponent(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		video.setWidth( "600px" );
		Panel textPanel = new Panel();
		Label text = new Label();
		text.setStyleName("h1");
		text.setValue("Replay your workout using street view images");
		textPanel.setContent(text);
		
		addComponent(videoWrapper);
		addComponent(textPanel);
		setComponentAlignment(videoWrapper, Alignment.MIDDLE_LEFT);
		setComponentAlignment(textPanel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(videoWrapper, 1.5f);
		setExpandRatio(textPanel, 1.0f);
	}
	
	

}
