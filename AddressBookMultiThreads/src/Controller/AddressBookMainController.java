/**
 * Created by Valoote on 01/02/14.
 */


package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

//Import from the Model and the View
import Model.ContactModel;
import Model.ContactsFileModel;
import View.AddressBookMainView;
import View.NewContactView;
import View.UpdateContactView;


public class AddressBookMainController {
	private AddressBookMainView view;
	private UpdateContactView view2;
	private ContactsFileModel man= new ContactsFileModel("contactsListFile.txt");
	private Thread t1;
	private Thread t2;
	
	public AddressBookMainController(AddressBookMainView view) {

		this.view = view;

		this.view.ouvrirFenetreListener(new AddNewContact());
		this.view.supprimerContactListener(new DeleteContact());
		this.view.afficherInfoContactListener(new InformationContact());
		this.view.shearchContactListener(new SearchContact());
		this.view.modifierContactListener(new UpdateContact());
		this.view.afficherGroupeContactListener(new GroupOfContact());
	}

	// AddContact button action
	class AddNewContact implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			NewContactView newFrame = new NewContactView();
			NewContactController con=new NewContactController(newFrame);
		}
	}

	// DeleteContact button action
	class DeleteContact implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			t1 = new Thread(new groupContactListThreadAction());
			t1.start();
			
		}

	}

	// SearchContact button
	class SearchContact implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String lastNameFirstNameSearch =null;
			lastNameFirstNameSearch =view.getJtf1();
			view.afficherInfoContactRecherche(lastNameFirstNameSearch);
		}

	}

	// UpdateContact button
	class UpdateContact implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			t2=new Thread(new UpdateThreadAction());
			t2.start();
	
		}
	}
	
	//Action when a group is selected, display the list of contacts of that grp
	class GroupOfContact implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {

			String group =view.getJlistGrp().getSelectedValue().toString();
			System.out.println(group);
			if(group.equals("Friends")) view.remplirJlistAmis();
			else if(group.equals("Family")) view.remplirJlistFamille();
			else if(group.equals("Work")) view.remplirJlistTravail();
			
			else view.remplirJlist(); 
			
		
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		
	}
	
	// Action when a contact is selected
	class InformationContact implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("Contact information are displayed");

			view.afficherInfoContact();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}


    //Thread to display Group Contact List
	class groupContactListThreadAction implements Runnable{

		public void run() {
			ContactModel contact1 = new ContactModel("", "", "", "", "", "", "","");
			contact1 = man.searchContact(view.getJlist().getSelectedValue().toString());
			String groupe = contact1.getGroupe();
			
			try {
				man.deleteContact(contact1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			view.remplirArrayList();
			if(groupe.equals("Amis")) view.remplirJlistAmis();
			else if(groupe.equals("Famille")) view.remplirJlistFamille();
			else view.remplirJlistTravail();
			view.remplirJlist();
			
		}
		
	}


    // Thread for the Update of a contact
	class UpdateThreadAction implements Runnable{

		@Override
		public void run() {
			ContactModel contact2 = new ContactModel("","","","","","","","");
			if(view.getJlist().getSelectedValue()==null){
				javax.swing.JOptionPane.showMessageDialog(null, "Please select a contact in order to update eventual information");
				return;
			}

			contact2 = man.searchContact(view.getJlist().getSelectedValue().toString());
			System.out.println("The selected contact is "+ contact2);
			UpdateContactView view2= new UpdateContactView(contact2);
			UpdateContactController con2=new UpdateContactController(view2);
			
		}
		
	}
	
}
