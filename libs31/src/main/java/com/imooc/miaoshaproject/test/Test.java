package com.imooc.miaoshaproject.test;

public class Test {


    public static void main(String... args){

        // 已知
        Node linkList1HeadNode = new Node();
        Node linkList2HeadNode = new Node();

        // 两个链表当前节点
        Node linkList1Current = linkList1HeadNode;
        Node linkList2Current = linkList2HeadNode;

        // 合并后的链表的头节点
        Node head = linkList1HeadNode.data < linkList2HeadNode.data ? linkList1HeadNode : linkList2HeadNode;

        // 合并
        // linklist1插入到linklist2
        while (linkList1Current != null && linkList2Current != null){
            if(linkList1Current.data < linkList2Current.data){

                // 得到第一个链表的下一个节点
                Node linkList1Next = linkList1Current.next;

                // 第一个链表当下的节点插入到第二个链表当下节点之前
                linkList1Current.next = linkList2Current;

                // 第一个链表遍历到下一个节点
                linkList1Current = linkList1Next;


            }
            else {
                linkList2Current = linkList2Current.next;
            }
        }
    }


    private static class Node{
        Integer data;
        Node next;
    }




}
