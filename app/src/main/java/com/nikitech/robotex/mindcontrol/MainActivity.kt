package com.nikitech.robotex.mindcontrol

import android.Manifest
import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.ActivityCompat
import android.text.Editable
import android.util.Log
import com.choosemuse.libmuse.*
import com.jjoe64.graphview.GraphView
import com.nikitech.robotex.mindcontrol.networking.Networking
import com.nikitech.robotex.mindcontrol.utils.DeviceUtils
import com.nikitech.robotex.mindcontrol.utils.FileUtils
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.File
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.nikitech.robotex.mindcontrol.model.*
import com.nikitech.robotex.mindcontrol.networking.NetworkingDelegate
import com.nikitech.robotex.mindcontrol.subviews.list.MuseListCell
import com.nikitech.robotex.mindcontrol.utils.Colors
import org.jetbrains.anko.sdk25.coroutines.onEditorAction
import org.jetbrains.anko.sdk25.coroutines.onFocusChange
import org.jetbrains.anko.sdk25.coroutines.onItemClick
import java.util.*

class MainActivity : AppCompatActivity(), NetworkingDelegate {

    private var contentView: MainView? = null

    /**
     * We will be updating the UI using a handler instead of in packet handlers because
     * packets come in at a very high frequency and it only makes sense to update the UI
     * at about 60fps. The update functions do some string allocation, so this reduces our memory
     * footprint and makes GC pauses less frequent/noticeable.
     */
    private val handler = Handler()

    /**
     * The MuseManager is how you detect Muse headbands and receive notifications
     * when the list of available headbands changes.
     */
    private var manager: MuseManagerAndroid? = null

    /**
     * A Muse refers to a Muse headband.  Use this to connect/disconnect from the
     * headband, register listeners to receive EEG data and get headband
     * configuration and version information.
     */
    private var muse: Muse? = null

    /**
     * The ConnectionListener will be notified whenever there is a change in
     * the connection state of a headband, for example when the headband connects
     * or disconnects.
     *
     * Note that ConnectionListener is an inner class at the bottom of this file
     * that extends MuseConnectionListener.
     */
    private var connectionListener: ConnectionListener? = null

    /**
     * The DataListener is how you will receive EEG (and other) data from the
     * headband.
     *
     * Note that DataListener is an inner class at the bottom of this file
     * that extends MuseDataListener.
     */
    private var dataListener: DataListener? = null

    /**
     * Data comes in from the headband at a very fast rate; 220Hz, 256Hz or 500Hz,
     * depending on the type of headband and the preset configuration.  We buffer the
     * data that is read until we can update the UI.
     *
     * The stale flags indicate whether or not new data has been received and the buffers
     * hold the values of the last data packet received.  We are displaying the EEG, ALPHA_RELATIVE
     * and ACCELEROMETER values in this example.
     *
     * Note: the array lengths of the buffers are taken from the comments in
     * MuseDataPacketType, which specify 3 values for accelerometer and 6
     * values for EEG and EEG-derived packets.
     */
    private val eegBuffer = DoubleArray(6)
    private var eegStale: Boolean = false
    private val alphaBuffer = DoubleArray(6)
    private var alphaStale: Boolean = false
    private val accelBuffer = DoubleArray(3)
    private var accelStale: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                // finally change the color
                window.statusBarColor = Colors.mainDarker
            }

        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        super.onCreate(savedInstanceState)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        supportActionBar!!.hide()

        // Muse 2016 (MU-02) headbands use Bluetooth Low Energy technology to
        // simplify the connection process.  This requires access to the COARSE_LOCATION
        // or FINE_LOCATION permissions.  Make sure we have these permissions before
        // proceeding.
        if (DeviceUtils.isMarshmallow()) {
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            initMuse()
        }

        contentView = MainView(this)
        setContentView(contentView)

        val timer = Timer()
        val myTask = object : TimerTask() {
            override fun run() {
                updateUI()
                calculateMuseCommand()
            }
        }

        timer.schedule(myTask, 0, (second / 5).toLong())

        Networking.INSTANCE.delegate = this

        contentView!!.connect.addressField.setText(Networking.INSTANCE.getIpAddress())
    }

    override fun onError(message: String) {
        alert(message)
    }

    private val second = 1000
    private val lock = Any()

    private var activeMuse: Muse? = null

    private fun updateUI() {

        val eeg1list = mutableListOf<DataPoint>()
        val eeg2list = mutableListOf<DataPoint>()
        val eeg3list = mutableListOf<DataPoint>()
        val eeg4list = mutableListOf<DataPoint>()
        val eeg5list = mutableListOf<DataPoint>()
        val eeg6list = mutableListOf<DataPoint>()

        synchronized(lock) {
            for ((counter, item) in eeg.withIndex()) {
                eeg1list.add(DataPoint(counter.toDouble(), item.one))
                eeg2list.add(DataPoint(counter.toDouble(), item.two))
                eeg3list.add(DataPoint(counter.toDouble(), item.three))
                eeg4list.add(DataPoint(counter.toDouble(), item.four))
                eeg5list.add(DataPoint(counter.toDouble(), item.auxLeft))
                eeg6list.add(DataPoint(counter.toDouble(), item.auxRight))
            }
        }

        runOnUiThread {
            addListToGraph(contentView!!.EEG.get(0), eeg1list, Color.BLUE)
            addListToGraph(contentView!!.EEG.get(1), eeg2list, Color.GREEN)
            addListToGraph(contentView!!.EEG.get(2), eeg3list, Color.RED)
            addListToGraph(contentView!!.EEG.get(3), eeg4list, Color.BLACK)
            addListToGraph(contentView!!.EEG.get(4), eeg5list, Color.CYAN)
            addListToGraph(contentView!!.EEG.get(5), eeg6list, Color.MAGENTA)
        }

        val xList = mutableListOf<DataPoint>()
        val yList = mutableListOf<DataPoint>()
        val zList = mutableListOf<DataPoint>()

        synchronized(lock) {
            for ((counter, item) in accelerometer.withIndex()) {
                xList.add(DataPoint(counter.toDouble(), item.x))
                yList.add(DataPoint(counter.toDouble(), item.y))
                zList.add(DataPoint(counter.toDouble(), item.z))
            }
        }

        runOnUiThread {
            addListToGraph(contentView!!.accelerometer.get(0), xList, Color.BLUE)
            addListToGraph(contentView!!.accelerometer.get(1), yList, Color.GREEN)
            addListToGraph(contentView!!.accelerometer.get(2), zList, Color.RED)
        }
    }

    private fun calculateMuseCommand() {

        /*
         * Kui ma vaatasin EEG2 järgi, siis 500-1000 ei võiks midagi olla.
         * Alla selle pöörab paremale ja üle selle sõidab otse
         */

        val median = getMedianOf(eeg, 10)

        if (median < 500) {
            Networking.INSTANCE.right()
            print("Sending command: Right")
        } else if (median > 1000) {
            Networking.INSTANCE.forward()
            print("Sending command: Forward")
        }
    }

    private fun getMedianOf(eeg: MutableList<EEGValue>, count: Int): Int {

        synchronized(lock) {

            val amount = eeg.takeLast(count)

            var total = 0.0

            for (item in amount) {
                total += item.two
            }
            return total.toInt() / count
        }
    }

    override fun onResume() {
        super.onResume()

        contentView!!.buttons.left.onClick {
            Networking.INSTANCE.left()
        }

        contentView!!.buttons.forward.onClick {
            Networking.INSTANCE.forward()
        }

        contentView!!.buttons.right.onClick {
            Networking.INSTANCE.right()
        }

        contentView!!.buttons.reverse.onClick {
            Networking.INSTANCE.reverse()
        }

        contentView!!.buttons.stop.onClick {
            Networking.INSTANCE.stop()
        }

        contentView!!.connect.refresh.onClick {
            manager!!.stopListening()
            manager!!.startListening()
        }

        for (item in contentView!!.tabBar.list) {
            item.onClick {
                contentView!!.setActiveItem(item)
            }
        }

        contentView!!.connect.connect.onClick {

            activeMuse!!.registerConnectionListener(connectionListener)
            activeMuse!!.registerDataListener(dataListener, MuseDataPacketType.EEG)
            activeMuse!!.registerDataListener(dataListener, MuseDataPacketType.ALPHA_RELATIVE)
            activeMuse!!.registerDataListener(dataListener, MuseDataPacketType.ACCELEROMETER)
            activeMuse!!.registerDataListener(dataListener, MuseDataPacketType.BATTERY)
            activeMuse!!.registerDataListener(dataListener, MuseDataPacketType.DRL_REF)
            activeMuse!!.registerDataListener(dataListener, MuseDataPacketType.QUANTIZATION)

            activeMuse!!.runAsynchronously()
        }

        val context = this
        contentView!!.connect.list.onItemClick { _, view, position, _ ->

            val cell = view as MuseListCell

            context.activeMuse = cell.muse

            contentView!!.connect.adapter.selectedItem = position
            contentView!!.connect.adapter.notifyDataSetChanged()
            contentView!!.connect.connect.show()
        }

        contentView!!.connect.addressField.onEditorAction { v, _, _ ->
            Networking.INSTANCE.updateIpAddress(v!!.text.toString().trim())
        }
    }

    override fun onPause() {
        super.onPause()

        contentView!!.buttons.left.setOnClickListener(null)
        contentView!!.buttons.forward.setOnClickListener(null)
        contentView!!.buttons.right.setOnClickListener(null)
        contentView!!.buttons.reverse.setOnClickListener(null)
        contentView!!.buttons.stop.setOnClickListener(null)

        contentView!!.connect.refresh.setOnClickListener(null)

        // It is important to call stopListening when the Activity is paused
        // to avoid a resource leak from the LibMuse library.
        if (isPermissionGranted) {
            manager!!.stopListening()
        }

        contentView!!.connect.addressField.setOnEditorActionListener(null)
    }

    private fun initMuse() {

        // We need to set the context on MuseManagerAndroid before we can do anything.
        // This must come before other LibMuse API calls as it also loads the library.
        manager = MuseManagerAndroid.getInstance()
        manager!!.setContext(this)

        connectionListener = ConnectionListener(this)
        dataListener = DataListener(this)

        manager!!.setMuseListener(ChangeListener(this))

        // Start up a thread for asynchronous file operations.
        // This is only needed if you want to do File I/O.
        fileThread.start()
    }

    /**
     * We don't want to block the UI thread while we write to a file, so the file
     * writing is moved to a separate thread.
     */
    private val fileThread = object : Thread() {
        override fun run() {

            Looper.prepare()
            FileUtils.INSTANCE.fileHandler.set(Handler())

            val dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(dir, "new_muse_file.muse")

            // MuseFileWriter will append to an existing file.
            // In this case, we want to start fresh so the file
            // if it exists.
            if (file.exists()) {
                file.delete()
            }

            Log.i("MainActivity", "Writing data to: " + file.absolutePath)
            FileUtils.INSTANCE.fileWriter.set(MuseFileFactory.getMuseFileWriter(file))
            Looper.loop()
        }
    }

    private fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
    }

    private var isPermissionGranted = false

    private val PERMISSION_REQUEST_COARSE_LOCATION = 1

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {

                if (grantResults.isEmpty()) {
                    return
                }

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "coarse location permission granted")
                    initMuse()
                    isPermissionGranted = true
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, " + "this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }
        }
    }

    fun museListChanged() {
        if (manager?.muses!!.size == 0) {
            return
        }

//        val list = mutableListOf<Muse>()
//        list.add(manager!!.muses[0])
//        list.add(manager!!.muses[0])
//        list.add(manager!!.muses[0])
//        contentView!!.connect.refreshList(list)

        contentView!!.connect.refreshList(manager!!.muses)


    }

    fun receiveMuseConnectionPacket(p: MuseConnectionPacket, muse: Muse) {
        val current = p.currentConnectionState

        // Format a message to show the change of connection state in the UI.
        val status = p.previousConnectionState.toString() + " -> " + current
        Log.i("MainActivity", status)

        if (current == ConnectionState.CONNECTING) {
            alert("Connecting")
        }
        if (current == ConnectionState.CONNECTED) {
            alert("Connected!")
        }
        if (current == ConnectionState.DISCONNECTED) {
            Log.i("MainActivity", "Muse disconnected:" + muse.name)
            // Save the data file once streaming has stopped.
            FileUtils.INSTANCE.save()
            // We have disconnected from the headband, so set our cached copy to null.
            this.muse = null
        }
    }

    private fun alert(text: String) {
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun receiveMuseDataPacket(p: MuseDataPacket, muse: Muse) {

        FileUtils.INSTANCE.writeDataPacketToFile(p)

        // valuesSize returns the number of data values contained in the packet.
        val n = p.valuesSize()
        when (p.packetType()) {
            MuseDataPacketType.EEG -> {
                assert(eegBuffer.size >= n)
                setEegChannelValues(eegBuffer, p)
                eegStale = true
            }
            MuseDataPacketType.ACCELEROMETER -> {
                assert(accelBuffer.size >= n)
                setAccelValues(p)

                accelStale = true
            }
            MuseDataPacketType.ALPHA_RELATIVE -> {
                assert(alphaBuffer.size >= n)
//                setEegChannelValues(alphaBuffer, p)
                alphaStale = true
            }
            MuseDataPacketType.BATTERY, MuseDataPacketType.DRL_REF, MuseDataPacketType.QUANTIZATION -> {
            }
            else -> {
            }
        }
    }

    fun receiveMuseArtifactPacket(packet: MuseArtifactPacket, muse: Muse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private val eeg = mutableListOf<EEGValue>()

    private fun setEegChannelValues(buffer: DoubleArray, p: MuseDataPacket) {

        if (eeg.size > MainView.DEFAULT_X_BOUNDS) {
            eeg.clear()
        }

        buffer[0] = p.getEegChannelValue(Eeg.EEG1)
        buffer[1] = p.getEegChannelValue(Eeg.EEG2)
        buffer[2] = p.getEegChannelValue(Eeg.EEG3)
        buffer[3] = p.getEegChannelValue(Eeg.EEG4)
        buffer[4] = p.getEegChannelValue(Eeg.AUX_LEFT)
        buffer[5] = p.getEegChannelValue(Eeg.AUX_RIGHT)

        synchronized(lock) {
            eeg.add(EEGValue.fromMuseDataPacket(p))
        }
    }

    private val accelerometer = mutableListOf<AccelerometerValue>()

    private fun setAccelValues(p: MuseDataPacket) {

        if (!contentView!!.tabBar.isActiveTabAccelerometer()) {
            return
        }

        accelBuffer[0] = p.getAccelerometerValue(Accelerometer.X)
        accelBuffer[1] = p.getAccelerometerValue(Accelerometer.Y)
        accelBuffer[2] = p.getAccelerometerValue(Accelerometer.Z)

        synchronized(lock) {
            accelerometer.add(AccelerometerValue(accelBuffer[0], accelBuffer[1], accelBuffer[2]))
        }
    }

    private fun addListToGraph(graph: GraphView, list: List<DataPoint>, color: Int) {
        graph.removeAllSeries()

        val series = LineGraphSeries(Collections.synchronizedCollection(list).toTypedArray())
        series.backgroundColor = color
        graph.addSeries(series)
    }

}
