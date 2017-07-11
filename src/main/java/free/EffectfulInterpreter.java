package free;

import cyclops.typeclasses.free.Free;
import free.Command.Bell;
import free.Command.Done;
import free.Command.Output;

import static com.aol.cyclops2.types.mixins.Printable.println;

public class EffectfulInterpreter {

    public static <R> void interpret(Free<Command.µ,R> program){
        //walk the Free data structure and handle each command,
        //by delegating to the appropriate method
        program.resume(new CommandFunctor(),Command::narrowK)
                .visit(
                        r ->   { matchCommand(r); return null;},
                        __->"\n"
                );
    }
    private static <R> String matchCommand(Command<Free<Command.µ, R>> command) {
        command.patternMatch().visit(StringBuildingInterpreter::handleOutput,
                StringBuildingInterpreter::handleBell,
                StringBuildingInterpreter::handleDone);
        return null;
    }
    static <R> void handleOutput(Output<Free<Command.µ,R>> output){
        output.visit((a, next) -> {
            println("emitted " + a);
            interpret(next);
            return null;
        });
    }

    static <R> void handleBell(Bell<Free<Command.µ,R>> bell){
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