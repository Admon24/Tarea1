import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Cliente {
    JFrame ventana_chat = null;
    JButton btn_enviar = null;
    JTextField txt_msg = null;
    JTextArea area_chat = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;
    JScrollPane scroll = null;
    Socket sc1 = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;
    double op;

    public Cliente(){
        hacerInterfaz();
    }

    public void hacerInterfaz(){
        //Aquí se construye la interfaz de usuario
        ventana_chat = new JFrame("Cliente1");
        btn_enviar = new JButton("Enviar");
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
                    sc1 = new Socket("localhost", 8080); //El socket sc va a estar conectado a esta misma máquina en el puerto
                    System.out.println("Clente 1 conectado");
                    leer1();
                    escribir1();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        main.start();
    }

    public void leer1(){
        Thread leer_hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lector = new BufferedReader(new InputStreamReader(sc1.getInputStream())); //Obtenemos la entrada del socket sc
                    while (true){
                        String msg_recibido = lector.readLine(); //Leo todo lo que envíe el socket sc
                        area_chat.append("Servidor envía: " + msg_recibido + "\n"); //Pintamos el mensaje recibido en la ventana
                        System.out.println(msg_recibido);

                        int i=0;
                        int[] dato= new int[3];

                        StringTokenizer Stoken = new StringTokenizer(msg_recibido,"-");//Convierte el string en un arreglo
                        while (Stoken.hasMoreTokens()){ //ciclo para separar palabras

                            dato[i]= Integer.parseInt(Stoken.nextToken());
                            System.out.println(dato[i]);
                            i++;
                            if (i==3){
                                op = ((dato[0]*dato[1])/100)+(dato[2]*0.15);
                                System.out.println(op);
                            }
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        leer_hilo.start(); //Se pone a funcionar el hilo
    }

    public void escribir1(){
        Thread escribir_hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escritor = new PrintWriter(sc1.getOutputStream(), true); //Obtengo la salida y le envío algo al socket sc
                    btn_enviar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) { //Se ejecuta cada vez que se le da click al botón
                            //String enviar_msg = txt_msg.getText(); //Obtengo el mensaje en la caja de texto
                            escritor.println(op); //Envío el mensaje mediante el método escritor
                            txt_msg.setText(""); //SE limpia la caja de texto
                        }
                    });
                    //escritor.println(); //Envío emnsajes, gracias al true de la línea de arriba
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        escribir_hilo.start();
    }

    public static void main(String[] args){
        new Cliente();
    }
}
