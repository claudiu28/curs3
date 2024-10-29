package ubb.scs.map.domain;

import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private final Long nodPrietenie1;
    private final Long nodPrietenie2;

    public Prietenie(Long nodPrietenie1, Long nodPrietenie2) {
        this.nodPrietenie1 = nodPrietenie1;
        this.nodPrietenie2 = nodPrietenie2;
    }

    public Long getNodPrietenie1() {
        return nodPrietenie1;
    }


    public Long getNodPrietenie2() {
        return nodPrietenie2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(nodPrietenie1, prietenie.nodPrietenie1) && Objects.equals(nodPrietenie2, prietenie.nodPrietenie2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodPrietenie1, nodPrietenie2);
    }
}
