package com.nikitech.robotex.mindcontrol.utils

import android.os.Handler
import com.choosemuse.libmuse.MuseDataPacket
import com.choosemuse.libmuse.MuseFileWriter
import java.util.concurrent.atomic.AtomicReference

class FileUtils {

    companion object {
        @JvmStatic val INSTANCE = FileUtils()
    }
    /**
     * To save data to a file, you should use a MuseFileWriter.  The MuseFileWriter knows how to
     * serialize the data packets received from the headband into a compact binary format.
     * To read the file back, you would use a MuseFileReader.
     */
    val fileWriter = AtomicReference<MuseFileWriter>()

    /**
     * We don't want file operations to slow down the UI, so we will defer those file operations
     * to a handler on a separate thread.
     */
    val fileHandler = AtomicReference<Handler>()

    /**
     * Writes the provided MuseDataPacket to the file.  MuseFileWriter knows
     * how to write all packet types generated from LibMuse.
     * @param p     The data packet to write.
     */
    fun writeDataPacketToFile(p: MuseDataPacket) {

        val h = fileHandler.get()
        h?.post { fileWriter.get().addDataPacket(0, p) }
    }

    /**
     * Flushes all the data to the file and closes the file writer.
     */
    fun save() {

        val h = fileHandler.get()

        h?.post {
            val w = fileWriter.get()
            // Annotation strings can be added to the file to
            // give context as to what is happening at that point in
            // time.  An annotation can be an arbitrary string or
            // may include additional AnnotationData.
            w.addAnnotationString(0, "Disconnected")
            w.flush()
            w.close()
        }
    }

}