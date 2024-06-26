package arch.rules;

import java.util.List;

public record ArchitecturalTestProperties(Boolean networkAccess, List<String> allowedPackages, List<String> allowedLoops) {}