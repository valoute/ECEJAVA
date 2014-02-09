/**
 * Created by Valoote on 01/02/14.
 */


package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//Import Model and View Package
import Model.ContactModel;
import Model.ContactsFileModel;
import View.AddressBookMainView;
import View.UpdateContactView;


public class UpdateContactController {
	private UpdateContactView updateFrame;
	private AddressBookMainView mainFrame;
	private ContactsFileModel contactFile = new ContactsFileModel("contactsListFile.txt");
	private Thread t;
	
	public UpdateContactController(UpdateContactView updateFrame){
		this.updateFrame = updateFrame;
		
		this.updateFrame.updateContactListener(new classModContact());
	}


	class classModContact implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			t=new Thread(new updateContactThreadAction());
			t.start();
			updateFrame.dispose();
		}
		
	}

    //Update contact Thread Runnable action
	class updateContactThreadAction implements Runnable{

		@Override
		public void run() {
			
			ContactModel newContact =new ContactModel(updateFrame.getJtf1(), updateFrame.getJtf2(), updateFrame.getJtf3(), updateFrame.getJtf4(), updateFrame.getJtf5(), updateFrame.getJtf6(), updateFrame.getJtf7(), updateFrame.getCombo());
			ContactModel contact = new ContactModel("","","","","","","","");
			System.out.println("******** " + mainFrame.getJlist().getSelectedValue().toString());
			contact = contactFile.searchContact(mainFrame.getJlist().getSelectedValue().toString());
			System.out.println("Old information "+ contact);
			System.out.println("Updated information "+ newContact);


			// Deletion of the old information  in the file
			try {
				contactFile.deleteContact(contact);
				mainFrame.FillArrayList();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Add the updated contact in the file
			try {
				contactFile.addContact(newContact);
				mainFrame.FillArrayList();
				String groupe = updateFrame.getCombo();
				if(groupe.equals("Friends")) mainFrame.fillJlistFriends();
				else if(groupe.equals("Family")) mainFrame.fillJlistFamily();
				else mainFrame.fillJlistWork();
				mainFrame.fillJlist();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		}
		
	}
	
}
