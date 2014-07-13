package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.operators.Modulo;
import uk.ac.ed.ph.snuggletex.SnugglePackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mauss
 *         Created: 13.07.14 17:05
 */
public class SnugglePackageRegistry {

    private static Map<Class, SnugglePackage> providerMap = new HashMap<Class, SnugglePackage>();

    static {
        configure();
    }

    public static void register(Class klass, SnugglePackageProvider provider) {
        providerMap.put(klass, provider.provide());
    }

    /**
     * static configuration for SnugglePackages
     */
    private static void configure() {
        register(Modulo.class, new Modulo());
    }

    public static List<SnugglePackage> getPackages() {
        List<SnugglePackage> list = new ArrayList<SnugglePackage>();
        list.addAll(providerMap.values());
        return list;
    }
}
