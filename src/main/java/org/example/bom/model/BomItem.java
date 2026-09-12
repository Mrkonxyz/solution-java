package org.example.bom.model;

/**
 * Sealed interface representing any item in a Bill of Materials (BOM) hierarchy.
 * Sealed hierarchy ensures compile-time exhaustiveness checks in Java 21 pattern matching.
 */
public sealed interface BomItem permits Part, Assembly {
    String id();
    String name();
    int quantity();
}
