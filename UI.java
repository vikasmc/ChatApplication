package httpserver;


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class UI extends Frame implements ActionListener, KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Label meslabel;
	TextField mesg;
	Button send, login, exit, setuser,exits;
	TextArea ta, usrlist;
	Panel top, mid, bot;
	static String message, messg, usr, name, to;
	Client r;

	public UI(String title) {
		super(title);
		initComponents();
		setLayout(new BorderLayout());
		top = new Panel();
		top.setLayout(new GridLayout(1, 2));
		bot = new Panel();
		bot.setLayout(new GridLayout(1, 3, 5, 5));
		setSize(500, 500);
		setBackground(Color.gray);
		setForeground(Color.black);
		meslabel = new Label("Message", Label.RIGHT);
		mesg = new TextField(20);
		ta = new TextArea("", 20, 20, TextArea.SCROLLBARS_BOTH);
		usrlist = new TextArea("user list", 20, 20, TextArea.SCROLLBARS_BOTH);
		send = new Button("Send");
		login = new Button("Login");
		exit = new Button("QuitChat");
		setuser = new Button("SetUser");
		exits=new Button("Quit");
		send.setEnabled(false);
		setuser.setEnabled(false);
		exit.setEnabled(false);
		exits.setEnabled(false);
		top.add(meslabel);
		top.add(mesg);
		bot.add(login);
		bot.add(send);
		bot.add(setuser);
		bot.add(exit);
		bot.add(exits);
		add(top, BorderLayout.NORTH);
		add(bot, BorderLayout.SOUTH);
		add(ta, BorderLayout.CENTER);
		add(usrlist, BorderLayout.EAST);
		send.addActionListener(this);
		mesg.addKeyListener(this);
		login.addActionListener(this);
		exit.addActionListener(this);
		setuser.addActionListener(this);
		exits.addActionListener(this);
		r = new Client();
	}

	public void actionPerformed(ActionEvent ae) {
		Button btn = (Button) ae.getSource();
		if (btn == login) {
			loginDialog ld = new loginDialog(this);
			ld.show();
			usr = ld.user;
			String output = r.SendName(usr,"name");
			String namepass[] = output.split(":");
			if (namepass[0].equals("the user name is addedd")) {
				ta.append("\nThe user " + usr + " has been added ");
				login.setEnabled(false);
				setuser.setEnabled(true);
				exits.setEnabled(true);
				String names[] = namepass[1].split(" ");
				for(int i=0;i<names.length;i++){
					usrlist.append("\n"+names[i]);
				}
				new MessageReader().start();
			} else {
				ta.append("\n" + output);
			}
		}
		if (btn == send) {
			String txt = mesg.getText();
			mesg.setText("");
			String toa=usr+":"+to+":"+txt;
			String output=r.SendMessage(toa);
			if(output.equals("Message sent from "+usr+" to "+to )){
				ta.append("\n"+txt);
			}
			else if(output.equals("the user "+to+" is not online")){
				ta.append("\n"+txt);
				send.setEnabled(false);
				setuser.setEnabled(true);
			}
			else{
				ta.append("\n"+output);
				send.setEnabled(false);
				setuser.setEnabled(true);
			}
		}
		if (btn == setuser) {
			GetDialog Id=new GetDialog(this);
			Id.show();
			to=Id.user;
			String output =r.SendName(to,"chatTo");
			if(output.equals("success")){
				ta.append("\nEnter the message you want to send to "+to);
				send.setEnabled(true);
				exit.setEnabled(true);
				setuser.setEnabled(false);
			}
			else{
				ta.append("\n"+output);
			}
		}
		if (btn == exit) {
				r.Delete(usr,"chatOff");
				send.setEnabled(false);
				setuser.setEnabled(true);
				exit.setEnabled(false);
				ta.append("\nYou left the chat with the user "+to);
		}
		if (btn == exits) {
			r.Delete(usr,"del");
			dispose();
		}
	}

	public void keyPressed(KeyEvent ke) {
	}

	public void keyReleased(KeyEvent ke) {
	}

	public void keyTyped(KeyEvent ke) {

	}

	public void initComponents() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				setVisible(false);
			}
		});

	}

	class MessageReader extends Thread {
		String msg;

		public void run() {
			do{
			String output = r.GetMessage(usr);
			if(output.equals("failure")){
				
			}
			else if(output.equals(null+":"+null)){
				
			}
			else{
				setuser.setEnabled(false);
				send.setEnabled(true);
				exit.setEnabled(true);
				String namepass[] = output.split(":");
				to = namepass[0];
				ta.append("\n"+output);
			}
			}while(true);
		}
	}

	public static void main(String s[]) {
		UI mcc = new UI("Client");
		mcc.setVisible(true);
		mcc.show();
	}


}

class loginDialog extends Dialog implements ActionListener {
	Label loginname;
	TextField logintext;
	Button okay;
	Panel top, central;
	String user;

	loginDialog(Frame parent) {
		super(parent, "User Login", true);
		setSize(400, 100);
		setResizable(false);
		setFont(new Font("ComicSans", Font.BOLD | Font.ITALIC, 15));
		loginname = new Label("Enter Name:", Label.RIGHT);
		logintext = new TextField(15);
		okay = new Button("LOGIN");
		okay.setBounds(10, 10, 20, 20);
		top = new Panel();
		central = new Panel();
		central.setLayout(new FlowLayout());
		top.setLayout(new GridLayout(1, 2, 0, 0));
		top.add(loginname);
		top.add(logintext);
		central.add(okay);
		add(central, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		okay.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		user = logintext.getText();
		dispose();
	}
}

class GetDialog extends Dialog implements ActionListener {
	Label loginname;
	TextField logintext;
	Button okay;
	Panel top, central;
	String user;

	GetDialog(Frame parent) {
		super(parent, "Enter the User you want to chat", true);
		setSize(400, 100);
		setResizable(false);
		setFont(new Font("ComicSans", Font.BOLD | Font.ITALIC, 15));
		loginname = new Label("Enter Name:", Label.RIGHT);
		logintext = new TextField(15);
		okay = new Button("Enter");
		okay.setBounds(10, 10, 20, 20);
		top = new Panel();
		central = new Panel();
		central.setLayout(new FlowLayout());
		top.setLayout(new GridLayout(1, 2, 0, 0));
		top.add(loginname);
		top.add(logintext);
		central.add(okay);
		add(central, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		okay.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		user = logintext.getText();
		dispose();
	}
}
