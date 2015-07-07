package co.gphl;

import java.io.File;
import java.io.IOException;

import co.gphl.common.namelist.F90NamelistData;
import co.gphl.common.namelist.F90NamelistGroup;
import co.gphl.common.namelist.impl.F90NamelistImpl;
import co.gphl.sdcp.F90Namelist.v2.GcalAuxGroupFactory;

public class NamelistRead {

    public static void main(String[] args) throws IOException {

        if ( args.length != 1 )
            throw new RuntimeException("Need input filename as argument");

        F90NamelistData data = new F90NamelistImpl( GcalAuxGroupFactory.factory(), new File(args[0]) );
        
        for ( int i = 0; i < data.size(); i++ ) {
            F90NamelistGroup group = data.get(i);
            System.out.println("Group " + i + " is a " + group.getClass().getSimpleName());
        }
        
        data.write( new File( args[0] + ".new") );
        
        System.out.println("Done");
        
    }

}
