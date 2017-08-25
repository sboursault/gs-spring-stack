package gs;


import com.google.common.collect.Lists;
import gs.model.Aka;
import gs.model.Inmate;
import java.time.LocalDate;

public class InmateExamples {


    public static Inmate.InmateBuilder thePenguin() {
        return Inmate.builder()
                .id("penguin_1234")
                .firstname("Oswald")
                .lastname("Cobblepot")
                .birthDate(LocalDate.of(1960, 05, 31))
                .aka(Lists.newArrayList(Aka.builder().name("Penguin").build()));
    }

    public static Inmate.InmateBuilder theJoker() {
        return Inmate.builder()
                .id("joker_5555")
                .firstname("???")
                .lastname("???")
                .aka(Lists.newArrayList(Aka.builder().name("Joker").build()));
    }
}
