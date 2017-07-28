package gs;


import com.google.common.collect.Lists;

public class InmateExamples {


    public static Inmate.InmateBuilder thePenguin() {
        return Inmate.builder()
                .id("penguin_1234")
                .firstname("Oswald")
                .lastname("Cobblepot")
                .aka(Lists.newArrayList("Penguin"));
    }
}
