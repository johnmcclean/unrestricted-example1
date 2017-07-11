package free;

import com.aol.cyclops2.hkt.Higher;
import cyclops.typeclasses.functor.Functor;

import java.util.function.Function;

public class CommandFunctor implements Functor<Command.µ> {
    @Override
    public <T, R> Higher<Command.µ, R> map(Function<? super T, ? extends R> fn, Higher<Command.µ, T> ds) {
        Command<T> c = Command.narrowK(ds);
        return Command.narrowK(ds).map(fn);
    }
}
