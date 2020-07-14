package ru.galuzin.jcsterss_samples;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.LL_Result;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@JCStressTest
@Outcome(id = {"a, a", "b, b" }, expect = Expect.ACCEPTABLE_INTERESTING, desc = "both set")
@Outcome(id = {"a, b" }, expect = Expect.ACCEPTABLE_INTERESTING, desc = "a b set")
@Outcome(id = {"b, a" }, expect = Expect.ACCEPTABLE_INTERESTING, desc = "b a set")
@State
public class ConcurrentHashMapTest2 {


//    final HashMap<String, Pair> chm;

    static AtomicInteger counter = new AtomicInteger(0);

    final Pair v;

    public ConcurrentHashMapTest2() {
//        chm = new HashMap<>();
//        chm.put("key", new Pair());
        v = new Pair();
        System.err.println("gal count " + counter.incrementAndGet());
    }

    /**
     * Тест создается каждый раз новый, actor1, actor2 выполняются по разу
     */
    @Actor
    public void actor1() {
        //chm.computeIfPresent("key", (k, v) ->{
            //if (v.f.equals("a")) {
                v.f = "b";
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v.s = "b";
            //}
            //return v;
        //});
    }

    @Actor
    public void actor2(LL_Result r) {
        //final Pair v = chm.get("key");
        r.r1 = v.f;
        r.r2 = v.s;
    }

    public static class Pair {
        public String f = "a";
        public String s = "a";
        //todo add list and add, remove elements from it
    }

}
