package org.example.bom.service;

import org.example.bom.model.Assembly;
import org.example.bom.model.BomItem;
import org.example.bom.model.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BomAnalysisServiceTest {

    private BomAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new BomAnalysisService();
    }

    @Nested
    @DisplayName("Tests for calculateTotalCost")
    class CalculateTotalCostTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when input item is null")
        void shouldThrowExceptionWhenItemIsNull() {
            assertThrows(IllegalArgumentException.class, () -> service.calculateTotalCost(null));
        }

        @Test
        @DisplayName("Base Case: Single Part with standard quantity and unit cost")
        void shouldCalculateCostForSinglePart() {
            // Arrange: 4 units @ $12.50 each = $50.00
            Part screw = new Part("P-01", "Titanium Screw", 4, new BigDecimal("12.50"));

            // Act
            BigDecimal actual = service.calculateTotalCost(screw);

            // Assert
            assertEquals(0, new BigDecimal("50.00").compareTo(actual),
                    "Expected 50.00 but got: " + actual);
        }

        @Test
        @DisplayName("Base Case: Single Part with zero quantity should result in zero cost")
        void shouldReturnZeroCostForPartWithZeroQuantity() {
            Part part = new Part("P-02", "Spare Washer", 0, new BigDecimal("100.00"));

            BigDecimal actual = service.calculateTotalCost(part);

            assertEquals(0, BigDecimal.ZERO.compareTo(actual));
        }

        @Test
        @DisplayName("Base Case: Single Part with zero cost should result in zero cost")
        void shouldReturnZeroCostForFreePart() {
            Part freeSample = new Part("P-03", "Sticker", 10, BigDecimal.ZERO);

            BigDecimal actual = service.calculateTotalCost(freeSample);

            assertEquals(0, BigDecimal.ZERO.compareTo(actual));
        }

        @Test
        @DisplayName("Edge Case: Assembly with empty components should cost only its assembly fee * quantity")
        void shouldCalculateCostForEmptyAssembly() {
            // Arrange: 2 empty boxes with $15.00 setup fee each = $30.00
            Assembly emptyBox = new Assembly(
                    "A-00",
                    "Empty Casing",
                    2,
                    new BigDecimal("15.00"),
                    List.of()
            );

            // Act
            BigDecimal actual = service.calculateTotalCost(emptyBox);

            // Assert
            assertEquals(0, new BigDecimal("30.00").compareTo(actual));
        }

        @Test
        @DisplayName("Single-level Assembly with multiple Part components")
        void shouldCalculateCostForSingleLevelAssembly() {
            // Arrange:
            // Frame: 1 unit @ $20.00 = $20.00
            // Wheel: 2 units @ $15.00 = $30.00
            // Subtotal of parts = $50.00
            // Assembly fee = $10.00
            // Assembly quantity = 3
            // Total = ($10.00 + $50.00) * 3 = $180.00
            Part frame = new Part("P-10", "Frame", 1, new BigDecimal("20.00"));
            Part wheel = new Part("P-11", "Wheel", 2, new BigDecimal("15.00"));

            Assembly skateboard = new Assembly(
                    "A-01",
                    "Skateboard Assembly",
                    3,
                    new BigDecimal("10.00"),
                    List.of(frame, wheel)
            );

            // Act
            BigDecimal actual = service.calculateTotalCost(skateboard);

            // Assert
            assertEquals(new BigDecimal("180.00"), actual);
        }

        @Test
        @DisplayName("Deeply nested hierarchy with cascading quantity multipliers")
        void shouldCalculateCostForDeeplyNestedHierarchy() {
            // Hierarchy:
            // Drone (qty: 2, assemblyCost: 50.00)
            //  └── Arm Assembly (qty: 4, assemblyCost: 10.00)
            //       └── Motor Unit (qty: 1, assemblyCost: 5.00)
            //            ├── Rotor Blade (qty: 2, unitCost: 3.50)  -> 7.00
            //            └── Copper Wire (qty: 1, unitCost: 1.00)  -> 1.00
            //
            // Calculation breakdown:
            // 1. Motor Unit single cost = (5.00 + 7.00 + 1.00) * 1 = 13.00
            // 2. Arm Assembly single cost = (10.00 + 13.00) * 4 = 92.00
            // 3. Drone total cost = (50.00 + 92.00) * 2 = 284.00

            Part rotor = new Part("P-ROTOR", "Rotor Blade", 2, new BigDecimal("3.50"));
            Part wire = new Part("P-WIRE", "Copper Wire", 1, new BigDecimal("1.00"));

            Assembly motorUnit = new Assembly(
                    "A-MOTOR",
                    "Motor Unit",
                    1,
                    new BigDecimal("5.00"),
                    List.of(rotor, wire)
            );

            Assembly armAssembly = new Assembly(
                    "A-ARM",
                    "Arm Assembly",
                    4,
                    new BigDecimal("10.00"),
                    List.of(motorUnit)
            );

            Assembly drone = new Assembly(
                    "A-DRONE",
                    "Drone Final Assembly",
                    2,
                    new BigDecimal("50.00"),
                    List.of(armAssembly)
            );

            // Act
            BigDecimal actual = service.calculateTotalCost(drone);

            // Assert
            assertEquals(0, new BigDecimal("284.00").compareTo(actual));
        }

        @Test
        @DisplayName("Asymmetric tree with mixed leaves and sub-assemblies")
        void shouldCalculateCostForAsymmetricTree() {
            // Main Unit (qty: 1, assemblyCost: 100.00)
            // ├── Direct Part: Manual (qty: 1, unitCost: 5.00) -> 5.00
            // └── Power Sub-system (qty: 2, assemblyCost: 20.00)
            //      ├── Battery (qty: 2, unitCost: 15.00) -> 30.00
            //      └── Fuse (qty: 1, unitCost: 2.00) -> 2.00
            //      Sub-system total = (20.00 + 30.00 + 2.00) * 2 = 104.00
            // Total = (100.00 + 5.00 + 104.00) * 1 = 209.00

            Part manual = new Part("P-MANUAL", "User Manual", 1, new BigDecimal("5.00"));
            Part battery = new Part("P-BAT", "Li-ion Cell", 2, new BigDecimal("15.00"));
            Part fuse = new Part("P-FUSE", "Fuse 10A", 1, new BigDecimal("2.00"));

            Assembly powerSystem = new Assembly(
                    "A-POWER",
                    "Power Sub-system",
                    2,
                    new BigDecimal("20.00"),
                    List.of(battery, fuse)
            );

            Assembly mainUnit = new Assembly(
                    "A-MAIN",
                    "Main Unit",
                    1,
                    new BigDecimal("100.00"),
                    List.of(manual, powerSystem)
            );

            BigDecimal actual = service.calculateTotalCost(mainUnit);

            assertEquals(0, new BigDecimal("209.00").compareTo(actual));
        }
    }

    @Nested
    @DisplayName("Tests for calculateMaxDepth")
    class CalculateMaxDepthTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when input item is null")
        void shouldThrowExceptionWhenItemIsNull() {
            assertThrows(IllegalArgumentException.class, () -> service.calculateMaxDepth(null));
        }

        @Test
        @DisplayName("Single Part should have a depth of 1")
        void shouldReturnOneForSinglePart() {
            Part part = new Part("P-01", "Nut", 1, new BigDecimal("0.50"));
            assertEquals(1, service.calculateMaxDepth(part));
        }

        @Test
        @DisplayName("Empty Assembly should have a depth of 1")
        void shouldReturnOneForEmptyAssembly() {
            Assembly empty = new Assembly("A-00", "Empty", 1, BigDecimal.TEN, List.of());
            assertEquals(1, service.calculateMaxDepth(empty));
        }

        @Test
        @DisplayName("Single-level Assembly with parts should have a depth of 2")
        void shouldReturnTwoForSingleLevelAssembly() {
            Part part = new Part("P-01", "Nut", 1, new BigDecimal("0.50"));
            Assembly assembly = new Assembly("A-01", "Nut Holder", 1, BigDecimal.ONE, List.of(part));
            assertEquals(2, service.calculateMaxDepth(assembly));
        }

        @Test
        @DisplayName("Asymmetric tree should report the depth of the deepest branch")
        void shouldReturnMaxDepthForAsymmetricTree() {
            // Root (Level 1)
            //  ├── Branch A: Leaf (Level 2)
            //  └── Branch B (Level 2)
            //        └── Sub-branch B1 (Level 3)
            //              └── Leaf (Level 4)
            Part shortBranchLeaf = new Part("P-S", "Short Leaf", 1, BigDecimal.ONE);

            Part deepLeaf = new Part("P-D", "Deep Leaf", 1, BigDecimal.ONE);
            Assembly subBranchB1 = new Assembly("A-B1", "Sub B1", 1, BigDecimal.ONE, List.of(deepLeaf));
            Assembly branchB = new Assembly("A-B", "Branch B", 1, BigDecimal.ONE, List.of(subBranchB1));

            Assembly root = new Assembly("A-ROOT", "Root", 1, BigDecimal.ONE, List.of(shortBranchLeaf, branchB));

            assertEquals(4, service.calculateMaxDepth(root));
        }

        @Test
        @DisplayName("Deep linear hierarchy of 5 levels should have a depth of 5")
        void shouldCalculateDepthForDeepLinearChain() {
            BomItem level5 = new Part("P-5", "Level 5 Leaf", 1, BigDecimal.ONE);
            BomItem level4 = new Assembly("A-4", "Level 4", 1, BigDecimal.ZERO, List.of(level5));
            BomItem level3 = new Assembly("A-3", "Level 3", 1, BigDecimal.ZERO, List.of(level4));
            BomItem level2 = new Assembly("A-2", "Level 2", 1, BigDecimal.ZERO, List.of(level3));
            BomItem level1 = new Assembly("A-1", "Level 1 Root", 1, BigDecimal.ZERO, List.of(level2));

            assertEquals(5, service.calculateMaxDepth(level1));
        }
    }
}
