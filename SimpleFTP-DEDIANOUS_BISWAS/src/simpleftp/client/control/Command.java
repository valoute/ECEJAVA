package simpleftp.client.control;

public enum Command {

	CONNECT("CONNECT"), DISCONNECT("DISCONNECT"), CLEAR("CLEAR"), SAVEPREFS(
			"SAVEPREFS"), LOADPREFS("LOADPREFS"), DELPREF("DELPREF"), PWD("PWD"), LIST(
			"LIST"), CWD("CWD"), PASV("PASV"), RETR("RETR"), STOR("STOR"), QUIT(
			"QUIT");

	private final String name;

	private Command(String nom) {
		name = nom;
	}

	public String getName() {
		return name;
	}
}
