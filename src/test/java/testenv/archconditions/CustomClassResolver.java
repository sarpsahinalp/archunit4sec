package testenv.archconditions;

import com.tngtech.archunit.ArchConfiguration;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.resolvers.ClassResolver;
import com.tngtech.archunit.core.importer.resolvers.ClassResolverFromClasspath;

import java.net.URL;
import java.util.Optional;

public class CustomClassResolver implements ClassResolver {
    private final ClassFileImporter classFileImporter = new ClassFileImporter();
    private final ClassResolverFromClasspath defaultResolver = new ClassResolverFromClasspath();

    @Override
    public void setClassUriImporter(ClassUriImporter classUriImporter) {
        defaultResolver.setClassUriImporter(classUriImporter);
    }

    @Override
    public Optional<JavaClass> tryResolve(String typeName) {
        return defaultResolver.tryResolve(typeName);
//        if (typeName.startsWith("de.tum.cit.ase")) {
//            return defaultResolver.tryResolve(typeName);
//        }

//        if (!typeName.startsWith("java.lang") && !typeName.startsWith("java.io") && !typeName.startsWith("java.util") && !typeName.startsWith("java.nio")) {
//            return Optional.empty();
//        }
//
//        try {
//            ArchConfiguration.get().setClassResolver(ClassResolverFromClasspath.class);
//            URL url = getClass().getResource("/" + typeName.replace(".", "/") + ".class");
//            return url != null ? Optional.of(classFileImporter.importUrl(url).get(typeName)) : Optional.empty();
//        } finally {
//            ArchConfiguration.get().setClassResolver(CustomClassResolver.class);
//        }
    }
}
