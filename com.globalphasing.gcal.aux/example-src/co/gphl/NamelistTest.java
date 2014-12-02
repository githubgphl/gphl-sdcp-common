package co.gphl;

import java.io.File;
import java.io.IOException;

import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistImpl;
import co.gphl.sdcp.F90NamelistGroup.v2.impl.LoopCountGroup;

public class NamelistTest {

    public static void main(String[] args) throws IOException {

        if ( args.length != 1 )
            throw new RuntimeException("Need output filename as argument");
        
        F90NamelistData data = new F90NamelistImpl();
        
        // Start with a LOOP_COUNT_LIST
        
        F90NamelistGroup loopCounts = new LoopCountGroup(null);
        data.add(loopCounts);
        
        loopCounts.put(LoopCountGroup.nSegments, 120 );
        loopCounts.put(LoopCountGroup.nTriangles, 62);
        loopCounts.put(LoopCountGroup.nVertices, 64);
        loopCounts.put(LoopCountGroup.nCrystals, 1);
        
        
        data.write( new File(args[0]) );
        
        
    }

}
