import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;

public class ChatBoard extends Applet implements Runnable {
        int connection_slot=-1;
        boolean am_connected=false;
        Chat Chatobj;
        ActionTextField sign_in,say,hear;
        String chat_name;
        ActionButton connect,disconnect,chat;
        ActionLabel chat_label;
        ActionPanel CardHolder;
        Panel sign_in_panel,chat_panel,sign_in_text;
        Panel chat_text,sign_in_button,chat_button;
    public void init() {
        sign_in=new ActionTextField("signin");
        say=new ActionTextField("Your Remarks Go Here");
        hear=new ActionTextField("Others Will Use This Field");
        hear.setEditable(false);
        connect =new ActionButton("connect");
        disconnect =new ActionButton("disconnect");
        chat =new ActionButton("chat");
        sign_in_panel=new Panel();
        chat_panel=new Panel();
        sign_in_text=new Panel();
        chat_text=new Panel();
        sign_in_button=new Panel();
        chat_button=new Panel();
 // "The Applet panel"
        CardHolder=new ActionPanel();
        chat_label=new ActionLabel("Sign In",Label.CENTER);
        setLayout(new BorderLayout());
        add("Center",CardHolder);
        add("North",chat_label);
        CardHolder.setLayout(new CardLayout());
        CardHolder.add("signin",
        sign_in_panel);
        CardHolder.add("chat",chat_panel);

        sign_in_panel.setLayout(new BorderLayout());
        chat_panel.setLayout(new BorderLayout());
        sign_in_panel.add("South",sign_in_button);
        sign_in_panel.add("Center",sign_in_text);
        chat_panel.add("South",chat_button);
        chat_panel.add("Center",chat_text);
        chat_text.setLayout(new BorderLayout());
        sign_in_text.setLayout(new BorderLayout());
        chat_text.add("North",say);
        chat_text.add("South",hear);
        sign_in_text.add("Center",sign_in);
        sign_in_button.add(connect);
        chat_button.add(disconnect);
        chat_button.add(chat);
        connect.addActionListener(sign_in);
        connect.addActionListener(chat_label);
        connect.addActionListener(CardHolder);
        disconnect.addActionListener(chat_label);
        disconnect.addActionListener(CardHolder);
        chat.addActionListener(say);
        chat_label.setText("Sign In");
        System.out.println("In init");
        
        try {
			Chatobj = (Chat)Naming.lookup("//www.umsl.edu/~siegel/wwwRMI/ChatServer");
		} catch (Exception e) {
			System.out.println("ChatBoard: exception: " +
				e.getMessage());
         }

    }

    public void start(){
        System.out.println("in start");
        new Thread(this).start();
    }
    public void run(){
        String get_message=" ";
        while(true){
            if(am_connected){
                try{
                    get_message=Chatobj.getbroadcast();
                }catch(Exception e){}
                hear.setText(get_message);
            }
            try{
                Thread.currentThread().sleep(1000);
            }catch(InterruptedException e){}
        }
    }



class ActionButton extends Button implements ActionListener{

    public ActionButton(String s){
        super(s);
           }

   public void actionPerformed(ActionEvent evt){
           String s=evt.getActionCommand();


   }

  }//end of innerclass


  class ActionTextField extends TextField implements ActionListener{
      String field_holder;
    public ActionTextField(String s){
        super(s);
        }

    public void actionPerformed(ActionEvent evt){
       String s=evt.getActionCommand();
       field_holder=getText().trim();
       if(s.equals("connect")){
            if(field_holder.length()<4){
                setText("Your Chat Name Must Be At Least 4 Letters");
            }
            else
            {
            try{
                System.out.println(connection_slot);
                connection_slot=Chatobj.signIn(field_holder,connection_slot);
                System.out.println(connection_slot);
                if(connection_slot>=0)
                     am_connected=true;
                else
                     setText("Connection Refused");
                }catch(Exception e){}

            }
       }
       if(s.equals("chat")){
        try{
        Chatobj.broadcast(field_holder,connection_slot);
        }
        catch(Exception e){}
        }
    }



  }

class ActionPanel extends Panel implements ActionListener{
    public ActionPanel(){
        super();
        }
    public void actionPerformed(ActionEvent evt){
        if(connection_slot>=0){
        String s=evt.getActionCommand();
        if((s.equals("disconnect"))|(s.equals("connect")&((sign_in.getText()).trim().length()>3)))
        ((CardLayout)getLayout()).next(this);
        if(s.equals("disconnect")){
		try{Chatobj.signIn(" ",connection_slot);}catch(Exception e){}
                connection_slot=-1;
       }
        }
     }

  }
class ActionLabel extends Label implements ActionListener{
    public ActionLabel(String s,int i){
        super(s,i);
        }

    public void actionPerformed(ActionEvent evt){

    String s=evt.getActionCommand();
    String hold=sign_in.getText();
     if(s.equals("connect")&(connection_slot>=0))setText("Start Chatting");
     if(s.equals("disconnect"))setText("Sign In");
     }

  }

}//end of class