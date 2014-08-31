package com.rugobal.dt.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.springframework.stereotype.Component;


@Component
public class UploadTradesApp extends JFrame {
	
    private static final long serialVersionUID = -8109047934193502155L;
    
//    @Inject
//    private ParseAndUploadPanel parseAndUplodJPanel;

    @Inject
	public UploadTradesApp(ParseAndUploadPanel parseAndUplodJPanel, StatisticsPanel statisticsPanel) {

		setTitle("Trade Uploader");
		setSize(1000, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);  
		
		initComponents(parseAndUplodJPanel, statisticsPanel);
	}

	private void initComponents(ParseAndUploadPanel parseAndUplodJPanel, StatisticsPanel statisticsPanel) {
		getContentPane().setLayout(new BorderLayout(5,5));
	    
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Upload", parseAndUplodJPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		tabbedPane.addTab("Statistics", statisticsPanel);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		add(tabbedPane, BorderLayout.CENTER);
    }	
	
}
