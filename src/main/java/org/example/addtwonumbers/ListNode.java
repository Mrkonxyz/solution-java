package org.example.addtwonumbers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {}

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    /**
     * Helper to create a linked list from an array of ints.
     * e.g. ListNode.of(2, 4, 3) -> 2 -> 4 -> 3
     */
    public static ListNode of(int... values) {
        if (values == null || values.length == 0) {
            return null;
        }
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int v : values) {
            current.next = new ListNode(v);
            current = current.next;
        }
        return dummy.next;
    }

    /**
     * Helper to convert linked list to int array for easy assertions.
     */
    public int[] toArray() {
        List<Integer> list = new ArrayList<>();
        ListNode curr = this;
        while (curr != null) {
            list.add(curr.val);
            curr = curr.next;
        }
        return list.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListNode listNode = (ListNode) o;
        return val == listNode.val && Objects.equals(next, listNode.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val, next);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        ListNode curr = this;
        while (curr != null) {
            sb.append(curr.val);
            if (curr.next != null) {
                sb.append(" -> ");
            }
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
