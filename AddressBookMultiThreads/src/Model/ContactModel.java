/**
 * Created by Valoote on 01/02/14.
 */


package Model;



public class ContactModel implements Comparable{

    //Attributes of our classic  Address Book
	private String lastName;
	private String firstName;
	private String address1;
	private String address2;
	private String phoneNb1;
	private String phoneNb2;
	private String mail;
	private String groupe;

	
	
	public ContactModel(){
        //Constructor
	}
	
	public ContactModel(String lastN, String firstN, String add1, String add2, String phone1, String phone2, String m, String grp){
		
		this.lastName= lastN;
		this.firstName= firstN;
		this.address1 = add1;
		this.address2 = add2;
		this.phoneNb1 = phone1;
		this.phoneNb2 = phone2;
		this.mail=m;
		this.groupe= grp;

		
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPhoneNb1() {
		return phoneNb1;
	}

	public void setPhoneNb1(String phoneNb1) {
		this.phoneNb1 = phoneNb1;
	}

	public String getPhoneNb2() {
		return phoneNb2;
	}

	public void setPhoneNb2(String phoneNb2) {
		this.phoneNb2 = phoneNb2;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getGroupe() {
		return groupe;
	}

	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}


	// In order to work with the contactsListFile.txt
	public String toString(){
		return this.getLastName() + ","+
				 this.getFirstName() + ","+
				 this.getAddress1() + ","+
				 this.getAddress2() + ","+
				 this.getPhoneNb1() + ","+
				 this.getPhoneNb2() + ","+
				this.getMail() + ","+
				 this.getGroupe()+"\n";
		}


    //Overriding of the compareTo function in order to compares FirstNames and LastNames
	@Override
    public int compareTo(Object o) {
		ContactModel contModel =(ContactModel)o;
		if(lastName.equals(contModel.lastName)){
			return firstName.compareTo(contModel.lastName);
		}
		return lastName.compareTo(contModel.lastName);
	}	
}
