package free;

import com.aol.cyclops2.hkt.Higher;
import com.aol.cyclops2.types.functor.Transformable;
import cyclops.control.Unrestricted;
import cyclops.control.lazy.Either3;
import cyclops.typeclasses.free.Free;

import java.util.function.BiFunction;
import java.util.function.Function;

//Command from https://github.com/xuwei-k/free-monad-java
abstract class Command<T> implements Higher<Command.µ,T> {

    public static <T> Command<T> narrowK(Higher<µ, T> ds) {
        return (Command<T>)ds;
    }

    public static class µ{}


    public final static <T> Function<Transformable<Free<Command.µ,T>>, Command<Free<Command.µ,T>>> decoder() {
        return c->(Command<Free<Command.µ,T>>)c;
    }

    public static Free<µ,String> output(final char a){
        return Free.liftF(new Output<>(a, null), new CommandFunctor());
    }
    public static Free<Command.µ,Void> bell(){
        return Free.liftF(new Bell<>(null), new CommandFunctor());
    }
    public static Free<Command.µ,Void> done(){
        return Free.liftF(new Done<Void>(), new CommandFunctor());
    }
    public static <A> Free<Command.µ,A> pointed(final A a){
        return Free.done(a);
    }
    public abstract <R> Command<R> map(final Function<? super T,? extends R> f);

    public abstract Either3<Output<T>, Bell<T>, Done<T>> patternMatch();

    private Command(){}




    static final class Output<T> extends Command<T> {
        private final char a;
        private final T next;

        private Output(final char a, final T next) {
            this.a = a;
            this.next = next;
        }



        public <R> R visit(final BiFunction<Character, ? super T, ? extends R> output) {
            return output.apply(a, next);
        }

        @Override
        public <R> Command<R> map(final Function<? super T,? extends R> f) {
            return new Command.Output<>(a, f.apply(next));
        }

        @Override
        public Either3<Output<T>, Bell<T>, Done<T>> patternMatch() {
            return Either3.left1(this);
        }
    }

    static final class Bell<T> extends Command<T> {
        private final T next;
        private Bell(final T next) {
            this.next = next;
        }


        public <R> R visit(final Function<? super T, ? extends R> bell) {
            return bell.apply(next);
        }

        @Override
        public <R> Command<R> map(final Function<? super T,? extends R> f) {
            return new Command.Bell<>(f.apply(next));
        }

        @Override
        public Either3<Output<T>, Bell<T>, Done<T>> patternMatch() {
            return Either3.left2(this);
        }
    }

    static final class Done<T> extends Command<T> {


        public <T> T visit(final T done) {
            return done;
        }

        @Override
        public <R> Command<R> map(final Function<? super T,? extends R> f) {
            return new Command.Done<>();
        }

        @Override
        public Either3<Output<T>, Bell<T>, Done<T>> patternMatch() {
            return Either3.right(this);
        }
    }
}
