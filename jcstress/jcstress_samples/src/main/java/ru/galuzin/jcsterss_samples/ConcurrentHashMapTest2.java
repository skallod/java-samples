package ru.galuzin.jcsterss_samples;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.LL_Result;

import java.util.concurrent.ConcurrentHashMap;

@JCStressTest
@Outcome(id = {"a, a", "b, b" }, expect = Expect.ACCEPTABLE_INTERESTING, desc = "both set")
@Outcome(id = {"a, b" }, expect = Expect.ACCEPTABLE_INTERESTING, desc = "a b set")
@Outcome(id = {"b, a" }, expect = Expect.ACCEPTABLE_INTERESTING, desc = "b a set")
@State
public class ConcurrentHashMapTest2 {


    final ConcurrentHashMap<String, Pair> chm;

    public ConcurrentHashMapTest2() {
        chm = new ConcurrentHashMap<>();
        chm.put("key", new Pair());
    }

    @Actor
    public void actor1() {
        chm.computeIfPresent("key", (k, v) ->{
            if (v.f.equals("a")) {
                v.f = "b";
                v.s = "b";
            } else {
                v.f = "a";
                v.s = "a";
            }
            return v;
        });
    }

    @Actor
    public void actor2(LL_Result r) {
        final Pair v = chm.get("key");
        r.r1 = v.f;
        r.r2 = v.s;
    }

    public static class Pair {
        public String f = "a";
        public String s = "a";
        //todo add list and add, remove elements from it
    }

}
