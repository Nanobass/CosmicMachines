package net.paxyinc.region;

import java.io.IOException;
import java.io.OutputStream;

public interface RegionFileWriteHandler {

    boolean write(int index, OutputStream os) throws IOException;

}
