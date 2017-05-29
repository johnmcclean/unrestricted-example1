package unrestricted;

import com.aol.cyclops2.types.Unwrapable;
import cyclops.control.Unrestricted;
import unrestricted.Command.Bell;
import unrestricted.Command.Done;
import unrestricted.Command.Output;

public class StringBuildingInterpreter {

    public static <R> String interpret(Unrestricted<R> program){
        //walk the Free data structure and handle each command,
        //by delegating to the appropriate method
        return program.resume(Command.decoder())
                .visit(
                        r ->    matchCommand(r),
                        __->"\n"
                );
    }
    private static <R> String matchCommand(Command<R> command){
        if(command instanceof Output)
            return handleOutput((Output<Unrestricted<R>>)command);
        else if(command instanceof Bell)
            return handleBell((Bell<Unrestricted<R>>)command);
        else if(command instanceof Done)
            return handleDone((Done<R>)command);
        throw new IllegalArgumentException("Unknown command " + command);
    }

    static <R> String handleOutput(Output<Unrestricted<R>> output){
        return output.visit((a, next) -> "emitted " + a + "\n" + interpret(next));
    }

    static <R> String handleBell(Bell<Unrestricted<R>> bell){
        return bell.visit(next -> "bell " + "\n" + interpret(next));
    }

    static <T> String handleDone(Done<T> done){
        return "done\n";
    }
}