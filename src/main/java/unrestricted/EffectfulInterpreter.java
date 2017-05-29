package unrestricted;

import com.aol.cyclops2.types.mixins.Printable;
import cyclops.control.Unrestricted;
import unrestricted.Command.Bell;
import unrestricted.Command.Done;
import unrestricted.Command.Output;

import static com.aol.cyclops2.types.mixins.Printable.println;

public class EffectfulInterpreter {

    public static <R> void interpret(Unrestricted<R> program){
        //walk the Free data structure and handle each command,
        //by delegating to the appropriate method
        program.resume(Command.decoder())
                .visit(
                        r ->   { matchCommand(r); return null;},
                        __->"\n"
                );
    }
    private static <R> void matchCommand(Command<R> command){
        if(command instanceof Output) {
            handleOutput((Output<Unrestricted<R>>) command);
            return;
        }
        else if(command instanceof Bell) {
            handleBell((Bell<Unrestricted<R>>) command);
            return;
        }
        else if(command instanceof Done) {
            handleDone((Done<R>) command);
            return;
        }
        throw new IllegalArgumentException("Unknown command " + command);
    }

    static <R> void handleOutput(Output<Unrestricted<R>> output){
        output.visit((a, next) -> {
            println("emitted " + a);
            interpret(next);
            return null;
        });
    }

    static <R> void handleBell(Bell<Unrestricted<R>> bell){
        bell.visit(next ->{
            java.awt.Toolkit.getDefaultToolkit().beep();
            interpret(next);
            return null;
        });
    }

    static <T> void handleDone(Done<T> done){
        println("done");
    }
}