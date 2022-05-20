package ifce.ppd.tuplespace.example;

import net.jini.space.JavaSpace;

import java.rmi.RMISecurityManager;

public class ReadMessage {

    public static void main(String[] args) {
        System.setProperty("java.security.policy","/home/joaomarcus/Projetos/tuple-space/src/main/java/ifce/ppd/tuplespace/example/all.policy");
        System.setSecurityManager(new RMISecurityManager());
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            Lookup finder = new Lookup(JavaSpace.class);
            JavaSpace space = (JavaSpace) finder.getService();
            if (space == null) {
                    System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                    System.exit(-1);
            } 
            System.out.println("O servico JavaSpace foi encontrado.");
	    System.out.println(space);

            while (true) {
                Message template = new Message();
                Message msg = (Message) space.take(template, null, 60 * 1000);
                if (msg == null) {
                    System.out.println("Tempo de espera esgotado. Encerrando...");
                    System.exit(0);
                }
                System.out.println("Mensagem recebida: "+ msg.content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
