/**
 * Created by Valoote on 01/02/14.
 */


package Model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



public class ContactsFileModel {
	//Two attributes; pathOfFile in order to contain the string of the path of the file and an object of fileWriter
	private String pathOfFile;
	private FileWriter file;

	
	public ContactsFileModel(String path_file)
	{
		pathOfFile = path_file;
	}
	
	public ContactsFileModel(){
	}
	
	public void setPathOfFile(String PATH)
	{
		pathOfFile = PATH;
	}
	
	public void addContact(ContactModel contactMod) throws IOException
	{
		file = new FileWriter(new File(pathOfFile), true);

        //We add the  contact infos into the file
		for (int i=0 ; i<8 ; i++)
		{
			switch(i)
			{
			case 0:
				file.append(contactMod.getLastName() + "\r\n");
				break;
				
			case 1:
				file.append(contactMod.getFirstName() + "\r\n");
				break;
				
			case 2:
				file.append(contactMod.getAddress1() + "\r\n");
				break;
				
			case 3:
				file.append(contactMod.getAddress2() + "\r\n");
				break;
				
			case 4:
				file.append(contactMod.getPhoneNb1() + "\r\n");
				break;
			
			case 5:
				file.append(contactMod.getPhoneNb2() + "\r\n");
				break;
		
			case 6:
				file.append(contactMod.getMail() + "\r\n");
				break;
			case 7:
				file.append(contactMod.getGroupe() + "\r\n");
			}
		}
            //Then we close the file.
			file.close();
	}
	
	public static List<ContactModel> getContactList() throws FileNotFoundException
	{
		List<ContactModel> list = new ArrayList<ContactModel>();
		List<String> line = new ArrayList<String>();
		Scanner scanner = new Scanner(new File("contactsListFile.txt"));

        //Read by line
		while(scanner.hasNextLine())
		{
			line.add(scanner.nextLine());
		}
		scanner.close();
		
		for (int i=0 ; i< line.size() ; i+=8)
		{
			ContactModel contactmod = new ContactModel();
			
			for (int j=0 ; j<8 ; j++)
			{
				if(j==0)
					contactmod.setLastName(line.get(i));
				if(j==1)
					contactmod.setFirstName(line.get(i + 1));
				if(j==2)
					contactmod.setAddress1(line.get(i + 2));
				if(j==3)
					contactmod.setAddress2(line.get(i + 3));
				if(j==4)
					contactmod.setPhoneNb1(line.get(i + 4));
				if(j==5)
					contactmod.setPhoneNb2(line.get(i + 5));
				if(j==6)
					contactmod.setMail(line.get(i + 6));
				if(j==7)
					contactmod.setGroupe(line.get(i + 7));
			}
			
			list.add(contactmod);
		}
		return list;
	}
	
	
	public static ContactModel searchContact(String lastName_firstName) {
		List<ContactModel> contactsList = new ArrayList<ContactModel>();
		try {
			contactsList = getContactList();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name[] = lastName_firstName.split(" ");
		Iterator<ContactModel> itr = contactsList.iterator();
		while (itr.hasNext()) {
			ContactModel contact = itr.next();
			if (contact.getLastName().equals(name[0]) && contact.getFirstName().equals(name[1])) 
				return contact;	
			}
		return null;
}
	
	public void deleteContact(ContactModel cont) throws IOException{
	
		List<ContactModel> contacts = new ArrayList<ContactModel>();
		
		try {
			contacts= getContactList();
			System.out.println(contacts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		int position;
		for (int i=0; i<contacts.size(); i++){
			if(contacts.get(i).getLastName().equals(cont.getLastName())&& contacts.get(i).getFirstName().equals(cont.getFirstName())){
				position =i;
				System.out.println("First Name: "+ contacts.get(i).getFirstName()+":info deleted, ex position in the list "+(i+1) );
				contacts.remove(position);
				
			}
		}

		
		FileWriter fw=new FileWriter("contactsListFile.txt",false);

		for(int i=0; i<contacts.size(); i++){
			
		fw.write(contacts.get(i).getLastName()+"\r\n");
		fw.write(contacts.get(i).getFirstName()+"\r\n");
		fw.write(contacts.get(i).getAddress1()+"\r\n");
		fw.write(contacts.get(i).getAddress2()+"\r\n");
		fw.write(contacts.get(i).getPhoneNb1()+"\r\n");
		fw.write(contacts.get(i).getPhoneNb2()+"\r\n");
		fw.write(contacts.get(i).getMail()+"\r\n");
		fw.write(contacts.get(i).getGroupe()+"\r\n");
		
		}
		fw.close();
	}
	
}
