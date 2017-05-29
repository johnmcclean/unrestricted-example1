package unrestricted;

import cyclops.control.Unrestricted;
import unrestricted.Command.Output;

import static cyclops.control.Unrestricted.Comprehensions.forEach;
import static unrestricted.Command.bell;
import static unrestricted.Command.done;
import static unrestricted.Command.output;
import static unrestricted.StringBuildingInterpreter.interpret;


public final class Program {


    public static void main(String[] args){
        Unrestricted<Void> program = forEach(          output('A'),
                                             __ ->     bell(),
                                             __ ->     output('B'),
                                             __ ->     done());
        System.out.println(interpret(program));
    }




}


