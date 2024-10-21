package ubb.scs.map.domain;

public class Prietenie extends Entity<Long> {
    private Long nodPrietenie1;
    private Long nodPrietenie2;

    public Prietenie(Long nodPrietenie1, Long nodPrietenie2) {
        this.nodPrietenie1 = nodPrietenie1;
        this.nodPrietenie2 = nodPrietenie2;
    }

    public Long getNodPrietenie1() {
        return nodPrietenie1;
    }

    public void setNodPrietenie1(Long nodPrietenie1) {
        this.nodPrietenie1 = nodPrietenie1;
    }

    public Long getNodPrietenie2() {
        return nodPrietenie2;
    }

    public void setNodPrietenie2(Long nodPrietenie2) {
        this.nodPrietenie2 = nodPrietenie2;
    }
}
