package sakura.kooi.virtualgraphictablets;

@FunctionalInterface
interface TriConsumer<A,B,C> {
    void apply(A a, B b, C c);
}