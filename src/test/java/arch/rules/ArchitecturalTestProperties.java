package arch.rules;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ArchitecturalTestProperties(Boolean networkAccess, List<String> allowedPackages, List<String> allowedLoops) {}