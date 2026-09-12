package org.example.bom.service;

import org.example.bom.model.Assembly;
import org.example.bom.model.BomItem;
import org.example.bom.model.Part;

import java.math.BigDecimal;
import java.util.List;

public class BomAnalysisService {

    /**
     * Recursively calculates the total cost of a BOM item, considering quantities
     * and nested assembly costs down the entire hierarchy.
     *
     * @param item the root BOM item (Part or Assembly)
     * @return total calculated cost
     * @throws IllegalArgumentException if item is null
     */
    public BigDecimal calculateTotalCost(BomItem item) {
      return  switch (item) {
            case Part part -> part.unitCost().multiply(BigDecimal.valueOf(item.quantity()));
            case Assembly assembly -> {
                BigDecimal sum = sumComponents(assembly.components(), 0, BigDecimal.valueOf(0));
                yield  sum.add(assembly.assemblyCost()).multiply(BigDecimal.valueOf(assembly.quantity()));
            }
            case null -> throw new IllegalArgumentException("Item must not be null");
        };
    }

    public BigDecimal sumComponents(List<BomItem> item, int index, BigDecimal total) {
        if (item.size() == index) {
            return total;
        }
        BigDecimal cost = calculateTotalCost(item.get(index));
        return  sumComponents(item, index + 1, cost.add(total));
    }

    /**
     * Recursively calculates the maximum hierarchy depth of the BOM tree.
     * An atomic Part or an empty Assembly has a depth of 1.
     *
     * @param item the root BOM item
     * @return maximum depth level of the tree
     * @throws IllegalArgumentException if item is null
     */
    public int calculateMaxDepth(BomItem item) {
        // TODO: Implement recursive depth calculation
        return 0;
    }
}
