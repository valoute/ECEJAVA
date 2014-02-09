/**
 * Created by Valoote on 01/02/14.
 */


package View;


import Model.ContactModel;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateContactView extends JFrame {

	private JTextField jtf1;
	private JTextField jtf2;
	private JTextField jtf3;
	private JTextField jtf4;
	private JTextField jtf5;
	private JTextField jtf6;
	private JTextField jtf7;

	private JButton b = new JButton("OK");



    private JLabel lastName = new JLabel("   Last Name:");
    private JLabel firstName = new JLabel("   First Name:");
    private JLabel address1 = new JLabel("   Adress 1:");
    private JLabel address2 = new JLabel("   Adress 2:");
    private JLabel phoneNb1 = new JLabel("   Phone Number 1:");
    private JLabel phoneNb2 = new JLabel("   Phone Number 2:");
    private JLabel mail = new JLabel("   M@il:");
    private JLabel group = new JLabel("   Group:");
	
	private String[] choixGrp={"All","Family","Friends","Work"};
	private JComboBox combo=new JComboBox(choixGrp);

	public UpdateContactView(ContactModel cont) {
		jtf1 = new JTextField(cont.getLastName());
		jtf2 = new JTextField(cont.getFirstName());
		jtf3 = new JTextField(cont.getAddress1());
		jtf4 = new JTextField(cont.getAddress2());
		jtf5 = new JTextField(cont.getPhoneNb1());
		jtf6 = new JTextField(cont.getPhoneNb2());
		jtf7 = new JTextField(cont.getMail());
		JPanel pan = new JPanel(new GridLayout(9, 1));

		pan.add(lastName);
		pan.add(jtf1);
		pan.add(firstName);
		pan.add(jtf2);
		pan.add(address1);
		pan.add(jtf3);
		pan.add(address2);
		pan.add(jtf4);
		pan.add(phoneNb1);
		pan.add(jtf5);
		pan.add(phoneNb2);
		pan.add(jtf6);
		pan.add(mail);
		pan.add(jtf7);
		combo.setPreferredSize(new Dimension(100,20));
		pan.add(group);
		pan.add(combo);
		pan.add(b);


		//comboBox Listener

		combo.addActionListener(new ItemAction());
		
		this.setBounds(0, 0, 300, 300);
		this.setLocationRelativeTo(null);
		this.setTitle("Update a Contact");
		this.setVisible(true);
		this.setContentPane(pan);
	}

	
	public String getJtf1(){
		return jtf1.getText();
	}
	public String getJtf2(){
		return jtf2.getText();
	}
	public String getJtf3(){
		return jtf3.getText();
	}
	public String getJtf4(){
		return jtf4.getText();
	}
	public String getJtf5(){
		return jtf5.getText();
	}
	public String getJtf6(){
		return jtf6.getText();
	}
	public String getJtf7(){
		return jtf7.getText();
	}
	public String getCombo() {
		ItemAction ia=new ItemAction();
		String textReunion =ia.getTextReunion();
		return textReunion;
	}

		
		public void modifierContactListener(ActionListener listenForOKButton){
			b.addActionListener(listenForOKButton);
		}
		
		class ItemAction implements ActionListener{
			public String textReunion;

			public String getTextReunion() {
				 textReunion =combo.getSelectedItem().toString();
				System.out.println("*******UPDATE******* \n"+ textReunion);
				return textReunion;
			}

			public void actionPerformed(ActionEvent e){
				String comboText=combo.getSelectedItem().toString();
				System.out.println(comboText);
			}
		}
		
		
}
