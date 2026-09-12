package org.example.bom.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Represents a composite assembly containing sub-components and its own assembly cost.
 */
public record Assembly(
    String id,
    String name,
    int quantity,
    BigDecimal assemblyCost,
    List<BomItem> components
) implements BomItem {
    public Assembly {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(assemblyCost, "assemblyCost must not be null");
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (assemblyCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Assembly cost cannot be negative");
        }
        components = components == null ? List.of() : List.copyOf(components);
    }
}
