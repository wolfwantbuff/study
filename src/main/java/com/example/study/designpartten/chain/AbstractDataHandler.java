package com.example.study.designpartten.chain;

import org.springframework.core.Ordered;

public abstract class AbstractDataHandler<T> implements Ordered {
    protected AbstractDataHandler<T> next = null;

    public abstract void doHandler(T object);


    public static class Builder<T> {
        private AbstractDataHandler<T> head;
        private AbstractDataHandler<T> tail;
        public void addHandler(AbstractDataHandler<T> handler) {
            if (this.head == null) {
                this.head = handler;
            } else {
                this.tail.next = handler;
            }
            this.tail = handler;
        }
        public AbstractDataHandler<T> build() {
            return this.head;
        }
    }
}
