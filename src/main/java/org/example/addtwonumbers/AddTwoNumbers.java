package org.example.addtwonumbers;

public class AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode root = new ListNode();
        linkMultipleNode(l1, l2, root);
        return root;
    }

    private void linkMultipleNode(ListNode l1, ListNode l2, ListNode current) {

        if (l1 != null && l2 == null) {
            linkSingleNode(l1, current);
            return;
        }
        if (l1 == null && l2 != null) {
            linkSingleNode(l2, current);
            return;
        }

        if (l1 == null) {
            return;
        }

        calValAndCurry(l1.val + l2.val + current.val , current);
        if (l1.next == null && l2.next == null) {
            return ;
        }

        if (current.next == null) {
            current.next = new ListNode();
        }

        linkMultipleNode(l1.next, l2.next , current.next);
    }

    private void linkSingleNode(ListNode n1, ListNode current) {
        if (n1 == null ) {
            return;
        }
        calValAndCurry(current.val +  n1.val, current);
        if (n1.next == null) {
            return;
        }

        if (n1.next != null && current.next == null) {
            current.next = new ListNode();
        }

        linkSingleNode(n1.next, current.next);
    }

    private void calValAndCurry(int sum, ListNode current) {
        if (current == null) {
            return;
        }
        int curry = sum / 10;
        int val = sum % 10;
        if (curry > 0) {
            current.next = new ListNode(curry);
        }
        current.val = val;
    }

}
