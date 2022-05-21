package ifce.ppd.tuplespace.server;

import ifce.ppd.tuplespace.model.User;
import net.jini.space.JavaSpace;

public class ReadUserTest {
    public static void main(String[] args) {
        System.setProperty("java.security.policy","/home/joaomarcus/Projetos/tuple-space/src/main/java/ifce/ppd/tuplespace/all.policy");
        System.setSecurityManager(new SecurityManager());

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

            User template = new User();
            User msg = (User) space.take(template, null, 60 * 1000);
            if (msg == null) {
                System.out.println("Tempo de espera esgotado. Encerrando...");
                System.exit(0);
            }
            System.out.println("Nome do usuário criado: "+ msg.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
