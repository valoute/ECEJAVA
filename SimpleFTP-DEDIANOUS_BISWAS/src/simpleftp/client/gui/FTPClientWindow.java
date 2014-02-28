package simpleftp.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import simpleftp.client.control.Command;
import simpleftp.client.control.CommandDispatcher;

public class FTPClientWindow extends JFrame {

	private static final long serialVersionUID = -1465072331839508257L;
	public final JTextField tfservername;
	public final JTextField tfusername;
	public final JPasswordField pfuserpass;
	public final JTextField tfport;
	public final JTextArea console;
	public final DefaultMutableTreeNode root;
	public final JTree tree;
	
	private BlockingQueue<Command> cmdQueue;

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JButton btnDownload;
	private JButton btnList;
	private JButton btnUpload;
	private JButton btnDisconnect;
	private JButton btnConnect;
	private JMenuItem mntmSavePreferences;
	private JButton btnClearConsole;
	private JMenuItem mntmDeletePreference;
	private JMenuItem mntmQuitSimpleftp;
	private JSeparator separator;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	private JLabel label_6;
	private JLabel label_7;
	private JLabel label_8;
	private JLabel label_9;
	private JLabel label_10;
	private JLabel label_11;
	private JLabel labe2;
	private JLabel labe2_1;
	private JLabel labe2_2;
	
	private JButton btnPwd;
    private JButton cwd;
   // private  JTextField cwdText;
    
	/**
	 * Create the frame.
	 */
	public FTPClientWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 614, 412);

		console = new JTextArea();
		console.setEditable(false);
		console.setWrapStyleWord(true);
		console.setRows(5);
		console.setColumns(55);

		cmdQueue = new LinkedBlockingQueue<Command>();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					cmdQueue.put(Command.QUIT);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);

		JMenuItem mntmLoadPrefs = new JMenuItem("Load Preferences...");
		mntmLoadPrefs.addActionListener(new GUICommandListener<Command>(
				cmdQueue, console, Command.LOADPREFS));
		mntmLoadPrefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.META_MASK));
		mnFile.add(mntmLoadPrefs);

		mntmSavePreferences = new JMenuItem("Save Preferences...");
		mntmSavePreferences.addActionListener(new GUICommandListener<Command>(
				cmdQueue, console, Command.SAVEPREFS));
		mntmSavePreferences.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.META_MASK));
		mnFile.add(mntmSavePreferences);

		mntmDeletePreference = new JMenuItem("Delete Preference...");
		mntmDeletePreference.addActionListener(new GUICommandListener<Command>(
				cmdQueue, console, Command.DELPREF));
		mntmDeletePreference.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_D, ActionEvent.META_MASK));
		mnFile.add(mntmDeletePreference);

		mntmQuitSimpleftp = new JMenuItem("Quit SimpleFTP");
		mntmQuitSimpleftp.addActionListener(new QuitButtonListener());
		mntmQuitSimpleftp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.META_MASK));

		separator = new JSeparator();
		mnFile.add(separator);
		mnFile.add(mntmQuitSimpleftp);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelConnectionInfo = new JPanel();
		panelConnectionInfo.setBorder(new TitledBorder(new EtchedBorder(),
				"Credentials and Server Address"));
		contentPane.add(panelConnectionInfo, BorderLayout.NORTH);
		GridBagLayout gbl_panelConnectionInfo = new GridBagLayout();
		gbl_panelConnectionInfo.columnWidths = new int[] { 201, 192, 0, 201, 0 };
		gbl_panelConnectionInfo.rowHeights = new int[] { 28, 28, 0 };
		gbl_panelConnectionInfo.columnWeights = new double[] { 0.0, 0.0, 0.0,
				1.0, Double.MIN_VALUE };
		gbl_panelConnectionInfo.rowWeights = new double[] { 0.0, 0.0,
				Double.MIN_VALUE };
		panelConnectionInfo.setLayout(gbl_panelConnectionInfo);

		JLabel lblUsername = new JLabel("Username");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.fill = GridBagConstraints.BOTH;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		panelConnectionInfo.add(lblUsername, gbc_lblUsername);

		tfusername = new JTextField();
		GridBagConstraints gbc_tfusername = new GridBagConstraints();
		gbc_tfusername.fill = GridBagConstraints.BOTH;
		gbc_tfusername.insets = new Insets(0, 0, 5, 5);
		gbc_tfusername.gridx = 1;
		gbc_tfusername.gridy = 0;
		panelConnectionInfo.add(tfusername, gbc_tfusername);
		tfusername.setColumns(10);

		JLabel lblServerName = new JLabel("Server IP address");
		GridBagConstraints gbc_lblServerName = new GridBagConstraints();
		gbc_lblServerName.fill = GridBagConstraints.BOTH;
		gbc_lblServerName.insets = new Insets(0, 0, 5, 5);
		gbc_lblServerName.gridx = 2;
		gbc_lblServerName.gridy = 0;
		panelConnectionInfo.add(lblServerName, gbc_lblServerName);

		tfservername = new JTextField();
		GridBagConstraints gbc_tfservername = new GridBagConstraints();
		gbc_tfservername.insets = new Insets(0, 0, 5, 0);
		gbc_tfservername.fill = GridBagConstraints.BOTH;
		gbc_tfservername.gridx = 3;
		gbc_tfservername.gridy = 0;
		panelConnectionInfo.add(tfservername, gbc_tfservername);
		tfservername.setColumns(10);

		JLabel lblUserPass = new JLabel("Password");
		GridBagConstraints gbc_lblUserPass = new GridBagConstraints();
		gbc_lblUserPass.fill = GridBagConstraints.BOTH;
		gbc_lblUserPass.insets = new Insets(0, 0, 0, 5);
		gbc_lblUserPass.gridx = 0;
		gbc_lblUserPass.gridy = 1;
		panelConnectionInfo.add(lblUserPass, gbc_lblUserPass);

		pfuserpass = new JPasswordField();
		GridBagConstraints gbc_pfuserpass = new GridBagConstraints();
		gbc_pfuserpass.fill = GridBagConstraints.BOTH;
		gbc_pfuserpass.insets = new Insets(0, 0, 0, 5);
		gbc_pfuserpass.gridx = 1;
		gbc_pfuserpass.gridy = 1;
		pfuserpass.setColumns(5);
		panelConnectionInfo.add(pfuserpass, gbc_pfuserpass);

		JLabel lbport = new JLabel("Server Port");
		GridBagConstraints gbc_tfport = new GridBagConstraints();
		gbc_tfport.anchor = GridBagConstraints.EAST;
		gbc_tfport.insets = new Insets(0, 0, 0, 5);
		gbc_tfport.gridx = 2;
		gbc_tfport.gridy = 1;
		panelConnectionInfo.add(lbport, gbc_tfport);

		tfport = new JTextField();
		GridBagConstraints gbc_tfport_1 = new GridBagConstraints();
		gbc_tfport_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfport_1.gridx = 3;
		gbc_tfport_1.gridy = 1;
		panelConnectionInfo.add(tfport, gbc_tfport_1);
		tfport.setColumns(10);

		JPanel panelCommands = new JPanel();
		contentPane.add(panelCommands, BorderLayout.EAST);
		panelCommands.setBorder(new TitledBorder(new EtchedBorder(), "Commands"));
		panelCommands.setLayout(new GridLayout(0, 3, 0, 0));

		btnConnect = new JButton("Connect");
		panelCommands.add(btnConnect);
		btnConnect.addActionListener(new GUICommandListener<Command>(cmdQueue,
				console, Command.CONNECT));

		btnDisconnect = new JButton("Disconnect");
		panelCommands.add(btnDisconnect);
		btnDisconnect.addActionListener(new GUICommandListener<Command>(
				cmdQueue, console, Command.DISCONNECT));

		btnPwd = new JButton("PWD");
		panelCommands.add(btnPwd);
		btnPwd.addActionListener(new GUICommandListener<Command>(cmdQueue,
				console, Command.PWD));

		label = new JLabel("");
		panelCommands.add(label);

		label_1 = new JLabel("");
		panelCommands.add(label_1);

		label_2 = new JLabel("");
		panelCommands.add(label_2);

		btnUpload = new JButton("Upload");
		panelCommands.add(btnUpload);
		btnUpload.addActionListener(new GUICommandListener<Command>(cmdQueue,
				console, Command.STOR));

		btnDownload = new JButton("Download");
		panelCommands.add(btnDownload);
		btnDownload.addActionListener(new GUICommandListener<Command>(cmdQueue,
				console, Command.RETR));

		btnList = new JButton("List");
		panelCommands.add(btnList);
		btnList.addActionListener(new GUICommandListener<Command>(cmdQueue,
				console, Command.LIST));
		
		labe2 = new JLabel("");
		panelCommands.add(labe2);

		labe2_1 = new JLabel("");
		panelCommands.add(labe2_1);

		labe2_2 = new JLabel("");
		panelCommands.add(labe2_2);

		
		
		
		cwd = new JButton("CWD");
		panelCommands.add(cwd);
		cwd.addActionListener(new GUICommandListener<Command>(cmdQueue, console, Command.CWD));
		
				
		label_3 = new JLabel("");
		panelCommands.add(label_3);

		label_4 = new JLabel("");
		panelCommands.add(label_4);

		label_5 = new JLabel("");
		panelCommands.add(label_5);

		label_6 = new JLabel("");
		panelCommands.add(label_6);

		label_7 = new JLabel("");
		panelCommands.add(label_7);

		label_8 = new JLabel("");
		panelCommands.add(label_8);

		label_9 = new JLabel("");
		panelCommands.add(label_9);
		
			
		
		btnClearConsole = new JButton("Clear Console");
		panelCommands.add(btnClearConsole);
		btnClearConsole.addActionListener(new GUICommandListener<Command>(cmdQueue, console, Command.CLEAR));

		label_10 = new JLabel("");
		panelCommands.add(label_10);

		label_11 = new JLabel("");
		panelCommands.add(label_11);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_2.setBorder(new TitledBorder(new EtchedBorder(), "Console"));

		JScrollPane consoleScroll = new JScrollPane(console);
		consoleScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panel_2.add(consoleScroll);

		scrollPane = new JScrollPane();
		scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Display"));
		scrollPane.setSize(150, 80);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		root = new DefaultMutableTreeNode("The FTP Server");
		tree = new JTree(root);
		tree.setShowsRootHandles(true);
		tree.setEditable(false);
		tree.setVisibleRowCount(10);
		scrollPane.setViewportView(tree);

		pack();
		setLocationRelativeTo(null);
		Thread cmdd = new Thread(new CommandDispatcher(cmdQueue, this));
		cmdd.start();
	}

	private class QuitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				cmdQueue.put(Command.QUIT);
				System.exit(DISPOSE_ON_CLOSE);
			} catch (InterruptedException e) {
				console.append(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
