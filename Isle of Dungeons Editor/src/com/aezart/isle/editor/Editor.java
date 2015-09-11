package com.aezart.isle.editor;

import javax.swing.SwingUtilities;

public class Editor {
	public static void main(String[] args){
		SwingUtilities.invokeLater(()->{
			MainWindow mw = new MainWindow();

		});
	}
}
