package shared;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class UneditableTextField extends JTextField {
	
	public UneditableTextField(String text){
		super(text);
		setEditable(false);
	}

}
