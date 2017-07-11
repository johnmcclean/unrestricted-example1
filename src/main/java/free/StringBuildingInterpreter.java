package free;

import cyclops.typeclasses.free.Free;
import free.Command.Bell;
import free.Command.Done;
import free.Command.Output;


public class StringBuildingInterpreter {

    public static <R> String interpret(Free<Command.µ,R> program) {
        //walk the Free data structure and handle each command,
        //by delegating to the appropriate method

       return program.resume(new CommandFunctor(), Command::narrowK)
                .visit(
                        r -> r.patternMatch()
                                .visit(StringBuildingInterpreter::handleOutput,
                                        StringBuildingInterpreter::handleBell,
                                        StringBuildingInterpreter::handleDone)
                        ,
                        __ -> "\n"
                );

    }

     static <R> String handleOutput(Output<Free<Command.µ,R>> output){
        return output.visit((a, next) -> "emitted " + a + "\n" + interpret(next));
    }

    static <R> String handleBell(Bell<Free<Command.µ,R>> bell){
        return bell.visit(next -> "bell " + "\n" + interpret(next));
    }

    static <T> String handleDone(Done<T> done){
        return "done\n";
    }
}