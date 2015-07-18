package com.chc.ocr.test;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import test.com.liang.TestOCR;

public class TestMain {

	public static void main(String[] args) throws Exception {
//      String userdir = System.getProperty("user.dir");
//      File tempFile = new File("d:", "temp.png");
      ScreenCapture capture = ScreenCapture.getInstance();
      capture.captureImage();
      JFrame frame = new JFrame();
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      JLabel imagebox = new JLabel();
      panel.add(BorderLayout.CENTER, imagebox);
      imagebox.setIcon(capture.getPickedIcon());
//      capture.saveAsPNG(tempFile);
      capture.captureImage();
      String character = TestOCR.recognizeCharacter(capture.getPickedImage());
      System.out.println(character);
      imagebox.setIcon(capture.getPickedIcon());
      frame.setContentPane(panel);
      frame.setSize(400, 300);
      frame.show();
	}

}
