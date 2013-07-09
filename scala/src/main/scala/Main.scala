import javax.naming.{Context, InitialContext}
import org.rhq.core.domain.auth.Subject
import org.rhq.core.domain.criteria.ResourceCriteria
import org.rhq.enterprise.server.auth.{SubjectManagerRemote => SubjectManager}
import org.rhq.enterprise.server.resource.{ResourceManagerRemote => ResourceManager}
import scala.collection.JavaConversions._

/**
 * @author Jiri Kremser
 */
object Main {
    def lookup[A](context: InitialContext, manager: Class[A]) : A = {
      manager.cast(context.lookup("ejb:rhq/rhq-enterprise-server-ejb3/" + manager.getSimpleName.stripSuffix("Remote") + "Bean!" + manager.getName))
    }

    def main(args: Array[String]) {
        val jndiProperties = new java.util.Hashtable[String, String]
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming")
        val context = new InitialContext(jndiProperties)
        val subjectManager = lookup[SubjectManager](context, classOf[SubjectManager])
        val subject = subjectManager.login("rhqadmin", "rhqadmin")
        println("Logged in as: " + subject)
        val resourceManager = lookup[ResourceManager](context, classOf[ResourceManager])
        val criteria = new ResourceCriteria
        criteria.addFilterName("CPU")
        val resources = resourceManager.findResourcesByCriteria(subject, criteria)
        println("Odd CPUs:" + (resources.filter(x => x.getName.takeRight(1).toInt % 2 == 1)).mkString("\n"))
    }
}
