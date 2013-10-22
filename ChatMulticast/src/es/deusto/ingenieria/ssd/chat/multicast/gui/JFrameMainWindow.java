package es.deusto.ingenieria.ssd.chat.multicast.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class JFrameMainWindow extends JFrame implements Observer, WindowListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFieldServerIP;
	private JTextField txtFieldServerPort;
	private JTextField txtFieldNick;
	private JButton btnConnect;
	private JList<String> listUsers;
	private JTextPane textAreaHistory;
	private JTextArea textAreaSendMsg;
	private JButton btnSendMsg;
	private JButton btnReloadListOfUsers;
	private SimpleDateFormat textFormatter = new SimpleDateFormat("HH:mm:ss");	

	/**
	 * Create the frame.
	 */
	public JFrameMainWindow() {
		//Add the frame as Observer of the Controller
		
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Chat main window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelUsers = new JPanel();
		panelUsers.setBorder(new TitledBorder(null, "Connected users", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelUsers, BorderLayout.EAST);
		panelUsers.setLayout(new BorderLayout(0, 0));
		panelUsers.setPreferredSize(new Dimension(200, 0));
		panelUsers.setMinimumSize(new Dimension(200, 0));
		
		listUsers = new JList<>();
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listUsers.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		listUsers.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectUser();
				
			}
		});
		
		panelUsers.add(listUsers);
		
		JPanel panelConnect = new JPanel();
		panelConnect.setBorder(new TitledBorder(null, "Connection details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelConnect, BorderLayout.NORTH);
		
		JLabel lblServerIp = new JLabel("Server IP:");		
		JLabel lblServerPort = new JLabel("Server Port:");
		
		txtFieldServerIP = new JTextField();
		txtFieldServerIP.setColumns(10);
		txtFieldServerIP.setText("127.0.0.1");
		txtFieldServerPort = new JTextField();
		txtFieldServerPort.setColumns(10);
		txtFieldServerPort.setText("6789");
		
		JLabel lblNick = new JLabel("Nick:");
		
		txtFieldNick = new JTextField();
		txtFieldNick.setColumns(10);
		txtFieldNick.setText("aaa");
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConnectClick();
			}
		});
		btnConnect.setToolTipText("Connect");
		GroupLayout gl_panelConnect = new GroupLayout(panelConnect);
		gl_panelConnect.setHorizontalGroup(
			gl_panelConnect.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelConnect.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelConnect.createParallelGroup(Alignment.LEADING)
						.addComponent(lblServerIp)
						.addComponent(lblServerPort))
					.addGap(21)
					.addGroup(gl_panelConnect.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelConnect.createSequentialGroup()
							.addComponent(txtFieldServerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(37)
							.addComponent(lblNick)
							.addGap(18)
							.addComponent(txtFieldNick, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(28)
							.addComponent(btnConnect))
						.addComponent(txtFieldServerPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(302, Short.MAX_VALUE))
		);
		gl_panelConnect.setVerticalGroup(
			gl_panelConnect.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelConnect.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelConnect.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerIp)
						.addComponent(txtFieldServerIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNick)
						.addComponent(btnConnect)
						.addComponent(txtFieldNick, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelConnect.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerPort)
						.addComponent(txtFieldServerPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		panelConnect.setLayout(gl_panelConnect);
		
		JPanel panelHistory = new JPanel();
		panelHistory.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "History", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelHistory, BorderLayout.CENTER);
		panelHistory.setLayout(new BorderLayout(0, 0));
		
		textAreaHistory = new JTextPane();
		textAreaHistory.setBackground(Color.BLACK);
		textAreaHistory.setToolTipText("Messages history");
		textAreaHistory.setEditable(false);
		
		JScrollPane scrollPaneHistory = new JScrollPane(textAreaHistory);
		scrollPaneHistory.setViewportBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelHistory.add(scrollPaneHistory);
		
		
		JPanel panelSendMsg = new JPanel();
		contentPane.add(panelSendMsg, BorderLayout.SOUTH);
		panelSendMsg.setBorder(new TitledBorder(null, "New message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSendMsg.setLayout(new BorderLayout(0, 0));
		
		btnSendMsg = new JButton("Send");
		btnSendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnSendClick();
			}
		});
		btnSendMsg.setToolTipText("Send new message");
		btnSendMsg.setEnabled(false);
		panelSendMsg.add(btnSendMsg, BorderLayout.EAST);
		
		textAreaSendMsg = new JTextArea();
		textAreaSendMsg.setTabSize(3);
		textAreaSendMsg.setRows(4);
		textAreaSendMsg.setToolTipText("New message");	
		
		JScrollPane scrollPaneNewMsg = new JScrollPane(textAreaSendMsg);
		scrollPaneNewMsg.setViewportBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelSendMsg.add(scrollPaneNewMsg, BorderLayout.CENTER);		
	}
	
	/**
	 * This method refresh the list of connected users
	 * @param allUsers list of connected users separated by &
	 */
	public void refreshUserList(String allUsers){
		
	}
	
	
	private void btnConnectClick() {
	}
	
	public void conversationRejected(){
	}
	
	public void existentNick(){
		JOptionPane.showMessageDialog(this, "The nick is already used.");
	}
	
	/**
	 * THis method is responsible of changing elements state in the GUI
	 * when the disconnection has been made with success.
	 */
	@SuppressWarnings("rawtypes")
	public void disconnectionSuccessful(){
		this.txtFieldServerIP.setEditable(true);
		this.txtFieldServerPort.setEditable(true);
		this.txtFieldNick.setEditable(true);
		this.listUsers.setEnabled(true);
		this.listUsers.clearSelection();
		((DefaultListModel)this.listUsers.getModel()).removeAllElements();
		this.btnConnect.setText("Connect");
		this.btnSendMsg.setEnabled(false);
		this.btnReloadListOfUsers.setEnabled(false);
		this.textAreaHistory.setText("");
		this.textAreaSendMsg.setText("");
		this.setTitle("Chat main window - 'Disconnected'");
		JOptionPane.showMessageDialog(this, "Disconnection successful.");
	}
	
	//The following methods corresponds to emergent window that notifies to the user
	//about the chat state.
	public void connectionFailed(){
		JOptionPane.showMessageDialog(this, "Connection failed.");
	}
	
	public void alreadyChatting(String nick){
		JOptionPane.showMessageDialog(this, nick+" is already chatting.");
	}
	
	public void disconnectedB(String nick){
		JOptionPane.showMessageDialog(this, nick+" is disconnected.");
	}
	
	public boolean acceptChatInvitation(String nick){
		int result = JOptionPane.showConfirmDialog(this, "Do you want to start a new chat session with '" + nick + "'", "Open chat Session", JOptionPane.YES_NO_OPTION);
	if (result==JOptionPane.OK_OPTION){
		return true;
	}
	else
	{
		return false;
	}
	
	}
	private void selectUser() {
	}
	
	private void btnSendClick() {
	}
	

	public void endConversationMessage(){
		String time = textFormatter.format(new Date());		
		String newMessage = " " + time + ": CONVERSATION FINISHED\n";
		appendMessageToHistory(newMessage, Color.GREEN); 
	}
	public void startConversationMessage(){
	}
	public void appendReceivedMessageToHistory(String message, String user, long timestamp) {		
		String time = textFormatter.format(new Date(timestamp));		
		String newMessage = " " + time + " - [" + user + "]: " + message.trim() + "\n";
		appendMessageToHistory(newMessage, Color.MAGENTA); 
	}
	public void appendMyMessageToHistory(String message, String user, long timestamp) {		
		String time = textFormatter.format(new Date(timestamp));		
		String newMessage = " " + time + " - [" + user + "]: " + message.trim() + "\n";
		appendMessageToHistory(newMessage, Color.BLUE); 
	}
	public void appendMessageToHistory(String message, Color color){
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setBold(attrs, true);
		StyleConstants.setForeground(attrs, color);		
		
		try {
			this.textAreaHistory.getStyledDocument().insertString(this.textAreaHistory.getStyledDocument().getLength(), message, attrs);
		} catch (BadLocationException e) {
			System.err.println("# Error updating message history: " + e.getMessage());
		} 
	}

	@Override
	public void update(Observable observable, Object object) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	//This method is used when the user close the chat window with (X).
	@Override
	public void windowClosing(WindowEvent e) {
			System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
		
	}
}
