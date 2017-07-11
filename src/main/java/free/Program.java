package free;

import cyclops.control.Unrestricted;
import cyclops.typeclasses.free.Free;

import static cyclops.typeclasses.free.Free.Comprehensions.forEach;
import static free.Command.*;
import static unrestricted.StringBuildingInterpreter.interpret;


public final class Program {


    public static void main(String[] args){
        Free<Command.µ,Void> program = forEach(          output('A'),
                                             __ ->     bell(),
                                             __ ->     output('B'),
                                             __ ->     done());
        System.out.println("*********");
        System.out.println("Executing program with StringBuildingInterpreter..");
        System.out.println();
        System.out.println(interpret(program));
        System.out.println("*********");
        System.out.println("Executing program with EffectfulInterpreter..");
        System.out.println();
        EffectfulInterpreter.interpret(program);
    }




}


