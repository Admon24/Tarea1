import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    //Creación de objetos
    JFrame ventana_chat = null;
    JButton btn_enviar = null;
    JTextField txt_msg = null;
    JTextArea area_chat = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;
    JScrollPane scroll = null;
    ServerSocket servidor = null;
    Socket sc = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;

    public Servidor(){
        hacerInterfaz();
    }

    public void hacerInterfaz(){
        //Aquí se construye la interfaz de usuario
        ventana_chat = new JFrame("Servidor");
        btn_enviar = new JButton("Consultar");
        txt_msg = new JTextField(4);
        area_chat = new JTextArea(10,12);
        scroll = new JScrollPane(area_chat);
        contenedor_areachat = new JPanel();
        contenedor_areachat.setLayout(new GridLayout(1,1));
        contenedor_areachat.add(scroll);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1,2));
        contenedor_btntxt.add(txt_msg);
        contenedor_btntxt.add(btn_enviar);
        ventana_chat.setLayout(new BorderLayout());
        ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
        ventana_chat.add(contenedor_btntxt, BorderLayout.SOUTH);
        ventana_chat.setSize(250,225);
        ventana_chat.setVisible(true);
        ventana_chat.setResizable(false);
        ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread main = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    servidor = new ServerSocket(8080); //Este constructor recibe el puerto por el que escucha el serverSocket
                    System.out.println("Servidor escuchando");
                        while (true){ //Ciclo infinito
                            sc = servidor.accept(); //El servidor acepta las conexiones que le llegan al puerto
                            leer();
                            escribir();
                        }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        main.start();
    }

    public void leer(){
        int i=0;
        int[] dato= new int[3];
        Thread leer_hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lector = new BufferedReader(new InputStreamReader(sc.getInputStream())); //Obtenemos la entrada del socket sc
                        while (true){
                            String msg_recibido = lector.readLine(); //Leo todo lo que envíe el socket sc
                            area_chat.append("Cliente envía: " + msg_recibido + "\n"); //Pintamos el mensaje recibido en la ventana
                            System.out.println(lector.readLine());
                        }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        leer_hilo.start(); //Se pone a funcionar el hilo
    }

    public void escribir(){
        Thread escribir_hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escritor = new PrintWriter(sc.getOutputStream(), true); //Obtengo la salida y le envío algo al socket sc
                    btn_enviar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) { //Se ejecuta cada vez que se le da click al botón
                            String enviar_msg = txt_msg.getText(); //Obtengo el mensaje en la caja de texto
                            escritor.println(enviar_msg); //Envío el mensaje mediante el método escritor
                            txt_msg.setText(""); //SE limpia la caja de texto
                        }
                    });
                    //escritor.println(); //Envío mensajes, gracias al true de la línea de arriba
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        escribir_hilo.start();
    }

    public static void main(String[] args){
        new Servidor();
    }
}
