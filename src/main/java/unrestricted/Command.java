package unrestricted;

import com.aol.cyclops2.types.functor.Transformable;
        import cyclops.control.Unrestricted;

import java.util.function.BiFunction;
import java.util.function.Function;

//Command from https://github.com/xuwei-k/free-monad-java
abstract class Command<T> implements Transformable<T> {


    public final static <T> Function<Transformable<Unrestricted<T>>,Command<Unrestricted<T>>> decoder() {
        return c->(Command<Unrestricted<T>>)c;
    }

    public static Unrestricted<String> output(final char a){
        return Unrestricted.liftF(new Output<>(a, null));
    }
    public static Unrestricted<Void> bell(){
        return Unrestricted.liftF(new Bell<>(null));
    }
    public static Unrestricted<Void> done(){
        return Unrestricted.liftF(new Done<Void>());
    }
    public static <A> Unrestricted<A> pointed(final A a){
        return Unrestricted.done(a);
    }


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
            return new Output<>(a, f.apply(next));
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
            return new Bell<>(f.apply(next));
        }
    }

    static final class Done<T> extends Command<T> {


        public <T> T visit(final T done) {
            return done;
        }

        @Override
        public <R> Command<R> map(final Function<? super T,? extends R> f) {
            return new Done<>();
        }
    }
}
