import javax.swing.*;
import java.awt.*;

public class Cliente {

    JFrame ventana_chat = null;
    JButton btn_enviar = null;
    JTextField txt_msg = null;
    JTextArea area_chat = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;

    public Cliente(){
        hacerInterfaz();
    }

    public void hacerInterfaz(){
        //Aquí se construye la interfaz de usuario
        ventana_chat = new JFrame("Cliente1");
        btn_enviar = new JButton("Consultar");
        txt_msg = new JTextField(4);
        area_chat = new JTextArea(1,3);
        contenedor_areachat = new JPanel();
        contenedor_areachat.setLayout(new GridLayout(1,1));
        contenedor_areachat.add(area_chat);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1,2));
        contenedor_btntxt.add(txt_msg);
        contenedor_btntxt.add(btn_enviar);
        ventana_chat.setLayout(new BorderLayout());
        ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
        ventana_chat.add(contenedor_btntxt, BorderLayout.SOUTH);
        ventana_chat.setSize(300,300);
        ventana_chat.setVisible(true);
        ventana_chat.setResizable(false);
        ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        new Cliente();
    }
}
