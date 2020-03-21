package r.sardorbek.mytaxiapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import kotlinx.android.synthetic.main.activity_main.*
import r.sardorbek.mytaxiapp.adapter.TripAdapter
import java.util.*

class MainActivity : AppCompatActivity() {
    private var tripHistory: ArrayList<Data>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(R.string.appBar_Title) //AppBar title set to "Trip history"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tripHistory = ArrayList<Data>()

        //Setting some random data for listView
        sampleData()

        val tripAdapter = TripAdapter(this, R.layout.activity_main, tripHistory)
        tripHistoryList.adapter = tripAdapter
        tripHistoryList.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (isGPSEnabled) {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            }
        }
    }

    //Sample data of addresses and prices for List View
    private fun sampleData() {
        tripHistory!!.add(Data("Sagbon 156 a block, Olmazor district", "Amir Temur Avenue 2a block 23 A", "38,500sum"))
        tripHistory!!.add(Data("Istiqbol 20 h, Mirabad dis.", "Sharaf Rashidov, prospect", "14,200sum"))
        tripHistory!!.add(Data("Muqimi 2a h, Chilanzar dis.", "Qumbuloq 13 h, Uchtepa dis.", "9,500sum"))
        tripHistory!!.add(Data("Shodlik 60 h, Uchtepa dis.", "Amir Temur Avenue 2a block 23 A", "12,500sum"))
        tripHistory!!.add(Data("Sharaf Raswhidov, prospect", "Muqimi 2a h, Chilanzar dis.", "29,00sum"))
        tripHistory!!.add(Data("Sagbon 156 a block, Olmazor district", "Amir Temur Avenue 2a block 23 A", "38,500sum"))
        tripHistory!!.add(Data("Istiqbol 20 h, Mirabad dis.", "Sharaf Rashidov, prospect", "14,200sum"))
        tripHistory!!.add(Data("Muqimi 2a h, Chilanzar dis.", "Qumbuloq 13 h, Uchtepa dis.", "9,500sum"))
        tripHistory!!.add(Data("Shodlik 60 h, Uchtepa dis.", "Amir Temur Avenue 2a block 23 A", "12,500sum"))
        tripHistory!!.add(Data("Sharaf Raswhidov, prospect", "Muqimi 2a h, Chilanzar dis.", "29,000sum"))
    }

    //Checks whether location is enabled or not
    private val isGPSEnabled: Boolean
        get() {
            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageLocationOff()
                return false
            }
            return true
        }

    //Alert message tells you to turn on your location
    private fun buildAlertMessageLocationOff() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("The application requires GPS to work, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS") { dialog, id -> startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), PERMISSIONS_REQUEST_ENABLE_GPS) }
        val alert = builder.create()
        alert.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (parentActivityIntent == null) {
                    onBackPressed()
                } else {
                    NavUtils.navigateUpFromSameTask(this)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ENABLE_GPS = 1
    }
}