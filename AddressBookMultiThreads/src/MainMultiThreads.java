/**
 * Created by Valoote on 01/02/14.
 */

import Controller.AddressBookMainController;
import View.AddressBookMainView;

public class MainMultiThreads {


    public static void main( String[] args){
        System.out.println("*********************************");
        System.out.println("*********************************");
        System.out.println("Welcome in the MultiThreads version of the AddressBook 2013-2014");
        System.out.println("*********************************");
        System.out.println("*********************************");

        //Creation and Initialisation of the mainView and the mainController of the addressBook
        AddressBookMainView view=new AddressBookMainView();
        AddressBookMainController control =new AddressBookMainController(view);

    }

}
