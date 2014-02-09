/**
 * Created by Valoote on 01/02/14.
 */


package View;


import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.*;

import Model.ContactModel;
import Model.ContactsFileModel;


public class AddressBookMainView extends JFrame{

    //As usual
	private static final long serialVersionUID = 1L;

    //File class object
	private static ContactsFileModel conFile = new ContactsFileModel("contactsListFile.txt");

	//JPanel
	private JPanel JPmain = new JPanel(new GridLayout(1, 3));
	private JPanel JPsearch = new JPanel(); //1st Panel
	private JPanel JPListContacts = new JPanel();// 2nd Panel
	private JPanel JPInfoContact = new JPanel();// 3rd Panel
	private JPanel JPContainInfo =new JPanel();

	//JButton
	private JButton searchButton =new JButton();
	private JButton updateButton = new JButton("Update contact");
	private JButton AddButton = new JButton("Add  contact");
	private JButton deleteButton =new JButton("Delete contact");
    private JButton adbButton =new JButton();
    private JButton adbButton2 =new JButton();
    private JButton picture =new JButton();


	//JLabel
	private JLabel JLinfo = new JLabel("               CONTACT INFO");
	private JLabel JLlist = new JLabel("CONTACTS LIST");
	private JLabel JLgroup = new JLabel("GROUPS");

	//Contact JList
	private static List<ContactModel> contacts;
	private static DefaultListModel model;
	private static JList jlist;
	
	//Group JList
	private static List<ContactModel> family = new ArrayList<ContactModel>();
	private static List<ContactModel> friends = new ArrayList<ContactModel>();
	private static List<ContactModel> work = new ArrayList<ContactModel>();
	private static DefaultListModel modelGrp;
	private static JList jlistGrp;

	//JTextField
	private JTextField jtextSearch = new JTextField("LastName   FirstName");
	
	public AddressBookMainView(){

        //Color
        jtextSearch.setForeground(Color.red);
        JPmain.setBackground(Color.CYAN);
        JPsearch.setBackground(Color.CYAN);
        JPInfoContact.setBackground(Color.CYAN);
        JPListContacts.setBackground(Color.CYAN);
        JPContainInfo.setBackground(Color.CYAN);

        //Icon
        searchButton.setIcon(new ImageIcon("./img/Capture.jpg"));
        adbButton.setIcon(new ImageIcon("./img/Capture1.jpg"));
        adbButton2.setIcon(new ImageIcon("./img/Capture1.jpg"));
        picture.setIcon(new ImageIcon("./img/Capture2.jpg"));

        //Size
        deleteButton.setPreferredSize(new Dimension(150, 30));
        updateButton.setPreferredSize(new Dimension(150, 30));
        AddButton.setPreferredSize(new Dimension(150, 30));
        searchButton.setPreferredSize(new Dimension(150, 30));
        adbButton.setPreferredSize(new Dimension(60, 60));
        adbButton2.setPreferredSize(new Dimension(60, 60));
        picture.setPreferredSize(new Dimension(159, 100));
        jtextSearch.setPreferredSize(new Dimension(150, 30));

		model = new DefaultListModel();
		contacts = new ArrayList<ContactModel>();
		jlist = new JList(model);
		
		
		modelGrp=new DefaultListModel();
		jlistGrp=new JList(modelGrp);
		
		remplirJlist();
		remplirJlist2();
		remplirArrayList();
		
		Font police = new Font("Helvetica", Font.ITALIC, 14);

		JLinfo.setFont(police);
		JLlist.setFont(police);
		JLgroup.setFont(police);
        JLinfo.setForeground(Color.ORANGE);
        JLlist.setForeground(Color.ORANGE);
        JLgroup.setForeground(Color.ORANGE);

		
		JPsearch.add(JLgroup);
		JPsearch.add(jlistGrp);
		jlistGrp.setFixedCellWidth(150);
		jlistGrp.setVisibleRowCount(10);
		JScrollPane scrollPane2 = new JScrollPane(jlistGrp);
		JPsearch.add(scrollPane2);
        JPsearch.add(picture);
		JPsearch.add(jtextSearch);
		JPsearch.add(searchButton);
        //validate();
		JPsearch.add(AddButton);
        JPsearch.add(adbButton2);
		
		JPListContacts.add(JLlist);
		JPListContacts.add(jlist);
		jlist.setFixedCellWidth(150);
		jlist.setVisibleRowCount(20);
		JScrollPane scrollPane = new JScrollPane(jlist);
		JPListContacts.add(scrollPane);
		JPListContacts.add(deleteButton);
        JPListContacts.add(adbButton);




        //BorderLayout for the second column
		JPInfoContact.setLayout(new BorderLayout());
        JPInfoContact.add(JLinfo,BorderLayout.NORTH);
		JPInfoContact.add(JPContainInfo,BorderLayout.CENTER);
        JPInfoContact.add(updateButton,BorderLayout.SOUTH);



		
		
		//Frame Init
		this.setTitle("Address Book 2013-2014 ECE");
		this.setSize(630, 535);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(JPmain);

        //ADD to main Pane
        this.getContentPane().add(JPListContacts);
        this.getContentPane().add(JPInfoContact);
		this.getContentPane().add(JPsearch);

		this.setVisible(true);
	}
	

	public ContactsFileModel getBookFile() {
		return conFile;
	}

	public void setBookFile(ContactsFileModel conFile) {
		this.conFile = conFile;
	}
	
	public String getJtf1(){
		return jtextSearch.getText();
	}	
	
	public static JList getJlist() {
		return jlist;
	}

	public static void setJlist(JList jlist) {
		AddressBookMainView.jlist = jlist;
	}
	
	public static JList getJlistGrp() {
		return jlistGrp;
	}
	
	//Method of button add action
			
		public void ouvrirFenetreListener(ActionListener listenForBoutonAjouter){
			AddButton.addActionListener(listenForBoutonAjouter);

			
		}
			
		public void supprimerContactListener(ActionListener listenForSuppButton){
				deleteButton.addActionListener(listenForSuppButton);

		}
		
		public void afficherInfoContactListener(MouseListener ok){
			jlist.addMouseListener(ok);
			
		}
		
		public void afficherGroupeContactListener(MouseListener ok){
			jlistGrp.addMouseListener(ok);

		}

		public void shearchContactListener(ActionListener b){
			searchButton.addActionListener(b);

		}
		public void modifierContactListener(ActionListener c){
			updateButton.addActionListener(c);

		}
		

		//FONCTION ANEX	 
		public static void remplirArrayList(){
			contacts.clear();
			family.clear();
			friends.clear();
			work.clear();
			try {
				contacts = conFile.getContactList();
			} catch (FileNotFoundException e1) {
				javax.swing.JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			
			for (int i=0 ; i<contacts.size() ; i++){	
				if(contacts.get(i).getGroupe().equals("Family"))
					family.add(contacts.get(i));
				if(contacts.get(i).getGroupe().equals("Friends"))
					friends.add(contacts.get(i));
				if(contacts.get(i).getGroupe().equals("Work"))
					work.add(contacts.get(i));
			}

		}
		
		public static void remplirJlist(){
			contacts.clear();
			jlist.removeAll();
			model.clear();
			
			
			try {
				contacts = conFile.getContactList();
			} catch (FileNotFoundException e1) {
				javax.swing.JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			Collections.sort(contacts);
			for (int i=0 ; i<contacts.size() ; i++){	
				model.addElement(contacts.get(i).getLastName() + " "+ contacts.get(i).getFirstName());
			}
		}
		
		public static void remplirJlistFamille(){
			jlist.removeAll();
			model.clear();			
			Collections.sort(family);
			for (int i=0 ; i< family.size() ; i++){
				model.addElement(family.get(i).getLastName() + " "+ family.get(i).getFirstName());
			}

		}
		public static void remplirJlistAmis(){
			jlist.removeAll();
			model.clear();			
			Collections.sort(friends);
			for (int i=0 ; i< friends.size() ; i++){
				model.addElement(friends.get(i).getLastName() + " "+ friends.get(i).getFirstName());
			}

		}
		public static void remplirJlistTravail(){
			jlist.removeAll();
			model.clear();			
			Collections.sort(work);
			for (int i=0 ; i< work.size() ; i++){
				model.addElement(work.get(i).getLastName() + " "+ work.get(i).getFirstName());
			}

		}
		
		public static void remplirJlist2(){
			String[] choixGrp={"All","Family","Friends","Work"};
			for (int i=0 ; i<choixGrp.length ; i++){
				modelGrp.addElement(choixGrp[i]);
			}
		}
		
		public void afficherInfoContact(){
			JLabel nom=null;
			JPContainInfo.removeAll();
			ContactModel cont_test = new ContactModel("", "", "", "", "", "", "","");
			ContactsFileModel m=new ContactsFileModel();
			cont_test = m.searchContact(jlist.getSelectedValue().toString());
			if (cont_test!=null){
				nom = new JLabel("<html><br><br><strong> Last Name : </strong>" + cont_test.getLastName()
						+ "<br><br><br><strong> First Name : </strong>" + cont_test.getFirstName()
						+ "<br><br><br><strong> Address 1 : </strong>" + cont_test.getAddress1()
						+ "<br><br><br><strong> Address 2 : </strong>" + cont_test.getAddress2()
						+ "<br><br><br><strong> Phone Number 1 : </strong>" + cont_test.getPhoneNb1()
						+ "<br><br><br><strong> Phone Number 2 : </strong>" + cont_test.getPhoneNb2()
						+ "<br><br><br><strong> M@il : </strong>" + cont_test.getMail()
						+ "<br><br><br><strong> Group : </strong>" + cont_test.getGroupe() + "</br></html>");
				JPContainInfo.add(nom);

				JPContainInfo.validate();
				JPContainInfo.setVisible(true);
				}else {
					nom = new JLabel("");
					javax.swing.JOptionPane.showMessageDialog(null, "This Contact does not exist");
					JPsearch.add(nom);
					this.setVisible(true);
				}
		}
		
		public void viderDernPanel(){
			JPContainInfo.removeAll();
		}
		
		public void afficherInfoContactRecherche(String nomPrenom){
			JLabel nom=null;
			JPContainInfo.removeAll();
			ContactModel cont_test = new ContactModel("", "", "", "", "", "", "","");
			ContactsFileModel m=new ContactsFileModel();
			cont_test = m.searchContact(nomPrenom);
			if (cont_test!=null){
				nom = new JLabel("<html><br><strong> Last Name : </strong>" + cont_test.getLastName()
						+ "<br><br><strong> First Name : </strong>" + cont_test.getFirstName()
						+ "<br><br><strong> Adress 1 : </strong>" + cont_test.getAddress1()
						+ "<br><br><strong> Adress 2 : </strong>" + cont_test.getAddress2()
						+ "<br><br><strong> Phone Number 1 : </strong>" + cont_test.getPhoneNb1()
						+ "<br><br><strong> Phone Number 2 : </strong>" + cont_test.getPhoneNb2()
						+ "<br><br><strong> M@il : </strong>" + cont_test.getMail()
						+ "<br><br><strong> Group : </strong>" + cont_test.getGroupe() + "</br></html>");;
				JPContainInfo.add(nom);
				JPContainInfo.validate();
				JPContainInfo.setVisible(true);
				}else {
					nom = new JLabel("");
					javax.swing.JOptionPane.showMessageDialog(null, "This Contact does not exist.");
					JPsearch.add(nom);
					this.setVisible(true);
				}
		}

	
}