package co.gphl;

import java.io.File;
import java.io.IOException;

import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistImpl;

public class NamelistTest2 {

    public static void main(String[] args) throws IOException {

        if ( args.length != 1 )
            throw new RuntimeException("Need output filename as argument");
        
        F90NamelistData data = new F90NamelistImpl();
        
        // Start with a LOOP_COUNT_LIST
        
        F90NamelistGroup loopCounts = new EXLoopCountGroup(64, 62, 120, 1);
        data.add(loopCounts);
        
        data.write( new File(args[0]) );

    }

}
