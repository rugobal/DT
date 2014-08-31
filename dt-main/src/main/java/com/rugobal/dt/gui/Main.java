package com.rugobal.dt.gui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	
	public static void main(String[] args) {
		
		// open/read the application context file
	    final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	    
	    ctx.registerShutdownHook();
	 
	    // instantiate our spring dao object from the application context
	    final UploadTradesApp uploadTradesApp = ctx.getBean(UploadTradesApp.class);
	    
	    uploadTradesApp.addWindowListener( new WindowAdapter() {
	    	public void windowClosing( WindowEvent e ) {
	    		System.exit(0);
	    	}
	    });
	    
	    
	    EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	uploadTradesApp.setVisible(true); 
            }
        });
	}

}
