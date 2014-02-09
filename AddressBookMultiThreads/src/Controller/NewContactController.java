/**
 * Created by Valoote on 01/02/14.
 */


package Controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Import of the Model and View package
import Model.ContactModel;
import Model.ContactsFileModel;
import View.NewContactView;
import View.AddressBookMainView;

public class NewContactController {

//List of attributes
	private NewContactView newConFrame; // For the New Contact Frame
	private AddressBookMainView mainFrame;
	private ContactsFileModel contactFile = new ContactsFileModel("contactsListFile.txt");
	private AddContact aC =new AddContact();
	private List<ContactModel> listContacts;
	private Thread t;
	
	public NewContactController(NewContactView newConFrame){

		this.newConFrame = newConFrame;
		this.newConFrame.ajouterContactListener(new AddContact());

	}
	
		// Add Contact Button
		public class AddContact implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
		

				
				System.out.println("Time before this thread "+System.currentTimeMillis());
				listContacts = new ArrayList<ContactModel>();
				
				try {
					listContacts = contactFile.getContactList();
				} catch (FileNotFoundException e2) {
					
					e2.printStackTrace();
				}
					t=new Thread(new newContactThreadAction());
					t.start();	
						
					
					mainFrame.remplirArrayList();
					String grp= newConFrame.getCombo();
					if(grp.equals("Friends")) mainFrame.remplirJlistAmis();
					else if(grp.equals("Family")) mainFrame.remplirJlistFamille();
					else mainFrame.remplirJlistTravail();
					mainFrame.remplirJlist();
					
					 System.out.println("Time after this thread " + System.currentTimeMillis());
				
				newConFrame.dispose();
				
		}
	
	}
		
		//New contact Thread Runnable action
		class newContactThreadAction implements Runnable{
		
			
			@Override
			public void run() {
				System.out.println("************Thread Runnable************");
				ContactModel newContact =new ContactModel(newConFrame.getJtf1(), newConFrame.getJtf2(), newConFrame.getJtf3(), newConFrame.getJtf4(), newConFrame.getJtf5(), newConFrame.getJtf6(), newConFrame.getJtf7(), newConFrame.getCombo());
				
					try {
						contactFile.addContact(newContact);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
					}
			}
		}
		

}
