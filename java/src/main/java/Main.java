import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.rhq.core.domain.auth.Subject;
import org.rhq.enterprise.server.auth.SubjectManagerRemote;

/**
 * @author Lukas Krejci
 */
public class Main {

    public static final void main(String[] args) throws Exception {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        InitialContext ic = new InitialContext(jndiProperties);

        SubjectManagerRemote sm = (SubjectManagerRemote) ic.lookup("ejb:rhq/rhq-enterprise-server-ejb3/SubjectManagerBean!" + SubjectManagerRemote.class.getName());

        Subject s = sm.login("rhqadmin", "rhqadmin");

        System.out.println(s);
    }
}
