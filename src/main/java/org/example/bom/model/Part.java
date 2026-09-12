package org.example.bom.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a single atomic component (leaf node) with a unit price.
 */
public record Part(
    String id,
    String name,
    int quantity,
    BigDecimal unitCost
) implements BomItem {
    public Part {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(unitCost, "unitCost must not be null");
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (unitCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit cost cannot be negative");
        }
    }
}
